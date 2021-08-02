package com.pr1n.androidtest.repo

import androidx.paging.PagingData
import com.apesmedical.commonsdk.base.newbase.IRepository
import com.apesmedical.commonsdk.base.newbase.Method
import com.apesmedical.commonsdk.http.ResultData
import com.pr1n.androidtest.Doctor
import com.pr1n.androidtest.DoctorList
import kotlinx.coroutines.flow.Flow

interface MainRepository : IRepository {
    fun getPagerData(): Flow<PagingData<DoctorList.Doctor>>

    fun getDoctorList(keyword: String): Flow<PagingData<Doctor>>

    fun getBanner(): Flow<ResultData<DoctorList>>

    fun getDataFlow(method: Method): Flow<ResultData<String>>

    fun getDataFlow1(method: Method): Flow<ResultData<Int>>

    fun test(vararg requestArgs: Pair<String, Any>)
}