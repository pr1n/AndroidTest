package com.apes.compiler.nav

import com.apes.annotation.nav.Destination
import com.apes.compiler.nav.NavProcessor.Companion.MODULE_NAME
import com.apes.compiler.nav.help.BuildNavExtHelp
import com.apes.compiler.nav.help.BuildNavKeepStateFragmentHelp
import com.apes.compiler.nav.help.NavJsonHelp
import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(MODULE_NAME)
@SupportedAnnotationTypes("com.apes.annotation.nav.Destination")
class NavProcessor : AbstractProcessor() {

    companion object {
        const val MODULE_NAME = "APES_MODULE_NAME"
        const val DEFAULT_MODULE_NAME = "app"
    }

    private lateinit var messager: Messager
    private lateinit var filer: Filer
    private lateinit var moduleName: String

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        messager = processingEnv.messager
        filer = processingEnv.filer
        messager.printMessage(Diagnostic.Kind.NOTE, "NavProcessor Init.")
        moduleName = processingEnv.options.getOrDefault(MODULE_NAME, DEFAULT_MODULE_NAME)
    }

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        messager.printMessage(Diagnostic.Kind.NOTE, "NavProcessor Process Execute.")
        val elements = roundEnv.getElementsAnnotatedWith(Destination::class.java)
        if (elements.isNullOrEmpty()) return false
        BuildNavKeepStateFragmentHelp().createKeepStateFragmentNavigatorKotlinFile(filer)
        BuildNavExtHelp().createKotlinFile(filer)
        NavJsonHelp(messager, moduleName, filer, elements).init()
        return false
    }
}

