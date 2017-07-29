package com.eighthours.flow.presenter.fragment

import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import com.eighthours.flow.R
import com.eighthours.flow.databinding.FragmentCashflowBinding
import com.eighthours.flow.domain.entity.Account
import com.eighthours.flow.domain.model.TargetAccount
import com.eighthours.flow.presenter.behavior.CashflowBehavior
import com.eighthours.flow.presenter.behavior.bean.CashflowBean
import com.eighthours.flow.presenter.fragment.picker.AccountPickerFragment
import com.eighthours.flow.presenter.fragment.picker.DatePickerFragment
import com.eighthours.flow.presenter.utility.*
import com.eighthours.flow.presenter.viewmodel.AccountViewModel
import com.eighthours.flow.utility.ManagedDisposable
import com.eighthours.flow.utility.MultiTimeDisposer
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import org.threeten.bp.LocalDate

class CashflowFragment()
    : DialogFragment(), ManagedDisposable by MultiTimeDisposer(), FragmentAccessor,
        DatePickerFragment.OnDateSetListener,
        AccountPickerFragment.OnAccountSetListener {

    companion object {
        val CASHFLOW = BundleKey<CashflowBean>("CASHFLOW")
    }

    constructor(cashflow: CashflowBean) : this() {
        arguments = Bundle().apply {
            put(CASHFLOW, cashflow)
        }
    }

    private lateinit var vm: AccountViewModel

    private lateinit var behavior: CashflowBehavior

    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.v("")
        super.onCreate(savedInstanceState)
        vm = ViewModelProviders.of(activity, component.vmFactory()).get(AccountViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Logger.v("")
        val cashflow = savedInstanceState?.get(CASHFLOW) ?: arguments?.get(CASHFLOW)
        behavior = managed(CashflowBehavior(cashflow, component.submitCashflowAction()))

        val binding = FragmentCashflowBinding.inflate(LayoutInflater.from(activity))
        binding.behavior = behavior

        // Date picker
        behavior.date.command.subscribe {
            val fragment = DatePickerFragment(this, behavior.date.get()!!)
            fragment.show(fragmentManager, "datePicker")
        }

        // Account pickers
        behavior.from.command.subscribe {
            val fragment = AccountPickerFragment(this, TargetAccount.FROM, behavior.from.get())
            fragment.show(fragmentManager, "fromAccountPicker")
        }
        behavior.to.command.subscribe {
            val fragment = AccountPickerFragment(this, TargetAccount.TO, behavior.to.get())
            fragment.show(fragmentManager, "toAccountPicker")
        }

        // Close this screen
        Observable.merge(behavior.save, behavior.cancel)
                .firstElement().subscribe {
            dismiss()
        }

        return AlertDialog.Builder(activity)
                .setTitle(R.string.title_cashflow)
                .setView(binding.root)
                .create()
    }

    override fun onDateSet(date: LocalDate) {
        behavior.date.update(date)
    }

    override fun onAccountSet(target: TargetAccount, account: Account) {
        when (target) {
            TargetAccount.FROM -> behavior.from.update(account)
            TargetAccount.TO -> behavior.to.update(account)
            else -> IllegalArgumentException("Illegal target $target")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Logger.v("")
        outState.putIfNotNull(CASHFLOW, behavior.createBean())
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        Logger.v("")
        super.onDestroyView()
        dispose()
    }
}
