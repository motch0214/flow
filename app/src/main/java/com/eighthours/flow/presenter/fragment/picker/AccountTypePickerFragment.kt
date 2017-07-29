package com.eighthours.flow.presenter.fragment.picker

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import com.eighthours.flow.domain.entity.AccountType
import com.eighthours.flow.presenter.adapter.AccountTypeSelectionAdapter
import com.eighthours.flow.presenter.utility.BundleKey
import com.eighthours.flow.presenter.utility.get
import com.eighthours.flow.presenter.utility.putIfNotNull
import com.orhanobut.logger.Logger

class AccountTypePickerFragment()
    : DialogFragment() {

    companion object {
        val SELECTED = BundleKey<AccountType>("SELECTED_ACCOUNT_TYPE")
    }

    constructor(fragment: Fragment, selected: AccountType?) : this() {
        setTargetFragment(fragment, 0)
        arguments = Bundle().apply {
            putIfNotNull(SELECTED, selected)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Logger.v("")
        val types = AccountType.values().toList()
        val selectedIndex = arguments.get(SELECTED)?.let { types.indexOf(it) } ?: -1

        return AlertDialog.Builder(activity)
                .setSingleChoiceItems(
                        AccountTypeSelectionAdapter(activity, types),
                        selectedIndex,
                        Listener(types))
                .create()
    }

    interface OnAccountTypeSetListener {
        fun onAccountTypeSet(type: AccountType)
    }

    private inner class Listener(
            val types: List<AccountType>)
        : DialogInterface.OnClickListener {

        override fun onClick(dialog: DialogInterface?, which: Int) {
            val selected = types[which]
            Logger.v("$selected picked")
            (targetFragment as OnAccountTypeSetListener).onAccountTypeSet(selected)
            dismiss()
        }
    }
}
