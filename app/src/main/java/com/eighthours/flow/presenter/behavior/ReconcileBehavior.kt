package com.eighthours.flow.presenter.behavior

import android.view.View
import com.eighthours.flow.domain.entity.Account
import com.eighthours.flow.domain.entity.Amount
import com.eighthours.flow.domain.entity.Position
import com.eighthours.flow.domain.entity.ZERO
import com.eighthours.flow.presenter.behavior.bean.CashflowBean
import com.eighthours.flow.presenter.behavior.property.RxAmountProperty
import com.eighthours.flow.presenter.utility.property.CommandProperty
import com.eighthours.flow.utility.Disposer
import com.eighthours.flow.utility.ManagedDisposable
import org.threeten.bp.LocalDate

class ReconcileBehavior(
        private val position: Position,
        private val account: Account,
        initial: Amount? = null)
    : ManagedDisposable by Disposer() {

    val name: String = account.name

    val amount = managed(RxAmountProperty(initial))

    val cancel = managed(CommandProperty<View>())

    val ok = managed(CommandProperty<View>(amount.map { it != ZERO }))

    fun createCashflow(): CashflowBean {
        val amount = amount.get()!!
        return CashflowBean(
                date = LocalDate.now(),
                from = if (position.amount > amount) account else null,
                to = if (position.amount < amount) account else null,
                amount = (position.amount - amount).abs()
        )
    }
}
