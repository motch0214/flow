package com.eighthours.flow.presenter.behavior.bean

import com.eighthours.flow.domain.entity.Account
import com.eighthours.flow.domain.entity.Amount
import com.eighthours.flow.domain.entity.ZERO
import com.eighthours.flow.domain.model.Cashflow
import org.threeten.bp.LocalDate
import java.io.Serializable

data class CashflowBean(
        val date: LocalDate?,
        val from: Account?,
        val to: Account?,
        val amount: Amount?)
    : Serializable {

    fun isValid(): Boolean {
        return date != null
                && from != null
                && to != null
                && from != to
                && amount != null
                && amount != ZERO
    }

    fun toCashflow() = Cashflow(
            date = date!!,
            from = from!!,
            to = to!!,
            amount = amount!!
    )
}
