package com.eighthours.flow.presenter.behavior

import android.view.View
import com.eighthours.flow.domain.entity.AccountType.*
import com.eighthours.flow.domain.entity.ZERO
import com.eighthours.flow.domain.model.SaveAccountAction
import com.eighthours.flow.presenter.behavior.bean.AccountBean
import com.eighthours.flow.presenter.behavior.property.RxAmountProperty
import com.eighthours.flow.presenter.utility.Formatter
import com.eighthours.flow.presenter.utility.property.CommandProperty
import com.eighthours.flow.presenter.utility.property.ObservableProperty
import com.eighthours.flow.presenter.utility.property.RxStringProperty
import com.eighthours.flow.utility.Disposer
import com.eighthours.flow.utility.ManagedDisposable
import io.reactivex.Observable

class EditAccountBehavior(
        initial: AccountBean?,
        action: SaveAccountAction)
    : ManagedDisposable by Disposer() {

    val type = managed(ObservableProperty(initial?.type) {
        Formatter.format(it)
    })

    val name = managed(RxStringProperty(initial?.name))

    val group = managed(ObservableProperty(initial?.group,
            obsVisibility = type.map {
                if (it == ASSET || it == INCOME || it == OUTGO) View.VISIBLE
                else View.GONE
            }.startWith(View.GONE)) { it.name })

    val amount = managed(RxAmountProperty(initial?.amount,
            obsVisibility = type.map {
                if (it == ASSET) View.VISIBLE
                else View.GONE
            }.startWith(View.GONE)))

    private val bean = Observable.merge(type, name, group, amount)
            .map { createBean() }

    val cancel = managed(CommandProperty<View>())

    val save = managed(CommandProperty<View>(bean.map { it.isValid() }))

    init {
        type.subscribe {
            group.text.set(null)
            amount.update(ZERO)
        }

        save.firstElement().subscribe {
            val bean = createBean()
            val account = bean.toAccount()
            if (account.type == ASSET) {
                action.createAsset(account, bean.amount!!)
            } else {
                action.saveAccount(account)
            }
        }
    }

    fun createBean() = AccountBean(
            type = type.get(),
            name = name.get(),
            group = group.get(),
            amount = amount.get()
    )
}

