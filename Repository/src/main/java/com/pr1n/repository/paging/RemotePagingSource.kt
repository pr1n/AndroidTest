package com.pr1n.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.library.sdk.ext.logi
import com.apesmedical.commonsdk.base.newbase.exception.NoDataException
import com.pr1n.repository.paging.config.PagingConfig

class RemotePagingSource<T : Any>(private inline val block: suspend (pageIndex: Int, pageSize: Int) -> List<T>) :
    PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>) = PagingConfig.DEFAULT_PAGE_INDEX

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            // 分页初始页码
            val pageIndex = params.key ?: PagingConfig.DEFAULT_PAGE_INDEX
            // 获取数据
            val result = block(pageIndex, params.loadSize)
            // 没有取到数据为加载完成
            if (result.isNullOrEmpty()) throw NoDataException()
            LoadResult.Page(
                data = result,
                prevKey = if (PagingConfig.DEFAULT_PAGE_INDEX == pageIndex) null else pageIndex - 1,
                nextKey = if (result.isEmpty()) null else pageIndex + 1
            )
        } catch (e: Exception) {
            logi("Exception -> $e")
            e.printStackTrace()
            LoadResult.Error(throwable = e)
        }
    }
}