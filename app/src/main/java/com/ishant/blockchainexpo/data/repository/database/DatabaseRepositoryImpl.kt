package com.ishant.blockchainexpo.data.repository.database

import androidx.paging.PagingSource
import com.ishant.blockchainexpo.application.database.AppDB
import com.ishant.blockchainexpo.application.database.tables.Coin
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DatabaseRepositoryImpl @Inject constructor(
    private val db: AppDB
) :
    DatabaseRepository {
    override suspend fun insert(coin: Coin) {
        db.getDao().insert(coin)
    }

    override suspend fun insert(coins: List<Coin>) {
        db.getDao().insert(coins)
    }

    override suspend fun deleteAndCreate(coins: List<Coin>) {
        db.getDao().deleteAndCreate(coins)
    }

    override  fun getAllCoins(quoteCoinCode:String): PagingSource<Int, Coin> {
      return db.getDao().getAllCoins(quoteCoinCode)
    }

    override suspend fun updateCoins(
        priceChange: String,
        priceChangePercentage: String,
        baseCoinCode: String
    ) {
       return db.getDao().updateCoins(priceChange,priceChangePercentage,baseCoinCode)
    }

    override fun isCoinUpdated(quoteCoinCode:String): Flow<List<Coin>> {
      return db.getDao().isCoinUpdated(quoteCoinCode)
    }

    override suspend fun deleteAllCoins() {
        db.getDao().deleteAllCoins()
    }


}