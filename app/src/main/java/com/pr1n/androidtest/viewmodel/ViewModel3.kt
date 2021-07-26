package com.pr1n.androidtest.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.apesmedical.commonsdk.base.newbase.BaseSavedStateViewModel
import com.pr1n.androidtest.repo.MainRepository

class ViewModel3(
    override val savedStateHandle: SavedStateHandle,
    override val repo: MainRepository
) : BaseSavedStateViewModel<MainRepository>() {

}