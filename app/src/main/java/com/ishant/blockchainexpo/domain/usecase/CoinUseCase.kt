package com.ishant.blockchainexpo.domain.usecase

import com.ishant.blockchainexpo.data.model.spot.CoinDto
import com.ishant.blockchainexpo.network.Resource
import kotlinx.coroutines.flow.Flow

interface CoinUseCase {
    operator fun invoke(): Flow<Resource<CoinDto>>
}