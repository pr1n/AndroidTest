package com.pr1n.androidtest.repo.impl

import com.apesmedical.commonsdk.base.newbase.IParamBuilder
import com.apesmedical.commonsdk.base.newbase.ext.post
import com.apesmedical.commonsdk.db.Empty
import com.library.sdk.ext.logi
import com.pr1n.androidtest.POST_URL
import com.pr1n.androidtest.repo.MainRepository

class MainRepositoryImpl : MainRepository {
    override fun getData(request: IParamBuilder.() -> Unit) =
        remote.post<String> {
            POST_URL
            request(this)
        }

    override fun getDataFlow(request: IParamBuilder.() -> Unit) =
        remote.post<String> {
            setUrl("http://test-yys-api.xadazhihui.cn/consult/list")
            request(this)
        }

    override fun getDataFlow1(request: IParamBuilder.() -> Unit) =
        remote.post<Int> {
            setUrl("http://test-yys-api.xadazhihui.cn/consult/list")
            request(this)
        }

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


