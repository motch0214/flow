package com.eighthours.flow.presenter.behavior.bean

import com.eighthours.flow.domain.entity.Account
import com.eighthours.flow.domain.entity.AccountType
import com.eighthours.flow.domain.entity.Amount
import com.eighthours.flow.domain.entity.ZERO
import org.threeten.bp.ZonedDateTime
import java.io.Serializable

data class AccountBean(
        val type: AccountType?,
        val name: String?,
        val group: Account?,
        val amount: Amount?)
    : Serializable {

    fun isValid(): Boolean {
        return when (type) {
            AccountType.ASSET -> group?.type == AccountType.ASSET_GROUP
                    && amount != null && amount != ZERO
            AccountType.INCOME, AccountType.OUTGO -> group?.type == AccountType.PL_GROUP
            else -> true
        } && name != null && name.isNotBlank()
    }

    fun toAccount() = Account(
            id = null,
            type = type!!,
            name = name!!,
            groupId = group?.id,
            updatedDateTime = ZonedDateTime.now()
    )
}
