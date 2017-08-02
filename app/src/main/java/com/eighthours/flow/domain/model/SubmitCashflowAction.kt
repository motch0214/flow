package com.eighthours.flow.domain.model

import com.eighthours.flow.domain.entity.*
import com.eighthours.flow.domain.entity.AccountType.*
import com.eighthours.flow.domain.repository.Repository
import com.eighthours.flow.utility.async
import com.orhanobut.logger.Logger
import org.threeten.bp.YearMonth
import org.threeten.bp.ZonedDateTime
import javax.inject.Inject

class SubmitCashflowAction
@Inject constructor(
        private val repository: Repository) {

    fun submit(cashflow: Cashflow) = async {
        Logger.d("submit $cashflow")
        val month = YearMonth.of(cashflow.date.year, cashflow.date.month)
        val now = ZonedDateTime.now()
        repository.transaction {
            val from = findOrCreatePositionFor(cashflow.from, month).let {
                it.copy(
                        amount = if (cashflow.from.type == INCOME)
                            it.amount + cashflow.amount
                        else
                            it.amount - cashflow.amount,
                        updatedDateTime = now
                )
            }
            val to = findOrCreatePositionFor(cashflow.to, month).let {
                it.copy(
                        amount = if (cashflow.to.type == OUTGO)
                            it.amount - cashflow.amount
                        else
                            it.amount + cashflow.amount,
                        updatedDateTime = now
                )
            }
            Logger.v("save $from, $to")
            save(from, to)
        }
    }

    private fun findOrCreatePositionFor(account: Account, month: YearMonth): Position {
        val stored = when (account.type) {
            ASSET -> repository.position().findAssetPosition(account.id!!)
            INCOME, OUTGO -> repository.position().findInOutPosition(account.id!!, month)
            else -> throw IllegalArgumentException("invalid AccountType (${account.type})")
        }

        return stored ?: Position(
                id = null,
                accountId = account.id,
                month = if (account.type == ASSET) null else month,
                amount = ZERO,
                updatedDateTime = null
        )
    }

    private fun save(vararg positions: Position) {
        positions.forEach {
            if (it.id == null && it.amount.isNotZero()) {
                repository.position().insert(it)
            } else if (it.id != null && it.amount.isNotZero()) {
                repository.position().update(it)
            } else if (it.id != null && it.amount.isZero()) {
                repository.position().delete(it)
            }
        }
    }
}
