package com.ishant.blockchainexpo.application.websocket

sealed class WebSocketState(val message : String){
    class Connected(message: String) : WebSocketState(message)
    class Disconnected(message: String) : WebSocketState(message)
    class ErrorOnSocket(message: String) : WebSocketState(message)
}