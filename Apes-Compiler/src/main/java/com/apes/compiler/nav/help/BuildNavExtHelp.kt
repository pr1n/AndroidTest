package com.apes.compiler.nav.help

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.apes.compiler.nav.PageType
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.KOperator
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.MAP
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT
import com.squareup.kotlinpoet.asTypeName
import java.io.Closeable
import javax.annotation.processing.Filer

class BuildNavExtHelp(private val packageName: String = "com.apes.nav") {

    /**
     * 创建Kitlin（NavExt.kt）文件
     */
    fun createKotlinFile(filer: Filer) {
        try {
            val kotlinFile = FileSpec.builder(packageName, "NavExt")
                .addType(createDestinationDataClass())
                .addFunction(createGetDestinationsFuncation())
                .addFunction(createBuildNavGraphFuncation())
                .build()
            kotlinFile.writeTo(filer)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            (filer as Closeable).close()
        }
    }

    /**
     * 创建数据类Destination
     */
    private fun createDestinationDataClass(): TypeSpec {
        val classBuilder = TypeSpec.classBuilder("DestinationData")
            .addModifiers(KModifier.DATA)
        val constrctor = FunSpec.constructorBuilder()
            .addParameter("className", STRING)
            .addParameter("pageUrl", STRING)
            .addParameter("asStarter", BOOLEAN)
            .addParameter("destinationId", LONG)
            .addParameter("destinationType", STRING)
            .build()

        val className = PropertySpec.builder("className", STRING).initializer("className").build()
        val pageUrl = PropertySpec.builder("pageUrl", STRING).initializer("pageUrl").build()
        val asStarter = PropertySpec.builder("asStarter", BOOLEAN).initializer("asStarter").build()
        val destinationId =
            PropertySpec.builder("destinationId", LONG).initializer("destinationId").build()
        val destinationType =
            PropertySpec.builder("destinationType", STRING).initializer("destinationType").build()
        classBuilder.primaryConstructor(constrctor)
        classBuilder.addProperty(className)
        classBuilder.addProperty(pageUrl)
        classBuilder.addProperty(asStarter)
        classBuilder.addProperty(destinationId)
        classBuilder.addProperty(destinationType)
        return classBuilder.build()
    }

    /**
     * 创建getDestinations()
     */
    private fun createGetDestinationsFuncation(jsonFileName: String = NavJsonHelp.OUTPUT_FILE_NAME): FunSpec {
        val fragmentActivityClass = ClassName("android.content", "Context")
        val destinationData = ClassName(packageName, "DestinationData")
        val hashMap = MAP.parameterizedBy(listOf(STRING, destinationData))
        val gson = Gson::class.asTypeName()
        val typeToken = TypeToken::class.asTypeName()
        return FunSpec.builder("getDestinations")
            .addModifiers(KModifier.PRIVATE)
            .receiver(fragmentActivityClass)
            .addStatement("val jsonText = this.assets.open(\"$jsonFileName\").use{ it.bufferedReader().readText() }")
            .addStatement(
                "return %T().fromJson(jsonText, object : %T<%T>() {}.type)",
                gson,
                typeToken,
                hashMap
            )
            .returns(hashMap.copy(true))
            .build()
    }

    /**
     * 创建buildNavGraph()
     */
    private fun createBuildNavGraphFuncation(): FunSpec {
        val pageType = PageType::class.asTypeName()
        val keepStateFragmentNavigator = ClassName(packageName, "KeepStateFragmentNavigator")
        val plusAssign = MemberName("androidx.navigation", KOperator.PLUS_ASSIGN)
        val componentName = ClassName("android.content", "ComponentName")
        val fragmentActivity = ClassName("androidx.fragment.app", "FragmentActivity")
        val findNavControlle = ClassName("androidx.navigation.fragment", "findNavController")
        val navGraphNavigator = ClassName("androidx.navigation", "NavGraphNavigator")
        val navGraph = ClassName("androidx.navigation", "NavGraph")
        val activityNavigator = ClassName("androidx.navigation", "ActivityNavigator")
        val fragmentNavigator = ClassName("androidx.navigation.fragment", "FragmentNavigator")
        val dialogFragmentNavigator =
            ClassName("androidx.navigation.fragment", "DialogFragmentNavigator")
        return FunSpec.builder("buildNavGraph")
            .receiver(fragmentActivity)
            .addParameter("containerId", INT)
            .addStatement("val destinations = this.getDestinations()")
            .addStatement("if (destinations.isNullOrEmpty()) return")
            .addStatement("val navHostController = supportFragmentManager.findFragmentById(containerId)")
            .addStatement("val navController = navHostController!!.%T()", findNavControlle)
            .addStatement("val navigatorProvider = navController.navigatorProvider")
            .addStatement(
                "val keepStateFragmentNavigator = %T(this, this.supportFragmentManager, containerId)",
                keepStateFragmentNavigator
            )
            .addStatement("navigatorProvider %M keepStateFragmentNavigator", plusAssign)
            .addStatement(
                "val graphNavigator = navigatorProvider.getNavigator(%T::class.java)",
                navGraphNavigator
            )
            .addStatement("val graph = %T(graphNavigator)", navGraph)
            .beginControlFlow("destinations.values.forEach {")
            .beginControlFlow("when (it.destinationType) {")
            .beginControlFlow("%T.PAGE_TYPE_ACTIVITY.type -> {", pageType)
            .addStatement(
                "val navigator = navigatorProvider.getNavigator(%T::class.java)",
                activityNavigator
            )
            .addStatement("val node = navigator.createDestination()")
            .addStatement("node.route = it.pageUrl")
            .addStatement(
                "node.setComponentName(%T(this.packageName, it.className))",
                componentName
            )
            .addStatement("node.addDeepLink(\"https://www.apes.com/\${it.pageUrl}\")")
            .addStatement("graph.addDestination(node)")
            .endControlFlow()
            .beginControlFlow("%T.PAGE_TYPE_FRAGMENT.type -> {", pageType)
            .addStatement(
                "//val navigator = navigatorProvider.getNavigator(%T::class.java)",
                fragmentNavigator
            )
            .addStatement(
                "val navigator = navigatorProvider.getNavigator(%T::class.java)",
                keepStateFragmentNavigator
            )
            .addStatement("val node = navigator.createDestination()")
            .addStatement("node.route = it.pageUrl")
            .addStatement("node.className = it.className")
            .addStatement("node.addDeepLink(\"https://www.apes.com/\${it.pageUrl}\")")
            .addStatement("graph.addDestination(node)")
            .endControlFlow()
            .beginControlFlow("%T.PAGE_TYPE_DIALOG.type -> {", pageType)
            .addStatement(
                "val navigator = navigatorProvider.getNavigator(%T::class.java)",
                dialogFragmentNavigator
            )
            .addStatement("val node = navigator.createDestination()")
            .addStatement("node.route = it.pageUrl")
            .addStatement("node.className = it.className")
            .addStatement("node.addDeepLink(\"https://www.apes.com/\${it.pageUrl}\")")
            .addStatement("graph.addDestination(node)")
            .endControlFlow()
            .endControlFlow()
            .addStatement("if (it.asStarter) graph.setStartDestination(it.pageUrl)")
            .endControlFlow()
            .addStatement("navController.setGraph(graph, null)")
            .returns(UNIT)
            .build()
    }
}