package com.ishant.blockchainexpo.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ishant.blockchainexpo.application.database.CoinDao
import com.ishant.blockchainexpo.application.database.tables.Coin
import com.ishant.blockchainexpo.data.repository.database.DatabaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

class CoinDatasource (private val db: DatabaseRepository) /*: PagingSource<Int, Coin>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Coin> {
        val page = params.key ?: 0
        return try {
            val entities= db.getAllCoins()
            LoadResult.Page(data = entities, prevKey = if (page == 0) null else page - 1, nextKey = if (entities.isEmpty()) null else page + 1)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

      override fun getRefreshKey(state: PagingState<Int, Coin>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }


    }
}*/