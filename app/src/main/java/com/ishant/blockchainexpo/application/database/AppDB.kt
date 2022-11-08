package com.ishant.blockchainexpo.application.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ishant.blockchainexpo.application.database.tables.Coin

@Database(entities = [Coin::class], version = 1, exportSchema = false)
abstract class AppDB : RoomDatabase() {
    abstract fun getDao(): CoinDao
}