package com.ishant.blockchainexpo.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ishant.blockchainexpo.application.websocket.WebSocketRepository
import com.ishant.blockchainexpo.application.websocket.WebSocketState
import com.ishant.blockchainexpo.application.websocket.model.BinanceSealed
import com.ishant.blockchainexpo.data.datasource.CoinDatasource
import com.ishant.blockchainexpo.data.model.spot.getCoins
import com.ishant.blockchainexpo.data.repository.database.DatabaseRepository
import com.ishant.blockchainexpo.domain.usecase.CoinUseCase
import com.ishant.blockchainexpo.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val coinUseCase: CoinUseCase,
    private val config: PagingConfig,
    private val datasource: CoinDatasource,
    val db: DatabaseRepository,
    private val service: WebSocketRepository
) : ViewModel() {
    val cryptoCurrencyData = Pager(config) { db.getAllCoins("USDT") }.flow.cachedIn(viewModelScope)
    fun cryptoCurrencySpotData(currencyType:String) = Pager(config) { db.getAllCoins(currencyType) }.flow.cachedIn(viewModelScope)
    var binanceMutableFlow = MutableStateFlow<BinanceSealed>(BinanceSealed.Empty)


    fun getAllCoin() {
        coinUseCase().onEach {
            when (it) {
                is Resource.Success -> {
                    //save into database
                    db.deleteAndCreate(it.data!!.getCoins())
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
            }

        }.launchIn(viewModelScope)
    }

    fun connectSocketNow() {
        viewModelScope.launch {
            when (val result = service.openSocket()) {
                is WebSocketState.Connected -> {
                    binanceMutableFlow.value = BinanceSealed.Error(result.message)
                    delay(3000)
                    service.getCurrentData()
                        .onEach { coinData ->
                            binanceMutableFlow.value = BinanceSealed.Success(coinData)
                        }.launchIn(viewModelScope)
                }
                is WebSocketState.Disconnected -> {
                    binanceMutableFlow.value = BinanceSealed.Error(result.message)
                }

                else -> {

                }
            }
        }
    }

    private fun socketDisconnectedNow() {
        viewModelScope.launch {
            service.closeSocket()
        }
    }

    override fun onCleared() {
        super.onCleared()
        socketDisconnectedNow()
    }

    fun updateCoins(priceChange: String?, priceChangePercent: String?, baseCoinCode: String) {
        viewModelScope.launch {
            db.updateCoins(priceChange!!, priceChangePercent!!, baseCoinCode)
        }
    }


}