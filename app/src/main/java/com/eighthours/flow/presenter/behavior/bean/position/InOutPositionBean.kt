package com.eighthours.flow.presenter.behavior.bean.position

import com.eighthours.flow.domain.entity.Account
import com.eighthours.flow.domain.entity.AccountType.INCOME
import com.eighthours.flow.domain.entity.Position
import com.eighthours.flow.presenter.utility.Formatter

data class InOutPositionBean(
        val position: Position,
        val account: Account) {

    val name get() = account.name

    val amount = Formatter.format(position.amount)

    val isIncome get() = account.type == INCOME
}
