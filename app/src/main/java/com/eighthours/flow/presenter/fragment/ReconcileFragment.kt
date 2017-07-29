package com.eighthours.flow.presenter.fragment

import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.eighthours.flow.R
import com.eighthours.flow.databinding.FragmentReoncileBinding
import com.eighthours.flow.domain.entity.Amount
import com.eighthours.flow.domain.entity.Position
import com.eighthours.flow.presenter.behavior.ReconcileBehavior
import com.eighthours.flow.presenter.utility.*
import com.eighthours.flow.presenter.viewmodel.AccountViewModel
import com.eighthours.flow.utility.ManagedDisposable
import com.eighthours.flow.utility.MultiTimeDisposer
import com.orhanobut.logger.Logger
import io.reactivex.Observable

class ReconcileFragment()
    : DialogFragment(), ManagedDisposable by MultiTimeDisposer(), FragmentAccessor {

    companion object {
        val POSITION = BundleKey<Position>("POSITION")

        val AMOUNT = BundleKey<Amount>("AMOUNT")
    }

    private lateinit var behavior: ReconcileBehavior

    constructor(position: Position) : this() {
        arguments = Bundle().apply {
            put(POSITION, position)
        }
    }

    private lateinit var vm: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.v("")
        super.onCreate(savedInstanceState)
        vm = ViewModelProviders.of(activity, component.vmFactory()).get(AccountViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Logger.v("")
        val position = arguments.get(POSITION)!!
        val account = vm.findAccount(position.accountId)!!
        val amount = savedInstanceState?.get(AMOUNT)

        behavior = managed(ReconcileBehavior(position, account, amount))

        val binding = FragmentReoncileBinding.inflate(activity.layoutInflater)
        binding.behavior = behavior

        // Prepare cashflow
        behavior.ok.subscribe {
            val cashflow = behavior.createCashflow()
            Logger.d("open cashflow with $cashflow")
            val fragment = CashflowFragment(cashflow)
            fragment.show(fragmentManager, "cashflow")
        }

        // Close this screen
        Observable.merge(behavior.ok, behavior.cancel)
                .firstElement().subscribe {
            dismiss()
        }

        return AlertDialog.Builder(activity)
                .setView(binding.root)
                .setTitle(R.string.menu_reconcile)
                .create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Logger.v("")
        outState.putIfNotNull(AMOUNT, behavior.amount.get())
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        Logger.v("")
        super.onDestroyView()
        dispose()
    }
}
