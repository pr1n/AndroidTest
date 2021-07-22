package com.pr1n.androidtest.repo

import com.apesmedical.commonsdk.base.IRepository
import com.apesmedical.commonsdk.http.ResultData
import kotlinx.coroutines.flow.Flow

interface MainRepository : IRepository {
    suspend fun getData(vararg requestArgs: Pair<String, Any>, before: suspend () -> Unit): String

    fun getDataFlow(vararg requestArgs: Pair<String, Any>): Flow<ResultData<String>>

    fun getDataFlow1(vararg requestArgs: Pair<String, Any>): Flow<ResultData<Int>>

    fun test(vararg requestArgs: Pair<String, Any>)
}