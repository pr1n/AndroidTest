package com.apesmedical.commonsdk.base.newbase

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.library.sdk.ext.logi


class NoDataException(message: String = "") : Exception(message)

const val DEFAULT_PAGE_INDEX = 1
const val DEFAULT_PAGE_SIZE = 20
const val DEFAULT_INITIAL_PAGE_SIZE = 30

class RemotePagingSource<T : Any>(private inline val block: suspend (pageIndex: Int, pageSize: Int) -> List<T>) :
    PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>) = DEFAULT_PAGE_INDEX

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            // 分页初始页码
            val pageIndex = params.key ?: DEFAULT_PAGE_INDEX
            // 获取数据
            val result = block(pageIndex, params.loadSize)
            if (result.isNullOrEmpty()) throw NoDataException()
            LoadResult.Page(
                data = result,
                prevKey = if (DEFAULT_PAGE_INDEX == pageIndex) null else pageIndex - 1,
                nextKey = if (result.isEmpty()) null else pageIndex + 1
            )
        } catch (e: Exception) {
            logi("Exception -> $e")
            LoadResult.Error(throwable = e)
        }
    }
}

@ExperimentalPagingApi
inline fun <reified T : Any> IRepository.getPager(crossinline pagingLamda: suspend (page: Int) -> List<T>) =
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
                pagingLamda(pageIndex)
            }
        }
    ).flow