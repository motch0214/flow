package com.eighthours.flow.presenter.behavior

import android.view.View
import com.eighthours.flow.domain.model.SubmitCashflowAction
import com.eighthours.flow.presenter.behavior.bean.CashflowBean
import com.eighthours.flow.presenter.behavior.property.RxAmountProperty
import com.eighthours.flow.presenter.utility.Formatter
import com.eighthours.flow.presenter.utility.property.CommandProperty
import com.eighthours.flow.presenter.utility.property.ObservableProperty
import com.eighthours.flow.utility.Disposer
import com.eighthours.flow.utility.ManagedDisposable
import io.reactivex.Observable
import org.threeten.bp.LocalDate

class CashflowBehavior(
        initial: CashflowBean?,
        private val action: SubmitCashflowAction)
    : ManagedDisposable by Disposer() {

    val date = managed(ObservableProperty(initial?.date ?: LocalDate.now()) {
        Formatter.format(it)
    })

    val from = managed(ObservableProperty(initial?.from) {
        it.name
    })

    val to = managed(ObservableProperty(initial?.to) {
        it.name
    })

    val amount = managed(RxAmountProperty(initial?.amount))

    private val bean = Observable.merge(date, from, to, amount)
            .map { createBean() }

    val cancel = managed(CommandProperty<View>())

    val save = managed(CommandProperty<View>(bean.map { it.isValid() }))

    init {
        save.firstElement().subscribe {
            action.submit(createBean().toCashflow())
        }
    }

    fun createBean() = CashflowBean(
            date = date.get(),
            from = from.get(),
            to = to.get(),
            amount = amount.get()
    )
}
