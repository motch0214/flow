package com.eighthours.flow.domain.model

import com.eighthours.flow.domain.entity.Account
import com.eighthours.flow.domain.entity.Amount
import org.threeten.bp.LocalDate

data class Cashflow(
        val date: LocalDate,
        val from: Account,
        val to: Account,
        val amount: Amount)
