package com.pr1n.androidtest.repo

import com.apesmedical.commonsdk.base.newbase.IParamBuilder
import com.apesmedical.commonsdk.base.newbase.IRepository
import com.apesmedical.commonsdk.http.ResultData
import kotlinx.coroutines.flow.Flow

interface MainRepository : IRepository {
    fun getData(request: IParamBuilder.() -> Unit): Flow<ResultData<String>>

    fun getDataFlow(request: IParamBuilder.() -> Unit): Flow<ResultData<String>>

    fun getDataFlow1(request: IParamBuilder.() -> Unit): Flow<ResultData<Int>>

    fun test(vararg requestArgs: Pair<String, Any>)
}