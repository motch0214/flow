package com.eighthours.flow.presenter.behavior.bean.position

import com.eighthours.flow.domain.entity.sum
import com.eighthours.flow.presenter.utility.Formatter

data class TotalAssetPositionBean(
        private val positions: List<AssetPositionBean>)
    : GroupPositionBean {

    override val id: Long get() = 0

    override val type get() = PositionBeanType.TOTAL_ASSET

    override val name = Formatter.format(TotalAccount.TOTAL_ASSET)

    override val amount = Formatter.format(positions.map { it.position.amount }.sum())
}
