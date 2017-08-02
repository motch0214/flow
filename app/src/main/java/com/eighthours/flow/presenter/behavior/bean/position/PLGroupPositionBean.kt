package com.eighthours.flow.presenter.behavior.bean.position

import com.eighthours.flow.domain.entity.Account
import com.eighthours.flow.domain.entity.sum
import com.eighthours.flow.presenter.utility.Formatter

data class PLGroupPositionBean(
        val group: Account,
        val positions: List<InOutPositionBean>)
    : GroupPositionBean {

    override val id get() = group.id!!

    override val type get() = PositionBeanType.PL_GROUP

    val name get() = group.name

    val amount = Formatter.format(positions.map { it.position.amount }.sum())
}
