package com.apes.compiler.nav.help

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.apes.annotation.nav.Destination
import com.apes.compiler.nav.PageType
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.tools.Diagnostic
import javax.tools.StandardLocation
import kotlin.math.abs

class NavJsonHelp(
    private val messager: Messager,
    private val moduleName: String,
    private val filer: Filer,
    private val elements: Set<Element>
) {

    companion object {
        /**
         * 默认文件名称
         */
        const val OUTPUT_FILE_NAME = "Destination.json"
    }

    /**
     * 入口
     */
    fun init() {
        val destinationMap = mutableMapOf<String, JsonObject>()
        handleDestination(elements, Destination::class.java, destinationMap)

        val fileObject =
            (filer as Closeable).use {
                (it as Filer).createResource(
                    StandardLocation.CLASS_OUTPUT, "",
                    OUTPUT_FILE_NAME, null
                )
            }

        val resourcePath = fileObject.toUri().path

        val appPath =
            resourcePath.substring(1, resourcePath.indexOf(moduleName) + moduleName.length + 1)
        val assetsPath = "${appPath}src/main/assets"

        val file = File(assetsPath)
        if (!file.exists()) file.mkdirs()

        val content = Gson().toJson(destinationMap)

        val outputFile = File(assetsPath, OUTPUT_FILE_NAME)
        if (outputFile.exists()) outputFile.delete()
        outputFile.createNewFile()
        FileOutputStream(outputFile).use { fos ->
            fos.bufferedWriter().use { bf ->
                bf.write(content)
            }
        }
    }

    /**
     * 处理导航
     */
    private fun handleDestination(
        elements: Set<Element>,
        destinationClass: Class<Destination>,
        destMap: MutableMap<String, JsonObject>
    ) {
        elements.forEach {
            val typeElement = it as TypeElement

            val className = typeElement.qualifiedName.toString()

            val annotation = typeElement.getAnnotation(destinationClass)
            val pageUrl = annotation.pageUrl
            val asStarter = annotation.asStarter
            val destinationId = abs(className.hashCode())

            val destinationType = getDestinationType(typeElement)

            if (destMap.containsKey(pageUrl)) {
                messager.printMessage(Diagnostic.Kind.ERROR, "不同页面不允许使用相同的pageUrl:$pageUrl")
            } else {
                val jsonObject = JsonObject()
                jsonObject.addProperty("className", className)
                jsonObject.addProperty("pageUrl", pageUrl)
                jsonObject.addProperty("asStarter", asStarter)
                jsonObject.addProperty("destinationId", destinationId)
                jsonObject.addProperty("destinationType", destinationType?.type ?: "")

                destMap += pageUrl to jsonObject
            }
        }
    }

    /**
     * 获取导航类型(Activity, Fragment, Dialog)
     */
    private tailrec fun getDestinationType(typeElement: TypeElement): PageType? {
        val typeMirror = typeElement.superclass
        val className = typeMirror.toString()
        when {
            className.contains(
                PageType.PAGE_TYPE_ACTIVITY.type,
                true
            ) -> return PageType.PAGE_TYPE_ACTIVITY
            className.contains(
                PageType.PAGE_TYPE_FRAGMENT.type,
                true
            ) -> return PageType.PAGE_TYPE_FRAGMENT
            className.contains(
                PageType.PAGE_TYPE_DIALOG.type,
                true
            ) -> return PageType.PAGE_TYPE_DIALOG
        }

        if (typeMirror is DeclaredType) {
            val element = typeMirror.asElement()
            if (element is TypeElement)
                return getDestinationType(element)
        }
        return null
    }
}