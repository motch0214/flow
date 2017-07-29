package com.eighthours.flow.presenter.fragment

import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import com.eighthours.flow.R
import com.eighthours.flow.databinding.FragmentEditAccountBinding
import com.eighthours.flow.domain.entity.Account
import com.eighthours.flow.domain.entity.AccountType
import com.eighthours.flow.domain.entity.AccountType.*
import com.eighthours.flow.domain.model.TargetAccount
import com.eighthours.flow.presenter.behavior.EditAccountBehavior
import com.eighthours.flow.presenter.behavior.bean.AccountBean
import com.eighthours.flow.presenter.fragment.picker.AccountPickerFragment
import com.eighthours.flow.presenter.fragment.picker.AccountTypePickerFragment
import com.eighthours.flow.presenter.utility.BundleKey
import com.eighthours.flow.presenter.utility.FragmentAccessor
import com.eighthours.flow.presenter.utility.get
import com.eighthours.flow.presenter.utility.putIfNotNull
import com.eighthours.flow.presenter.viewmodel.AccountViewModel
import com.eighthours.flow.utility.ManagedDisposable
import com.eighthours.flow.utility.MultiTimeDisposer
import com.orhanobut.logger.Logger
import io.reactivex.Observable

class EditAccountFragment() : DialogFragment(),
        ManagedDisposable by MultiTimeDisposer(),
        AccountTypePickerFragment.OnAccountTypeSetListener,
        AccountPickerFragment.OnAccountSetListener,
        FragmentAccessor {

    companion object {
        val ACCOUNT = BundleKey<AccountBean>("ACCOUNT")
    }

    private lateinit var vm: AccountViewModel

    private lateinit var behavior: EditAccountBehavior

    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.v("")
        super.onCreate(savedInstanceState)
        vm = ViewModelProviders.of(activity, component.vmFactory()).get(AccountViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Logger.v("")
        val account = savedInstanceState?.get(ACCOUNT)
        behavior = managed(EditAccountBehavior(account, component.saveAccountAction()))

        val binding = FragmentEditAccountBinding.inflate(LayoutInflater.from(activity))
        binding.behavior = behavior

        // Account type picker
        behavior.type.command.subscribe {
            val fragment = AccountTypePickerFragment(this, behavior.type.get())
            fragment.show(fragmentManager, "accountTypePicker")
        }

        // Group account picker
        behavior.group.command.subscribe {
            val target = when (behavior.type.get()) {
                ASSET -> TargetAccount.ASSET_GROUP
                INCOME, OUTGO -> TargetAccount.PL_GROUP
                else -> throw IllegalArgumentException("unexpected type ${behavior.type.get()}")
            }
            val fragment = AccountPickerFragment(this, target, behavior.group.get())
            fragment.show(fragmentManager, "groupAccountPicker")
        }

        // Close this dialog
        Observable.merge(behavior.save, behavior.cancel)
                .firstElement().subscribe {
            dismiss()
        }

        return AlertDialog.Builder(activity)
                .setTitle(R.string.title_add_account)
                .setView(binding.root)
                .create()
    }

    override fun onAccountTypeSet(type: AccountType) {
        behavior.type.update(type)
    }

    override fun onAccountSet(target: TargetAccount, account: Account) {
        behavior.group.update(account)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Logger.v("")
        outState.putIfNotNull(ACCOUNT, behavior.createBean())
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        Logger.v("")
        super.onDestroyView()
        dispose()
    }
}
