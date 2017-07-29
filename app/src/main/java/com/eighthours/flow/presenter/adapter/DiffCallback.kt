package com.eighthours.flow.presenter.adapter

import android.support.v7.util.DiffUtil

abstract class DiffCallback<in M>(
        private val old: List<M>,
        private val new: List<M>)
    : DiffUtil.Callback() {

    abstract fun areItemsTheSame(oldItem: M, newItem: M): Boolean

    abstract fun areContentsTheSame(oldItem: M, newItem: M): Boolean

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areItemsTheSame(old[oldItemPosition], new[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areContentsTheSame(old[oldItemPosition], new[newItemPosition])
    }

    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return new.size
    }
}
