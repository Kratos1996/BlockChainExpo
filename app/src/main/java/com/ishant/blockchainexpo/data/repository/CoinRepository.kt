package com.ishant.blockchainexpo.data.repository

import com.ishant.blockchainexpo.data.model.spot.CoinDto


interface CoinRepository {

    suspend fun getAllCoin(): CoinDto
}