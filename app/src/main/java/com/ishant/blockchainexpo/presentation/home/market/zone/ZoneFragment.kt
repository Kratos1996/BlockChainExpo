package com.ishant.blockchainexpo.presentation.home.market.zone

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ishant.blockchainexpo.R
import com.ishant.blockchainexpo.application.constance.ErrorMessage.UNEXPECTED
import com.ishant.blockchainexpo.application.database.tables.Coin
import com.ishant.blockchainexpo.application.websocket.model.BinanceItem
import com.ishant.blockchainexpo.application.websocket.model.BinanceSealed
import com.ishant.blockchainexpo.databinding.FragmentZoneBinding
import com.ishant.blockchainexpo.presentation.home.market.adapter.MainLoadStateAdapter
import com.ishant.blockchainexpo.presentation.home.market.zone.adapter.ZonePagingAdapter
import com.ishant.blockchainexpo.presentation.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.collections.ArrayList


@AndroidEntryPoint
class ZoneFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: ZonePagingAdapter
    private lateinit var mainLoadStateAdapter: MainLoadStateAdapter
    private lateinit var binding: FragmentZoneBinding
    private var listOfCoin = ArrayList<Coin>()
    private var lastVisiblePosition = 5
    private var firstVisiblePosition = 0

    var binanceList: ArrayList<BinanceItem> = ArrayList<BinanceItem>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_zone, container, false)
        adapter = ZonePagingAdapter(requireContext())
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
        lifecycleScope.launchWhenResumed {
            viewModel.db.isCoinUpdated("USDT").collectLatest {
                listOfCoin=ArrayList(it)
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModel.cryptoCurrencyData.collectLatest { result ->
                adapter.submitData(lifecycle, result)
                adapter.withLoadStateFooter(mainLoadStateAdapter)
            }
        }
        viewModel.connectSocketNow()
        lifecycleScope.launchWhenResumed {
            viewModel.binanceMutableFlow.collectLatest {
                when (it) {
                    is BinanceSealed.Error -> {
                        Log.e("WebSocket", it.message ?: UNEXPECTED)
                    }
                    is BinanceSealed.Success -> {
                       // Log.e("WebSocket", it.response.binance[0].symbol +it.response.binance[0].bestAskPrice  ?: "not Fetching")
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
                    val finalCode =   basecode +"USDT"
                    if (binanceList.isNotEmpty()) {
                        run breaking@{
                            binanceList.forEach { item ->
                                if (item.symbol == finalCode) {
                                    viewModel.updateCoins(
                                        item.bestAskPrice,
                                        item.priceChangePercent,
                                        finalCode
                                    )

                                }
                            }
                        }
                    }

                }
            }
        }
    }


}