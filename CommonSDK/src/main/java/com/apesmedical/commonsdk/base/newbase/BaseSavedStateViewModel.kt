package com.apesmedical.commonsdk.base.newbase

import androidx.lifecycle.SavedStateHandle
import com.apesmedical.commonsdk.base.BaseViewModel

/**
 * Created by Beetle_Sxy on 2020/10/9.
 * 协程处理网络请求回调
 */
abstract class BaseSavedStateViewModel<out Repo : IRepo> : BaseViewModel() {
    protected abstract val savedStateHandle: SavedStateHandle
    protected abstract val repo: Repo
}
