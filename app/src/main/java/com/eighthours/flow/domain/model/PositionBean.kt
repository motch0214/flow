package com.eighthours.flow.domain.model

import com.eighthours.flow.domain.entity.AccountType
import com.eighthours.flow.domain.entity.Amount
import org.threeten.bp.YearMonth
import org.threeten.bp.ZonedDateTime

data class PositionBean(

        val group: String,

        val type: AccountType,

        val name: String,

        val month: YearMonth?,

        val amount: Amount,

        val updatedDateTime: ZonedDateTime
)
