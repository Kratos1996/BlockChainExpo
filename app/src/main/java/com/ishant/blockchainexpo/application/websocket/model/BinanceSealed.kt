package com.ishant.blockchainexpo.application.websocket.model

sealed class BinanceSealed {

    class Success(val response: Binance) : BinanceSealed()
    class Error(val message: String?) : BinanceSealed()
    object Empty : BinanceSealed()

}