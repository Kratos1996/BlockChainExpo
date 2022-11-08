package com.ishant.blockchainexpo.application.database.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Coin( @PrimaryKey val _id: String,
                 @ColumnInfo val price_in_dollar: Double,
                 @ColumnInfo val base_coin_code: String,
                 @ColumnInfo  val base_coin_icon: String,
                 @ColumnInfo  val base_coin_name: String,
                 @ColumnInfo  val base_coin_precision: Int,
                 @ColumnInfo val cs: Double,
                 @ColumnInfo val high_price: Double,
                 @ColumnInfo val last_price: Double,
                 @ColumnInfo  val low_price: Double,
                 @ColumnInfo val market_cap: Double,
                 @ColumnInfo val pair_name: String,
                 @ColumnInfo val price_change: Double,
                 @ColumnInfo val price_change_percent: Double,
                 @ColumnInfo val quote_coin_code: String,
                 @ColumnInfo val quote_coin_icon: String,
                 @ColumnInfo  val quote_coin_name: String,
                 @ColumnInfo val quote_coin_precision: Int,
                 @ColumnInfo val quote_volume: Double,
                 @ColumnInfo val tick_size: Double,
                 @ColumnInfo val volume: Double) {
}