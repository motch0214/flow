package com.eighthours.flow.presenter.adapter

import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.eighthours.flow.databinding.*
import com.eighthours.flow.presenter.behavior.bean.position.*
import io.reactivex.disposables.Disposable

class PositionListAdapter(
        context: Context)
    : RecyclerView.Adapter<PositionListAdapter.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    class ViewHolder(
            val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root)

    private var items: List<GroupPositionBean> = emptyList()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(index: Int): Int {
        return items[index].type.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val type = PositionBeanType.values()[viewType]
        val binding = when (type) {
            PositionBeanType.TOTAL_ASSET -> ItemTotalAssetPositionBinding.inflate(inflater)
            PositionBeanType.TOTAL_PL -> ItemTotalPlPositionBinding.inflate(inflater)
            PositionBeanType.ASSET_GROUP,
            PositionBeanType.PL_GROUP -> ItemGroupPositionBinding.inflate(inflater)
        }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, index: Int) {
        val type = items[index].type
        when (type) {
            PositionBeanType.TOTAL_ASSET -> {
                holder.binding as ItemTotalAssetPositionBinding
                holder.binding.bean = items[index] as TotalAssetPositionBean
            }
            PositionBeanType.TOTAL_PL -> {
                holder.binding as ItemTotalPlPositionBinding
                holder.binding.bean = items[index] as TotalPLPositionBean
            }
            PositionBeanType.ASSET_GROUP -> {
                holder.binding as ItemGroupPositionBinding
                (holder.binding.bean as? Disposable)?.dispose()
                bind(holder.binding, items[index] as AssetGroupPositionBean)
            }
            PositionBeanType.PL_GROUP -> {
                holder.binding as ItemGroupPositionBinding
                bind(holder.binding, items[index] as PLGroupPositionBean)
            }
        }
    }

    fun update(items: List<GroupPositionBean>) {
        val old = this.items
        this.items = items
        DiffUtil.calculateDiff(Callback(old, items)).dispatchUpdatesTo(this)
    }

    private class Callback(old: List<GroupPositionBean>, new: List<GroupPositionBean>)
        : DiffCallback<GroupPositionBean>(old, new) {

        override fun areItemsTheSame(oldItem: GroupPositionBean, newItem: GroupPositionBean): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GroupPositionBean, newItem: GroupPositionBean): Boolean {
            return oldItem == newItem
        }
    }


    private fun bind(binding: ItemGroupPositionBinding, bean: AssetGroupPositionBean) {
        binding.bean = bean
        val existence = binding.positions.childCount

        for ((i, position) in bean.positions.withIndex()) {
            val b = if (i < existence) {
                binding.positions.getChildAt(i).tag as ItemAssetPositionBinding
            } else {
                ItemAssetPositionBinding.inflate(inflater).apply {
                    root.tag = this
                    binding.positions.addView(root)
                }
            }
            b.bean = position
        }

        for (i in bean.positions.size until existence) {
            binding.positions.removeViewAt(bean.positions.size)
        }
    }

    private fun bind(binding: ItemGroupPositionBinding, bean: PLGroupPositionBean) {
        binding.bean = bean
        val existence = binding.positions.childCount

        for ((i, position) in bean.positions.withIndex()) {
            val b = if (i < existence) {
                binding.positions.getChildAt(i).tag as ItemInoutPositionBinding
            } else {
                ItemInoutPositionBinding.inflate(inflater).apply {
                    root.tag = this
                    binding.positions.addView(root)
                }
            }
            b.bean = position
        }

        for (i in bean.positions.size until existence) {
            binding.positions.removeViewAt(bean.positions.size)
        }
    }
}
