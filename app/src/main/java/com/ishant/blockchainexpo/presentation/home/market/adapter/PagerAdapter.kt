package com.ishant.blockchainexpo.presentation.home.market.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ishant.blockchainexpo.R
import com.ishant.blockchainexpo.presentation.home.market.spot.SpotFragment
import com.ishant.blockchainexpo.presentation.home.market.zone.ZoneFragment

class PagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    val pages = listOf(
        Pair(ZoneFragment(), R.string.zones),
        Pair(SpotFragment(), R.string.spot),
    )

    override fun createFragment(position: Int): Fragment {
        return pages[position].first
    }

    override fun getItemCount(): Int {
        return pages.count()
    }

}