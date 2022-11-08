package com.ishant.blockchainexpo.application.websocket


import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ishant.blockchainexpo.application.constance.AppConstance
import com.ishant.blockchainexpo.application.constance.ErrorMessage.UNEXPECTED
import com.ishant.blockchainexpo.application.websocket.model.Binance
import com.ishant.blockchainexpo.application.websocket.model.BinanceItem
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import java.lang.reflect.Type

class WebSocketRepositoryImpl(private val client: HttpClient): WebSocketRepository {

    private var socket : WebSocketSession? = null

    override suspend fun openSocket(): WebSocketState {
        return try {
            socket = client.webSocketSession {
               url(AppConstance.BASE_URL_WEBSOCKET+"!ticker@arr")
            }
            if (socket?.isActive == true){
                WebSocketState.Connected("Connected")
            }else{
                WebSocketState.Disconnected("Disconnected")
            }
        }catch (e: Exception){
            WebSocketState.ErrorOnSocket(e.message?:UNEXPECTED)
        }
    }

    override fun getCurrentData(): Flow<Binance> {
        return try{
            socket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    Log.e("websocket", it.toString())
                    val json  = (it as? Frame.Text)?.readText() ?: ""
                    val type: Type = object : TypeToken<List<BinanceItem>>() {}.type
                    val binanceData= Gson().fromJson<List<BinanceItem>>(json, type)
                    Log.e("websocket", binanceData.toString())
                 Binance(ArrayList(binanceData))
                } ?: flow{}
        }catch(e : Exception){
            flow{}
        }
    }

    override suspend fun closeSocket() {
        socket?.close()
    }
}

