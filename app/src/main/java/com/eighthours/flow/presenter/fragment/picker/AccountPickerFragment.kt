package com.eighthours.flow.presenter.fragment.picker

import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import com.eighthours.flow.domain.entity.Account
import com.eighthours.flow.domain.model.TargetAccount
import com.eighthours.flow.presenter.adapter.AccountSelectionAdapter
import com.eighthours.flow.presenter.utility.*
import com.eighthours.flow.presenter.viewmodel.AccountViewModel
import com.orhanobut.logger.Logger

class AccountPickerFragment()
    : DialogFragment(), FragmentAccessor {

    companion object {
        val TARGET = BundleKey<TargetAccount>("TARGET_ACCOUNT")
        val SELECTED = BundleKey<Account>("SELECTED_ACCOUNT")
    }

    private lateinit var vm: AccountViewModel

    constructor(fragment: Fragment, target: TargetAccount, selected: Account?) : this() {
        setTargetFragment(fragment, 0)
        arguments = Bundle().apply {
            put(TARGET, target)
            putIfNotNull(SELECTED, selected)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.v("")
        super.onCreate(savedInstanceState)
        vm = ViewModelProviders.of(activity, component.vmFactory()).get(AccountViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Logger.v("")
        val target = arguments.get(TARGET)!!
        val accounts = filterAndSortFor(target, vm.findAccountMap())
        val selectedIndex = arguments.get(SELECTED)?.let { accounts.indexOf(it) } ?: -1

        return AlertDialog.Builder(activity)
                .setSingleChoiceItems(
                        AccountSelectionAdapter(activity, target, accounts),
                        selectedIndex,
                        Listener(target, accounts))
                .create()
    }

    interface OnAccountSetListener {
        fun onAccountSet(target: TargetAccount, account: Account)
    }

    private inner class Listener(
            val target: TargetAccount,
            val accounts: List<Account>)
        : DialogInterface.OnClickListener {

        override fun onClick(dialog: DialogInterface?, which: Int) {
            val selected = accounts[which]
            Logger.v("${selected.name} picked")
            (targetFragment as OnAccountSetListener).onAccountSet(target, selected)
            dismiss()
        }
    }

    private fun filterAndSortFor(target: TargetAccount, accountMap: Map<Long, Account>): List<Account> {
        return when (target) {
            TargetAccount.FROM,
            TargetAccount.TO -> {
                accountMap.values.filter(target.filter)
                        .groupBy { accountMap.getValue(it.groupId!!) }.entries
                        .sortedBy { (group, _) -> group.name }
                        .flatMap { (group, accounts) ->
                            listOf(group).plus(accounts.sortedBy { it.name })
                        }
            }
            TargetAccount.ASSET_GROUP,
            TargetAccount.PL_GROUP -> {
                accountMap.values.filter(target.filter)
                        .sortedBy { it.name }
            }
        }
    }
}
