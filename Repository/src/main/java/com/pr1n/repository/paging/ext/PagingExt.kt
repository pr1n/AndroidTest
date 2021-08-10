package com.pr1n.repository.paging.ext

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.apesmedical.commonsdk.http.Failure
import com.apesmedical.commonsdk.http.ResultData
import com.apesmedical.commonsdk.http.Success
import com.pr1n.repository.base.IRepository
import com.pr1n.repository.paging.DEFAULT_INITIAL_PAGE_SIZE
import com.pr1n.repository.paging.DEFAULT_PAGE_SIZE
import com.pr1n.repository.paging.RemotePagingSource


@ExperimentalPagingApi
inline fun <reified T : Any> IRepository.getPager(crossinline pagingLamda: suspend (pageIndex: Int, pageSize: Int) -> List<T>) =
    Pager(
        // 分页配置
        config = PagingConfig(
            // 分页大小
            pageSize = DEFAULT_PAGE_SIZE,
            // 启用占位符
            enablePlaceholders = true,
            // 距离最后一条的前多少条数据开始加载下一页
            prefetchDistance = 2,
            // 初始的分页大小
            initialLoadSize = DEFAULT_INITIAL_PAGE_SIZE
        ),
        pagingSourceFactory = {
            RemotePagingSource { pageIndex, pageSize ->
                pagingLamda(pageIndex, pageSize)
            }
        }
    ).flow

fun <T, R: List<T>> ResultData<R>.toPagerData() =
    when (this) {
        is Success -> data ?: throw Exception("")
        is Failure -> throw error!!
        else -> throw Exception("")
    }