package com.eighthours.flow.presenter.behavior.bean.position

interface GroupPositionBean {

    val id: Long

    val type: PositionBeanType
}

enum class PositionBeanType {
    TOTAL_ASSET, ASSET_GROUP, TOTAL_PL, PL_GROUP
}

enum class TotalAccount {
    TOTAL_ASSET, TOTAL_PL
}
