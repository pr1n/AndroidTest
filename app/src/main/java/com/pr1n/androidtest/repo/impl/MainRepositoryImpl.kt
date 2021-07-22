package com.pr1n.androidtest.repo.impl

import com.apesmedical.commonsdk.base.postToAwait
import com.apesmedical.commonsdk.base.postToFlow
import com.apesmedical.commonsdk.db.Empty
import com.library.sdk.ext.logi
import com.pr1n.androidtest.POST_URL
import com.pr1n.androidtest.repo.MainRepository

class MainRepositoryImpl : MainRepository {
    override suspend fun getData(
        vararg requestArgs: Pair<String, Any>,
        before: suspend () -> Unit
    ) =
        remote.postToAwait<String>(POST_URL, *requestArgs) { it.also { before() } }

    override fun getDataFlow(vararg requestArgs: Pair<String, Any>) =
        remote.postToFlow<String>("http://test-yys-api.xadazhihui.cn/consult/list", *requestArgs)

    override fun getDataFlow1(vararg requestArgs: Pair<String, Any>) =
        remote.postToFlow<Int>("http://test-yys-api.xadazhihui.cn/consult/list", *requestArgs)

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


