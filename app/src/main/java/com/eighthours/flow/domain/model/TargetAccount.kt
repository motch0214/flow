package com.eighthours.flow.domain.model

import com.eighthours.flow.domain.entity.Account
import com.eighthours.flow.domain.entity.AccountType

enum class TargetAccount(
        val filter: (Account) -> Boolean) {

    FROM({ it.type == AccountType.ASSET || it.type == AccountType.INCOME }),

    TO({ it.type == AccountType.ASSET || it.type == AccountType.OUTGO }),

    ASSET_GROUP({ it.type == AccountType.ASSET_GROUP }),

    PL_GROUP({ it.type == AccountType.PL_GROUP })
}
