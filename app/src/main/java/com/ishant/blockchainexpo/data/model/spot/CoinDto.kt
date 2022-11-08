package com.ishant.blockchainexpo.data.model.spot

import com.ishant.blockchainexpo.application.database.tables.Coin

data class CoinDto(
    val `data`: Data,
    val message: String,
    val status: Int
)
data class Data(
    val docs: List<Doc>
)
data class Doc(
    val __order: Any,
    val _id: String,
    val all_coin_price: AllCoinPrice,
    val base_coin_code: String,
    val base_coin_icon: String,
    val base_coin_name: String,
    val base_coin_precision: Int,
    val cs: Double,
    val high_price: Double,
    val last_price: Double,
    val low_price: Double,
    val market_cap: Double,
    val pair_name: String,
    val price_change: Double,
    val price_change_percent: Double,
    val quote_coin_code: String,
    val quote_coin_icon: String,
    val quote_coin_name: String,
    val quote_coin_precision: Int,
    val quote_volume: Double,
    val tick_size: Double,
    val volume: Double
)
data class AllCoinPrice(
    val usd: Usd
)
data class Usd(
    val coin_price: Double
)
public fun CoinDto.getCoins(): ArrayList<Coin> {
val arrayList=ArrayList<Coin>()
    this.data.docs.forEach {
        arrayList.add(Coin(it._id,
            it.all_coin_price.usd.coin_price,
            it.base_coin_code,
            it.base_coin_icon,
            it.base_coin_name,
            it.base_coin_precision,
            it.cs,
            it.high_price,
            it.last_price,
            it.low_price,
            it.market_cap,
            it.pair_name,
            it.price_change,
            it.price_change_percent,
            it.quote_coin_code,
            it.quote_coin_icon,
            it.quote_coin_name,
            it.quote_coin_precision,
            it.quote_volume,
            it.tick_size,
            it.volume
            ))
    }
    return arrayList
}
