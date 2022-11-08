package com.ishant.blockchainexpo.data.remote

import com.ishant.blockchainexpo.application.constance.AppConstance.GET_COINS
import com.ishant.blockchainexpo.data.model.spot.CoinDto
import retrofit2.http.POST

interface ApiInterface  {

    @POST(GET_COINS)
    suspend fun getAllCoins(): CoinDto


}