package com.apesmedical.commonsdk.base

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

fun <VM : ViewModel> ComponentActivity.viewModel(): Lazy<VM> {
    val vmClass = getActualTypeArgument() as Class<VM>
    return this.viewModel(clazz = vmClass.kotlin)
}

fun <VM : ViewModel> ComponentActivity.stateViewModel(): Lazy<VM> {
    val vmClass = getActualTypeArgument() as Class<VM>
    return this.stateViewModel(clazz = vmClass.kotlin)
}

fun <VM : ViewModel> Fragment.viewModel(): Lazy<VM> {
    val vmClass = getActualTypeArgument() as Class<VM>
    return this.stateViewModel(clazz = vmClass.kotlin)
}

fun <VM : ViewModel> Fragment.stateViewModel(): Lazy<VM> {
    val vmClass = getActualTypeArgument() as Class<VM>
    return this.stateViewModel(clazz = vmClass.kotlin)
}

fun <VM : ViewModel> Fragment.sharedViewModel(): Lazy<VM> {
    val vmClass = getActualTypeArgument(true) as Class<VM>
    return this.sharedViewModel(clazz = vmClass.kotlin)
}

fun <VM : ViewModel> Fragment.sharedStateViewModel(): Lazy<VM> {
    val vmClass = getActualTypeArgument(true) as Class<VM>
    return this.sharedStateViewModel(clazz = vmClass.kotlin)
}

private fun <T> T.getActualTypeArgument(isAvm: Boolean = false): Class<*> where T : ViewModelStoreOwner, T : SavedStateRegistryOwner {
    val type = this.javaClass.genericSuperclass ?: throw Exception("genericSuperclass not fount.")
    return if (type is ParameterizedType)
        type.actualTypeArguments.let { array ->
            if (isAvm) array.lastOrNull { isEqualType(it) }
            else array.firstOrNull { isEqualType(it) }
        } as? Class<*> ?: throw Exception("Not find actualTypeArguments is ViewModel")
    else throw IllegalAccessException("${this.javaClass.name}.genericSuperclass not is ParameterizedType.")
}

private fun <T> T.getActualTypeArguments(): List<Class<*>> where T : ViewModelStoreOwner, T : SavedStateRegistryOwner {
    val type = this.javaClass.genericSuperclass ?: throw Exception("genericSuperclass not fount.")
    return if (type is ParameterizedType) type.actualTypeArguments.filter { isEqualType(it) }
        .map { it as Class<*> }
    else throw IllegalAccessException("${this.javaClass.name}.genericSuperclass not is ParameterizedType.")
}

private tailrec fun isEqualType(
    type: Type,
    contrastClass: Class<*> = ViewModel::class.java
): Boolean = if (type === contrastClass) true
else {
    val sup = (type as Class<*>).superclass
    if (sup == null) false
    else isEqualType(sup)
}

fun <VM1 : ViewModel, VM2 : ViewModel> ComponentActivity.stateViewModelPair(): Pair<Lazy<VM1>, Lazy<VM2>> {
    val (vmClass1, vmClass2) = getActualTypeArguments().also {
        if (it.size != 2) throw Exception("Generic (VM1, VM2) didNotMatch.")
    }
    return Pair(
        this.stateViewModel(clazz = (vmClass1 as Class<VM1>).kotlin),
        this.stateViewModel(clazz = (vmClass2 as Class<VM2>).kotlin),
    )
}

fun <VM1 : ViewModel, VM2 : ViewModel, VM3 : ViewModel> ComponentActivity.stateViewModelTriple(): Triple<Lazy<VM1>, Lazy<VM2>, Lazy<VM3>> {
    val (vmClass1, vmClass2, vmClass3) = getActualTypeArguments().also {
        if (it.size != 3) throw Exception("Generic (VM1, VM2, VM3) didNotMatch.")
    }
    return Triple(
        this.stateViewModel(clazz = (vmClass1 as Class<VM1>).kotlin),
        this.stateViewModel(clazz = (vmClass2 as Class<VM2>).kotlin),
        this.stateViewModel(clazz = (vmClass3 as Class<VM3>).kotlin)
    )
}