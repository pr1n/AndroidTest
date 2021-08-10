package com.pr1n.androidtest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.apesmedical.commonsdk.base.newbase.BaseSavedStateViewModel
import com.apesmedical.commonsdk.http.ResultData
import com.library.sdk.ext.logi
import com.pr1n.repository.entity.DoctorList
import com.pr1n.repository.repo.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart

class ViewModel1(
    override val savedStateHandle: SavedStateHandle,
    override val repo: MainRepository,
) : BaseSavedStateViewModel<MainRepository>() {

    private val _stateFlow = MutableStateFlow("")
    val stateFlow: StateFlow<String> get()= _stateFlow

    fun test(){
        _stateFlow.tryEmit("")
    }

    private val _resultLiveData = MediatorLiveData<ResultData<DoctorList>>()
    val resultLiveData: LiveData<ResultData<DoctorList>> get() = _resultLiveData

    fun getDataLiveData() {
        repo.getBanner().onStart {
            repo.test()
        }.toLiveData(_resultLiveData)
    }

    fun getPagerData() =
        repo.getPagerData().cachedIn(viewModelScope)

    fun getDoctorList(keyword: String = "医生") = repo.getDoctorList(keyword).cachedIn(viewModelScope)

    private val _getBannerLiveData = MediatorLiveData<ResultData<DoctorList>>()
    val getBannerLiveData: LiveData<ResultData<DoctorList>> get() = _getBannerLiveData

    fun getBanner() {
        repo.getBanner().toLiveData(_getBannerLiveData)
    }

    private fun <T> Flow<T>.toLiveData(mediatorLiveData: MediatorLiveData<T>) {
        mediatorLiveData.addSource(asLiveData()){
            logi("addSource -> $it")
            mediatorLiveData.value = it
        }
    }
}