package com.pr1n.androidtest.repo.impl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.apesmedical.commonsdk.base.newbase.Get
import com.apesmedical.commonsdk.base.newbase.LocalService
import com.apesmedical.commonsdk.base.newbase.Method
import com.apesmedical.commonsdk.base.newbase.Post
import com.apesmedical.commonsdk.base.newbase.RemoteService
import com.apesmedical.commonsdk.base.newbase.ext.request
import com.apesmedical.commonsdk.base.newbase.ext.requestByData
import com.apesmedical.commonsdk.base.newbase.getPager
import com.apesmedical.commonsdk.base.newbase.toPagerData
import com.apesmedical.commonsdk.db.Empty
import com.apesmedical.commonsdk.http.Success
import com.library.sdk.ext.logi
import com.pr1n.androidtest.CONSULT_URL
import com.pr1n.androidtest.DOCTOR_SEARCH_URL
import com.pr1n.androidtest.Doctors
import com.pr1n.androidtest.Doctor
import com.pr1n.androidtest.DoctorList
import com.pr1n.androidtest.POST_URL
import com.pr1n.androidtest.repo.MainRepository
import kotlinx.coroutines.flow.Flow

class MainRepositoryImpl(override val remote: RemoteService, override val local: LocalService) :
    MainRepository {
    @ExperimentalPagingApi
    override fun getPagerData(): Flow<PagingData<DoctorList.Doctor>> =
        getPager { pageIndex, pageSize ->
            val result = remote.requestByData<DoctorList>(Post {
                setUrl(CONSULT_URL)
                addParam("page", pageIndex)
            })
            if (result is Success) result.data?.doctor ?: emptyList()
            else throw Exception("")
        }

    @ExperimentalPagingApi
    override fun getDoctorList(keyword: String): Flow<PagingData<Doctor>> =
        getPager { pageIndex, _ ->
            remote.requestByData<Doctors>(Post {
                setUrl(DOCTOR_SEARCH_URL)
                addParam("keyword", keyword)
                addParam("page", pageIndex)
            }).toPagerData()
        }

    override fun getBanner() =
        remote.request<DoctorList>(Post {
            setUrl(CONSULT_URL)
            addParam("page", 1)
        })

    override fun getDataFlow(method: Method) =
        remote.request<String>(Post {
            setUrl(CONSULT_URL)
        })

    override fun getDataFlow1(method: Method) =
        remote.request<Int>(Post {
            setUrl(CONSULT_URL)
        })

    override fun test(vararg requestArgs: Pair<String, Any>) {
        val empty = Empty(100L, "Pr1n", 4)
        local.emptyDao.testInsert(empty)
        logi("Query -> ${local.emptyDao.testQuery(empty.id)}")
        val copyEmpty = empty.copy(100L, "UpdatePr1n", 0)
        local.emptyDao.testUpdate(copyEmpty)
        logi("Query -> ${local.emptyDao.testQuery(copyEmpty.id)}")
        local.emptyDao.testDelete(copyEmpty)
        logi("Query -> ${local.emptyDao.testQuery(copyEmpty.id)}")
    }
}


