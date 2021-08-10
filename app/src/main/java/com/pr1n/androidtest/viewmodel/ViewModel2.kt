package com.pr1n.androidtest.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.apesmedical.commonsdk.base.newbase.BaseSavedStateViewModel
import com.pr1n.repository.remote.base.Post
import com.pr1n.repository.repo.MainRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine

class ViewModel2(
    override val savedStateHandle: SavedStateHandle,
    override val repo: MainRepository
) : BaseSavedStateViewModel<MainRepository>() {
    val resultData = repo.getDataFlow(Post { addParam("page", 1) })
        .asLiveData()

    val tripleResultData = liveData {
        val flow1 = repo.getDataFlow(Post { addParam("page", 1) })
        val flow2 = repo.getDataFlow1(Post { addParam("page", 1) })
        val flow3 = repo.getDataFlow(Post { addParam("page", 1) })
        combine(flow1, flow2, flow3) { a, b, c ->
            Triple(a, b, c)
        }.collectLatest(::emit)
//        val flows = listOf(flow1, flow2, flow3)
//        combine(flows) { it }.collectLatest(::emit)
    }
}