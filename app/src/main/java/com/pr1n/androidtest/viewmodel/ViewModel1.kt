package com.pr1n.androidtest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.apesmedical.commonsdk.base.newbase.BaseSavedStateViewModel
import com.library.sdk.ext.logi
import com.pr1n.androidtest.repo.MainRepository
import kotlinx.coroutines.flow.onStart

class ViewModel1(
    override val savedStateHandle: SavedStateHandle,
    override val repo: MainRepository,
) : BaseSavedStateViewModel<MainRepository>() {

    private val _resultLiveData = MutableLiveData("")
    val resultLiveData: LiveData<String> get() = _resultLiveData

    fun getData() {
        launchUI {
            logi("CurrentThreadName -> ${Thread.currentThread().name}")
            repo.getData {
                addParams("key" to "value")
            }.onStart {
                chageResult("loading...")
                repo.test()
            }.asLiveData()
        }
    }

    fun chageResult(value: String) {
        // 生产前处理
        _resultLiveData.postValue(value)
    }
}