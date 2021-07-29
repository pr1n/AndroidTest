package com.pr1n.androidtest.repo.impl

import androidx.paging.ExperimentalPagingApi
import com.apesmedical.commonsdk.base.newbase.Get
import com.apesmedical.commonsdk.base.newbase.LocalService
import com.apesmedical.commonsdk.base.newbase.Method
import com.apesmedical.commonsdk.base.newbase.Post
import com.apesmedical.commonsdk.base.newbase.RemoteService
import com.apesmedical.commonsdk.base.newbase.ext.request
import com.apesmedical.commonsdk.base.newbase.ext.requestByData
import com.apesmedical.commonsdk.base.newbase.getPager
import com.apesmedical.commonsdk.db.Empty
import com.apesmedical.commonsdk.http.Success
import com.google.gson.Gson
import com.library.sdk.ext.logi
import com.pr1n.androidtest.DoctorList
import com.pr1n.androidtest.PAGER_URL
import com.pr1n.androidtest.POST_URL
import com.pr1n.androidtest.repo.MainRepository

class MainRepositoryImpl(override val remote: RemoteService, override val local: LocalService) :
    MainRepository {
    override fun getData(value: String) =
        remote.request<String>(Get {
            setUrl(POST_URL)
            config { connectTimeout(1000) }
        })

    @ExperimentalPagingApi
    override fun getPagerData() =
        getPager {
            val result = remote.requestByData<DoctorList>(Post {
                setUrl(PAGER_URL)
                addParam("page", it)
            })
            logi("result -> ${Gson().toJson(result.data)}")
            if (result is Success) result.data?.doctor ?: emptyList()
            else throw Exception("")
        }

    override fun getDataFlow(method: Method) =
        remote.request<String>(Post {
            setUrl(PAGER_URL)
        })

    override fun getDataFlow1(method: Method) =
        remote.request<Int>(Post {
            setUrl(PAGER_URL)
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


