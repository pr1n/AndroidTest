package com.pr1n.repository.repo

import androidx.paging.PagingData
import com.apesmedical.commonsdk.http.ResultData
import com.pr1n.repository.base.Repo
import com.pr1n.repository.entity.Doctor
import com.pr1n.repository.entity.DoctorList
import com.pr1n.repository.remote.base.Method
import kotlinx.coroutines.flow.Flow

interface MainRepository : Repo {
    fun getPagerData(): Flow<PagingData<DoctorList.Doctor>>

    fun getDoctorList(keyword: String): Flow<PagingData<Doctor>>

    fun getBanner(): Flow<ResultData<DoctorList>>

    fun getDataFlow(method: Method): Flow<ResultData<String>>

    fun getDataFlow1(method: Method): Flow<ResultData<Int>>

    fun test(vararg requestArgs: Pair<String, Any>)
}