package com.apes.compiler.nav.help

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import java.io.Closeable
import java.util.ArrayDeque
import javax.annotation.processing.Filer

class BuildNavKeepStateFragmentHelp(private val packageName: String = "com.apes.nav") {

    /**
     * CreateKotlinFile
     */
    fun createKeepStateFragmentNavigatorKotlinFile(filer: Filer) {
        try {
            val kotlinFile = FileSpec.builder(packageName, "KeepStateFragmentNavigator")
                .addType(createClassType())
                .build()
            kotlinFile.writeTo(filer)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            (filer as Closeable).close()
        }
    }

    /**
     * KeepStateFragmentNavigator
     */
    private fun createClassType(): TypeSpec {
        val context = ClassName("android.content", "Context")
        val fragmentManager = ClassName("androidx.fragment.app", "FragmentManager")
        val navigator = ClassName("androidx.navigation", "Navigator")
        val destination = ClassName("androidx.navigation.fragment.FragmentNavigator", "Destination")
        val navigatorType = navigator.parameterizedBy(destination)
        val navigatorName = ClassName("androidx.navigation.Navigator", "Name")
        val navigatorNameAnno =
            AnnotationSpec.builder(navigatorName).addMember("\"keepStateFragmentNavigator\"")
                .build()
        val constrctorFun = FunSpec.constructorBuilder()
            .addParameter("mContext", context, KModifier.PRIVATE)
            .addParameter("mFragmentManager", fragmentManager, KModifier.PRIVATE)
            .addParameter("mContainerId", INT, KModifier.PRIVATE)
            .build()
        val mContext =
            PropertySpec.builder("mContext", context, KModifier.PRIVATE).initializer("mContext")
                .build()
        val mFragmentManager =
            PropertySpec.builder("mFragmentManager", fragmentManager, KModifier.PRIVATE)
                .initializer("mFragmentManager").build()
        val mContainerId =
            PropertySpec.builder("mContainerId", INT, KModifier.PRIVATE).initializer("mContainerId")
                .build()
        val companionObject = TypeSpec.companionObjectBuilder()
            .addProperty(
                PropertySpec.builder("TAG", STRING, KModifier.PRIVATE, KModifier.CONST)
                    .initializer("\"DeepStateFragmentNavigator\"").build()
            )
            .addProperty(
                PropertySpec.builder(
                    "KEY_BACK_STACK_IDS",
                    STRING,
                    KModifier.PRIVATE,
                    KModifier.CONST
                )
                    .initializer("\"androidx-nav-fragment:navigator:backStackIds\"").build()
            )
            .build()
        val arrayDeque = ArrayDeque::class.asClassName().parameterizedBy(INT)
        val mBackStack = PropertySpec.builder("mBackStack", arrayDeque, KModifier.PRIVATE)
            .initializer("%T()", arrayDeque).build()
        return TypeSpec.classBuilder("KeepStateFragmentNavigator")
            .addAnnotation(navigatorNameAnno)
            .primaryConstructor(constrctorFun)
            .addProperty(mContext)
            .addProperty(mFragmentManager)
            .addProperty(mContainerId)
            .superclass(navigatorType)
            .addType(companionObject)
            .addProperty(mBackStack)
            .addFunction(createPopBackStackFunction())
            .addFunction(createCreateDestinationFunction())
            .addFunction(createNavigateFunction())
            .addFunction(createonSaveStateFunction())
            .addFunction(createOnRestoreStateFunction())
            .addFunction(createGenerateBackStackNameFunction())
            .build()
    }

    /**
     * popBackStack
     */
    private fun createPopBackStackFunction(): FunSpec {
        val log = ClassName("android.util", "Log")
        val fragmentManager = ClassName("androidx.fragment.app", "FragmentManager")
        return FunSpec.builder("popBackStack")
            .addModifiers(KModifier.OVERRIDE)
            .addStatement("if (mBackStack.isEmpty()) return false")
            .beginControlFlow("if (mFragmentManager.isStateSaved) {")
            .addStatement(
                "%T.i(TAG, \"Ignoring popBackStack() call: FragmentManager has already saved its state\")",
                log
            )
            .addStatement("return false")
            .endControlFlow()
            .addStatement(
                "mFragmentManager.popBackStack(generateBackStackName(mBackStack.size, mBackStack.peekLast()), %T.POP_BACK_STACK_INCLUSIVE)",
                fragmentManager
            )
            .addStatement("mBackStack.removeLast()")
            .addStatement("return true")
            .returns(BOOLEAN)
            .build()
    }

    /**
     * createDestination
     */
    private fun createCreateDestinationFunction(): FunSpec {
        val destination = ClassName("androidx.navigation.fragment.FragmentNavigator", "Destination")
        return FunSpec.builder("createDestination")
            .addModifiers(KModifier.OVERRIDE)
            .addStatement("return %T(this)", destination)
            .returns(destination)
            .build()
    }

    /**
     * navigate
     */
    private fun createNavigateFunction(): FunSpec {
        val log = ClassName("android.util", "Log")
        val navDestination = ClassName("androidx.navigation", "NavDestination")
        val destination = ClassName("androidx.navigation.fragment.FragmentNavigator", "Destination")
        val bundle = ClassName("android.os", "Bundle")
        val navOptions = ClassName("androidx.navigation", "NavOptions")
        val fragmentNavigator = ClassName("androidx.navigation.fragment", "FragmentNavigator")
        val extras = ClassName("androidx.navigation.fragment.FragmentNavigator", "Extras")
        val idRes = ClassName("androidx.annotation", "IdRes")
        return FunSpec.builder("navigate")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("destination", destination)
            .addParameter("args", bundle.copy(true))
            .addParameter("navOptions", navOptions.copy(true))
            .addParameter("navigatorExtras", extras.copy(true))
            .beginControlFlow("if (mFragmentManager.isStateSaved) {")
            .addStatement(
                "%T.i(TAG, \"Ignoring navigate() call: FragmentManager has already saved its state\")",
                log
            )
            .addStatement("return null")
            .endControlFlow()
            .addStatement("")
            .addStatement("var className = destination.className")
            .beginControlFlow("if (className[0] == '.') {")
            .addStatement("className = mContext.packageName + className")
            .endControlFlow()
            .addStatement("")
            .addStatement("var frag = mFragmentManager.findFragmentByTag(className)")
            .addStatement("if (frag == null) frag = mFragmentManager.fragmentFactory.instantiate(mContext.classLoader, className)")
            .addStatement("frag.arguments = args")
            .addStatement("val ft = mFragmentManager.beginTransaction()")
            .addStatement("var enterAnim = navOptions?.enterAnim ?: -1")
            .addStatement("var exitAnim = navOptions?.exitAnim ?: -1")
            .addStatement("var popEnterAnim = navOptions?.popEnterAnim ?: -1")
            .addStatement("var popExitAnim = navOptions?.popExitAnim ?: -1")
            .beginControlFlow("if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {")
            .addStatement("enterAnim = if (enterAnim != -1) enterAnim else 0")
            .addStatement("exitAnim = if (exitAnim != -1) exitAnim else 0")
            .addStatement("popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0")
            .addStatement("popExitAnim = if (popExitAnim != -1) popExitAnim else 0")
            .addStatement("ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)")
            .endControlFlow()
            .addStatement("")
            .addStatement("mFragmentManager.fragments.forEach(ft::hide)")
            .addStatement("")
            .addStatement("if (!frag.isAdded) ft.add(mContainerId, frag, className)")
            .addStatement("")
            .addStatement("ft.show(frag)")
            .addStatement("")
            .addStatement("//ft.replace(mContainerId, frag)")
            .addStatement("")
            .addStatement("ft.setPrimaryNavigationFragment(frag)")
            .addStatement("@%T val destId = destination.id", idRes)
            .addStatement("val initialNavigation = mBackStack.isEmpty()")
            .addStatement("// TODO Build first class singleTop behavior for fragments")
            .addStatement("val isSingleTopReplacement = (navOptions != null && !initialNavigation && navOptions.shouldLaunchSingleTop() && mBackStack.peekLast() == destId)")
            .beginControlFlow("val isAdded: Boolean = when {")
            .addStatement("initialNavigation -> true")
            .beginControlFlow("isSingleTopReplacement -> {")
            .addStatement("// Single Top means we only want one instance on the back stack")
            .beginControlFlow("if (mBackStack.size > 1) {")
            .addStatement("// If the Fragment to be replaced is on the FragmentManager's")
            .addStatement("// back stack, a simple replace() isn't enough so we")
            .addStatement("// remove it from the back stack and put our replacement")
            .addStatement("// on the back stack in its place")
            .addStatement("mFragmentManager.popBackStack(")
            .addStatement("    generateBackStackName(mBackStack.size, mBackStack.peekLast()),")
            .addStatement("    FragmentManager.POP_BACK_STACK_INCLUSIVE")
            .addStatement(")")
            .addStatement("ft.addToBackStack(generateBackStackName(mBackStack.size, destId))")
            .endControlFlow()
            .addStatement("false")
            .endControlFlow()
            .beginControlFlow("else -> {")
            .addStatement("ft.addToBackStack(generateBackStackName(mBackStack.size + 1, destId))")
            .addStatement("true")
            .endControlFlow()
            .endControlFlow()
            .beginControlFlow("if (navigatorExtras is %T.%T) {", fragmentNavigator, extras)
            .beginControlFlow("for ((key, value) in navigatorExtras.sharedElements) {")
            .addStatement("ft.addSharedElement(key!!, value!!)")
            .endControlFlow()
            .endControlFlow()
            .addStatement("ft.setReorderingAllowed(true)")
            .addStatement("ft.commit()")
            .addStatement("// The commit succeeded, update our view of the world")
            .beginControlFlow("return if (isAdded) {")
            .addStatement("mBackStack.add(destId)")
            .addStatement("destination")
            .endControlFlow()
            .addStatement("else null")
            .returns(navDestination.copy(true))
            .build()
    }

    /**
     * onSaveState
     */
    private fun createonSaveStateFunction(): FunSpec {
        val bundle = ClassName("android.os", "Bundle")
        return FunSpec.builder("onSaveState")
            .addModifiers(KModifier.OVERRIDE)
            .addStatement("val b = %T()", bundle)
            .addStatement("val backStack = IntArray(mBackStack.size)")
            .addStatement("var index = 0")
            .beginControlFlow("for (id in mBackStack) {")
            .addStatement("backStack[index++] = id")
            .endControlFlow()
            .addStatement("b.putIntArray(KEY_BACK_STACK_IDS, backStack)")
            .addStatement("return b")
            .returns(bundle)
            .build()
    }

    /**
     * onRestoreState
     */
    private fun createOnRestoreStateFunction(): FunSpec {
        val bundle = ClassName("android.os", "Bundle")
        return FunSpec.builder("onRestoreState")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("savedState", bundle)
            .addStatement("val backStack = savedState.getIntArray(KEY_BACK_STACK_IDS)")
            .beginControlFlow("if (backStack != null) {")
            .addStatement("mBackStack.clear()")
            .beginControlFlow("for (destId in backStack) {")
            .addStatement("mBackStack.add(destId)")
            .endControlFlow()
            .endControlFlow()
            .build()
    }

    /**
     * generateBackStackName
     */
    private fun createGenerateBackStackNameFunction(): FunSpec {
        return FunSpec.builder("generateBackStackName")
            .addModifiers(KModifier.PRIVATE)
            .addParameter("backStackIndex", INT)
            .addParameter("destId", INT)
            .addStatement("return \"\$backStackIndex-\$destId\"")
            .returns(STRING)
            .build()
    }
}