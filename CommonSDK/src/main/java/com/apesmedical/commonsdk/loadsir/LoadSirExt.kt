package com.apesmedical.commonsdk.loadsir

import android.app.Activity
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.apesmedical.commonsdk.base.newbase.exception.NoDataException
import com.apesmedical.commonsdk.http.Complete
import com.apesmedical.commonsdk.http.Failure
import com.apesmedical.commonsdk.http.Loading
import com.apesmedical.commonsdk.http.ResultData
import com.apesmedical.commonsdk.http.Success
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import java.net.ConnectException
import java.net.UnknownHostException

fun View.regLoadSir(reloadLamda: ((View) -> Unit)? = null) =
    LoadSir.getDefault().register(this) { reloadLamda?.invoke(it) }

fun Activity.regLoadSir(reloadLamda: ((View) -> Unit)? = null) =
    LoadSir.getDefault().register(this) { reloadLamda?.invoke(it) }

fun <T> LoadService<*>.show(resultData: ResultData<T>) {
    when (resultData) {
        is Loading -> showLoading()
        is Failure ->
            when (resultData.error) {
                is ConnectException -> showTimeout()
                is NoDataException -> showNoData()
                is UnknownHostException -> showNoNetwork()
                else -> showError()
            }
        is Success -> showSuccess()
        is Complete -> Unit
    }
}

fun LoadService<*>.show(states: CombinedLoadStates) {
    when (val state = states.refresh) {
        is LoadState.NotLoading -> showSuccess()
        LoadState.Loading -> showLoading()
        is LoadState.Error ->
            when (state.error) {
                is ConnectException -> showTimeout()
                is NoDataException -> showNoData()
                is UnknownHostException -> showNoNetwork()
                else -> showError()
            }
    }
}

fun <T> LiveData<ResultData<T>>.observeAndShow(
    owner: LifecycleOwner,
    loadService: LoadService<*>,
    failure: (message: String, throwable: Throwable) -> Unit = { _, _ -> },
    success: ((T?) -> Unit) = {},
) {
    observe(owner) {
        loadService.show(it)
        when (val result = it) {
            is Failure -> failure(result.message ?: "", result.error ?: Exception())
            is Success -> success(result.data)
        }
    }
}

fun LoadService<*>.showLoading() = showCallback(LoadingCallback::class.java)
fun LoadService<*>.showEmpty() = showCallback(EmptyCallback::class.java)
fun LoadService<*>.showNoData() = showCallback(NoDataCallback::class.java)
fun LoadService<*>.showNoNetwork() = showCallback(NoNetworkCallback::class.java)
fun LoadService<*>.showTimeout() = showCallback(TimeoutCallback::class.java)
fun LoadService<*>.showError() = showCallback(ErrorCallback::class.java)
fun LoadService<*>.showCustom() = showCallback(CustomCallback::class.java)


