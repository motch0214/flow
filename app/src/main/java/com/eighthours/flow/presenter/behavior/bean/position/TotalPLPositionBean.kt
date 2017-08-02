package com.eighthours.flow.presenter.behavior.bean.position

import com.eighthours.flow.domain.entity.AccountType.INCOME
import com.eighthours.flow.domain.entity.AccountType.OUTGO
import com.eighthours.flow.domain.entity.isZero
import com.eighthours.flow.domain.entity.sum
import com.eighthours.flow.presenter.utility.Formatter

data class TotalPLPositionBean(
        private val positions: List<InOutPositionBean>)
    : GroupPositionBean {

    override val id: Long get() = 0

    override val type get() = PositionBeanType.TOTAL_PL

    val name = Formatter.format(TotalAccount.TOTAL_PL)

    val amount = Formatter.format(positions.map { it.position.amount }.sum())

    private val incomeAmount = positions.filter { it.account.type == INCOME }
            .map { it.position.amount }.sum()

    private val outgoAmount = positions.filter { it.account.type == OUTGO }
            .map { it.position.amount }.sum()

    val income = Formatter.format(incomeAmount)

    val outgo = Formatter.format(outgoAmount)

    val isEitherZero = incomeAmount.isZero() || outgoAmount.isZero()
}
