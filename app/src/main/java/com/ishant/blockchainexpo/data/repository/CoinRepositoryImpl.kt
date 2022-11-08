package com.ishant.blockchainexpo.data.repository

import com.ishant.blockchainexpo.data.model.spot.CoinDto
import com.ishant.blockchainexpo.data.remote.ApiInterface

class CoinRepositoryImpl(private val api: ApiInterface):CoinRepository {
    override suspend fun getAllCoin(): CoinDto {
        return api.getAllCoins()
    }
}