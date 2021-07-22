package com.pr1n.androidtest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.apesmedical.commonsdk.base.BaseSavedStateViewModel
import com.library.sdk.ext.logi
import com.pr1n.androidtest.repo.MainRepository
import kotlinx.coroutines.delay

class ViewModel1(
    override val savedStateHandle: SavedStateHandle,
    override val repo: MainRepository,
) : BaseSavedStateViewModel<MainRepository>() {

    private val _resultLiveData = MutableLiveData<String>()
    val resultLiveData: LiveData<String> get() = _resultLiveData

    fun getData() {
        launchUI {
            logi("CurrentThreadName -> ${Thread.currentThread().name}")
            repo.getData("key" to "value") {
                chageResult("loading...")
                delay(2000)
                repo.test()
            }.let(::chageResult)
        }
    }

    fun chageResult(value: String) {
        // 生产前处理
        _resultLiveData.postValue(value)
    }
}