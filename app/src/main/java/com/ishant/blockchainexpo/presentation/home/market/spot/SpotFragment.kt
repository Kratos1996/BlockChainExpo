package com.ishant.blockchainexpo.presentation.home.market.spot

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ishant.blockchainexpo.R
import com.ishant.blockchainexpo.application.constance.ErrorMessage
import com.ishant.blockchainexpo.application.database.tables.Coin
import com.ishant.blockchainexpo.application.websocket.model.BinanceItem
import com.ishant.blockchainexpo.application.websocket.model.BinanceSealed
import com.ishant.blockchainexpo.databinding.FragmentZoneBinding
import com.ishant.blockchainexpo.presentation.home.market.adapter.MainLoadStateAdapter
import com.ishant.blockchainexpo.presentation.home.market.spot.adapter.SpotPagingAdapter
import com.ishant.blockchainexpo.presentation.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SpotFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: SpotPagingAdapter
    private lateinit var mainLoadStateAdapter: MainLoadStateAdapter
    private lateinit var binding: FragmentZoneBinding
    private var listOfCoin = ArrayList<Coin>()
    private var lastVisiblePosition = 5
    private var firstVisiblePosition = 0
    private var quoteCurrencyName = "USDT"
    var binanceList: ArrayList<BinanceItem> = ArrayList<BinanceItem>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_zone, container, false)
        adapter = SpotPagingAdapter(requireContext())
        mainLoadStateAdapter = MainLoadStateAdapter()
        binding.coinRecycler.adapter = adapter
        binding.coinRecycler.setHasFixedSize(true)
        binding.coinRecycler.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val manager = (binding.coinRecycler.layoutManager as LinearLayoutManager)!!
                lastVisiblePosition = manager!!.findLastCompletelyVisibleItemPosition()
                firstVisiblePosition = manager.findFirstCompletelyVisibleItemPosition()
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.connectSocketNow()
        lifecycleScope.launchWhenResumed {
            viewModel.db.isCoinUpdated(quoteCurrencyName).collectLatest {
                listOfCoin=ArrayList(it)
            }
        }
        /*Pagination-Adapter*/
        lifecycleScope.launchWhenResumed {
            viewModel.cryptoCurrencySpotData(currencyType =quoteCurrencyName ).collectLatest { result->
                    adapter.submitData(lifecycle,result)
                    adapter.withLoadStateFooter(mainLoadStateAdapter)
                }
        }
        /*WebSocket*/
        lifecycleScope.launchWhenResumed {
            viewModel.binanceMutableFlow.collectLatest {
                when (it) {
                    is BinanceSealed.Error -> {
                         Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                        Log.e("WebSocket", it.message ?: ErrorMessage.UNEXPECTED)
                    }
                    is BinanceSealed.Success -> {
                        Log.e("WebSocket", it.response.binance[0].symbol ?: "not Fetching")
                        binanceList = it.response.binance
                        updateDatabase()

                    }

                    else -> {}
                }
            }
        }
    }
    private fun updateDatabase() {
        if (firstVisiblePosition > -1 && (lastVisiblePosition > -1)) {
            if (lastVisiblePosition != firstVisiblePosition) {
                // Toast.makeText(requireContext(),lastVisiblePosition.toString(),Toast.LENGTH_SHORT).show()
                first@  for (i in firstVisiblePosition..lastVisiblePosition) {
                    var basecode = listOfCoin[i].base_coin_code
                    val finalCode =   basecode +quoteCurrencyName
                    if (binanceList.isNotEmpty()) {
                        run breaking@{
                            binanceList.forEach { item ->
                                if (item.symbol == finalCode) {
                                    viewModel.updateCoins(
                                        item.bestAskPrice,
                                        item.priceChangePercent,
                                        finalCode
                                    )
                                    return@breaking
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}