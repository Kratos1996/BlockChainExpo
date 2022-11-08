package com.ishant.blockchainexpo.application.websocket

import com.ishant.blockchainexpo.application.websocket.model.Binance
import kotlinx.coroutines.flow.Flow

interface WebSocketRepository {

    suspend fun openSocket() : WebSocketState

    fun getCurrentData(): Flow<Binance>

    suspend fun  closeSocket()


}