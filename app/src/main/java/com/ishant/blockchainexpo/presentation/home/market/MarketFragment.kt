package com.ishant.blockchainexpo.presentation.home.market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.ishant.blockchainexpo.R
import com.ishant.blockchainexpo.databinding.FragmentMarketBinding
import com.ishant.blockchainexpo.presentation.home.market.adapter.PagerAdapter
import com.ishant.blockchainexpo.presentation.home.viewmodel.HomeViewModel


class MarketFragment : Fragment() {
    private lateinit var binding: FragmentMarketBinding
    private lateinit var pagerAdapter: PagerAdapter
    private var indicatorWidth = 0
    private val tabList= mutableListOf<String>("Zones","Spot")
    private val onPageChangeListener: ViewPager2.OnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            val params = binding.indicator.layoutParams as LinearLayout.LayoutParams
            //Multiply positionOffset with indicatorWidth to get translation
            val translationOffset: Float = (positionOffset + position) * indicatorWidth
            params.leftMargin = translationOffset.toInt()
            binding.indicator.layoutParams = params
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_market, container, false)
        pagerAdapter = PagerAdapter(requireActivity())
        binding.viewPagerMarket.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayoutMain, binding.viewPagerMarket) { tab, position ->
            tab.text = tabList[position]
            binding.viewPagerMarket.setCurrentItem(tab.position, true)
        }.attach()

        //Determine indicator width at runtime
        binding.tabLayoutMain.post(Runnable {
            indicatorWidth = binding.tabLayoutMain.width / binding.tabLayoutMain.tabCount
            val indicatorParams = binding.indicator.layoutParams as LinearLayout.LayoutParams
            indicatorParams.width = indicatorWidth
            binding.indicator.layoutParams = indicatorParams
        })

        binding.tabLayoutMain.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPagerMarket.setCurrentItem(tab.position, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}


            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.viewPagerMarket.registerOnPageChangeCallback(onPageChangeListener)
    }

    override fun onPause() {
        super.onPause()
        binding.viewPagerMarket.unregisterOnPageChangeCallback(onPageChangeListener)
    }
}