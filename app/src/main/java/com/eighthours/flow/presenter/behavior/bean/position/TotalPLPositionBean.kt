package com.eighthours.flow.presenter.behavior.bean.position

import com.eighthours.flow.domain.entity.Position
import com.eighthours.flow.domain.entity.sum
import com.eighthours.flow.presenter.utility.Formatter

data class TotalPLPositionBean(
        private val positions: List<Position>)
    : GroupPositionBean {

    override val id: Long get() = 0

    override val type get() = PositionBeanType.TOTAL_PL

    override val name = Formatter.format(TotalAccount.TOTAL_PL)

    override val amount = Formatter.format(positions.map { it.amount }.sum())
}
