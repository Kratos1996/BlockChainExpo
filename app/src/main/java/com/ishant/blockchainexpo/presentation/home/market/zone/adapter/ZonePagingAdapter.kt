package com.ishant.blockchainexpo.presentation.home.market.zone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ishant.blockchainexpo.R
import com.ishant.blockchainexpo.application.database.tables.Coin
import com.ishant.blockchainexpo.databinding.ZoneItemBinding
import java.math.RoundingMode
import java.text.DecimalFormat

class ZonePagingAdapter(private val context: Context) :
    PagingDataAdapter<Coin, ZonePagingAdapter.ZoneViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Coin>() {
            override fun areItemsTheSame(oldItem: Coin, newItem: Coin): Boolean =
                oldItem._id == newItem._id

            override fun areContentsTheSame(oldItem: Coin, newItem: Coin): Boolean =
                oldItem == newItem
        }
    }

    fun roundOffDecimal(number: Double): Double? {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.FLOOR
        return df.format(number).toDouble()
    }

    inner class ZoneViewHolder(private val binding: ZoneItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: Coin) {
            Glide.with(binding.root).load(item.base_coin_icon)
                .placeholder(R.drawable.bitcoin) //placeholder
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(binding.coinImage)
            binding.coinName.text = item.base_coin_name
            binding.coinVolume.text = buildString {
                append(roundOffDecimal(item.volume).toString())
                append(" M")
            }
            binding.coinCurrentValue.text = buildString {
                append("$ ")
                append(roundOffDecimal(item.price_in_dollar))
            }
            binding.coinCurrentData.text = buildString {
                append(roundOffDecimal(item.price_change_percent))
                append(" % ")
            }
            if (item.price_change_percent > 0) {
                binding.coinCurrentData.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.up_arrow,
                    0,
                    0,
                    0
                )
                binding.coinCurrentData.setTextColor(context.resources.getColor(R.color.green))
            } else {
                binding.coinCurrentData.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.down_arrow,
                    0,
                    0,
                    0
                )
                binding.coinCurrentData.setTextColor(context.resources.getColor(R.color.red))
            }
        }

    }

override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ZoneViewHolder {
    return ZoneViewHolder(
        ZoneItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )
}

override fun onBindViewHolder(holder: ZoneViewHolder, position: Int) {
    val item = getItem(position)
    holder.onBind(item!!)

}
}