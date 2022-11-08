package com.ishant.blockchainexpo.data.repository.database

import androidx.paging.PagingSource
import com.ishant.blockchainexpo.application.database.tables.Coin
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {


    suspend fun insert(coin: Coin)

    suspend fun deleteAndCreate (coins: List<Coin>)

    suspend fun insert(coins: List<Coin>)

     fun getAllCoins(quoteCoinCode:String): PagingSource<Int,Coin>

    suspend fun updateCoins(priceChange:String, priceChangePercentage: String, baseCoinCode: String)

    fun isCoinUpdated(baseCoinCode:String): Flow<List<Coin>>

    suspend fun deleteAllCoins()


}