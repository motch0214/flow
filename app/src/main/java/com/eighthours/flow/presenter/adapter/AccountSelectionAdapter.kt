package com.eighthours.flow.presenter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.eighthours.flow.databinding.ItemAccountSelectionBinding
import com.eighthours.flow.databinding.ItemAccountSelectionSeparatorBinding
import com.eighthours.flow.domain.entity.Account
import com.eighthours.flow.domain.model.TargetAccount

class AccountSelectionAdapter(
        context: Context,
        private val target: TargetAccount,
        accounts: List<Account>)
    : ArrayAdapter<Account>(context, 0, accounts) {

    private val inflater = LayoutInflater.from(context)

    override fun areAllItemsEnabled(): Boolean {
        return when (target) {
            TargetAccount.FROM,
            TargetAccount.TO -> false
            TargetAccount.ASSET_GROUP,
            TargetAccount.PL_GROUP -> true
        }
    }

    override fun isEnabled(index: Int): Boolean {
        return when (target) {
            TargetAccount.FROM,
            TargetAccount.TO -> getItem(index).groupId != null
            TargetAccount.ASSET_GROUP,
            TargetAccount.PL_GROUP -> true
        }
    }

    override fun getView(index: Int, convertView: View?, parent: ViewGroup): View {
        if (isEnabled(index)) {
            val binding = if (convertView == null
                    || convertView.tag !is ItemAccountSelectionBinding) {
                ItemAccountSelectionBinding.inflate(inflater).apply {
                    root.tag = this
                }
            } else {
                convertView.tag as ItemAccountSelectionBinding
            }

            binding.account = getItem(index)

            return binding.root

        } else {
            val binding = if (convertView == null
                    || convertView.tag !is ItemAccountSelectionSeparatorBinding) {
                ItemAccountSelectionSeparatorBinding.inflate(inflater).apply {
                    root.tag = this
                }
            } else {
                convertView.tag as ItemAccountSelectionSeparatorBinding
            }

            binding.group = getItem(index)

            return binding.root
        }
    }
}
