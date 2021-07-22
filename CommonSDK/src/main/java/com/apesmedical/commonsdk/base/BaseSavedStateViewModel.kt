package com.apesmedical.commonsdk.base

import androidx.lifecycle.SavedStateHandle

/**
 * Created by Beetle_Sxy on 2020/10/9.
 * 协程处理网络请求回调
 */
abstract class BaseSavedStateViewModel<out Repo : IRepository> : BaseViewModel() {
    protected abstract val savedStateHandle: SavedStateHandle
    protected abstract val repo: Repo
}
