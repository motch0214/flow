package com.eighthours.flow.presenter.behavior.bean.position

import android.view.View
import com.eighthours.flow.domain.entity.Account
import com.eighthours.flow.domain.entity.Position
import com.eighthours.flow.presenter.utility.Formatter
import com.eighthours.flow.presenter.utility.property.CommandProperty
import com.eighthours.flow.utility.Disposer
import com.eighthours.flow.utility.ManagedDisposable

data class AssetPositionBean(
        val position: Position,
        val account: Account)
    : ManagedDisposable by Disposer() {

    val name get() = account.name

    val amount = Formatter.format(position.amount)

    val openMenu = managed(CommandProperty<View>())
}
