package com.ishant.blockchainexpo.application.websocket.model

import com.google.gson.annotations.SerializedName

data class Binance(

	@SerializedName("Binance")
	val binance:ArrayList<BinanceItem> = ArrayList<BinanceItem>()
)

data class BinanceItem(

	@SerializedName("a")
	val bestAskPrice: String? = null,

	@SerializedName("A")
	val bestAskQuantity: String? = null,

	@SerializedName("b")
	val bestBidPrice: String? = null,

	@SerializedName("B")
	val bestBidQuantity: String? = null,

	@SerializedName("c")
	val lastPrice: String? = null,

	@SerializedName("C")
	val statisticsCloseTime : Long? = null,

	@SerializedName("e")
	val eventType: String? = null,

	@SerializedName("E")
	val eventTime: Long? = null,

	@SerializedName("F")
	val firstTradeId: Int? = null,

	@SerializedName("h")
	val highPrice: String? = null,

	@SerializedName("l")
	val lowPrice: String? = null,

	@SerializedName("L")
	val lastTradeId: Int? = null,

	@SerializedName("n")
	val totalNumbersOfTrades: Int? = null,

	@SerializedName("o")
	val openPrice: String? = null,

	@SerializedName("O")
	val statisticsOpenTime: Long? = null,

	@SerializedName("p")
	val priceChange: String? = null,

	@SerializedName("P")
	val priceChangePercent: String? = null,

	@SerializedName("Q")
	val lastQuantity: String? = null,

	@SerializedName("q")
	val totalTradeQuoteAssetsVolume: String? = null,

	@SerializedName("s")
	val symbol: String? = null,

	@SerializedName("v")
	val totalTradeBaseAssetsVolume: String? = null,

	@SerializedName("w")
	val  weightedAveragePrice: String? = null,

	@SerializedName("x")
	val firstTradePrice: String? = null
)
