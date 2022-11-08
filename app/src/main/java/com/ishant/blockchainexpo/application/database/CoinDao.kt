package com.ishant.blockchainexpo.application.database


import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.ishant.blockchainexpo.application.database.tables.Coin
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {
    @Transaction
    suspend fun deleteAndCreate (coins: List<Coin>) {
        deleteAllCoins()
        insert(coins)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(coin: Coin): Long

    @Insert
    suspend fun insert(coins: List<Coin>)

    @Query("select * from Coin where quote_coin_code=:usd")
    fun getAllCoins(usd:String): PagingSource<Int,Coin>

    @Query("UPDATE Coin SET price_in_dollar=:priceChange , price_change_percent=:priceChangePercentage WHERE pair_name = :baseCoinCode")
    suspend fun updateCoins(priceChange:String, priceChangePercentage: String, baseCoinCode: String)

    @Query("select * from Coin WHERE quote_coin_code = :quoteCoinCode ")
    fun isCoinUpdated(quoteCoinCode:String): Flow<List<Coin>>

    @Query("DELETE  FROM Coin")
    suspend fun deleteAllCoins()
}