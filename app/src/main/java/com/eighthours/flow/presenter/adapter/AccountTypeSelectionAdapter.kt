package com.eighthours.flow.presenter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.eighthours.flow.databinding.ItemAccountTypeSelectionBinding
import com.eighthours.flow.domain.entity.AccountType
import com.eighthours.flow.presenter.utility.Formatter

class AccountTypeSelectionAdapter(
        context: Context,
        types: List<AccountType>)
    : ArrayAdapter<AccountType>(context, 0, types) {

    private val inflater = LayoutInflater.from(context)

    override fun getView(index: Int, convertView: View?, parent: ViewGroup): View {
        val binding = if (convertView == null) {
            ItemAccountTypeSelectionBinding.inflate(inflater).apply {
                root.tag = this
            }
        } else {
            convertView.tag as ItemAccountTypeSelectionBinding
        }

        binding.name.text = Formatter.format(getItem(index))

        return binding.root
    }
}
