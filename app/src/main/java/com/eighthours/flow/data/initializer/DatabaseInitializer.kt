package com.eighthours.flow.data.initializer

import com.eighthours.flow.domain.entity.Account
import com.eighthours.flow.domain.entity.AccountType
import com.eighthours.flow.domain.entity.AccountType.*
import com.eighthours.flow.domain.repository.Repository
import com.eighthours.flow.utility.async
import com.orhanobut.logger.Logger
import org.threeten.bp.ZonedDateTime

class DatabaseInitializer {

    private var repository: Repository? = null

    fun initializeIfNeeded(repository: Repository) {
        this.repository = repository
    }

    fun initialize() = async {
        val repository = this.repository ?: return@async

        Logger.i("initialize database")
        val now = ZonedDateTime.now()

        var id = 0L
        val holder = mutableMapOf<String, Account>()

        fun create(type: AccountType, name: String, group: String? = null) = holder.put(name, Account(
                id = ++id,
                type = type,
                name = name,
                groupId = group?.let { holder[it]?.id },
                updatedDateTime = now))

        create(PL_GROUP, "Food")
        create(OUTGO, "groceries", "Food")
        create(OUTGO, "dining out", "Food")
        create(PL_GROUP, "Transportation")
        create(OUTGO, "commutation cost", "Transportation")
        create(OUTGO, "other transportation", "Transportation")
        create(PL_GROUP, "Commodity")
        create(OUTGO, "household groceries", "Commodity")
        create(OUTGO, "cigarette", "Commodity")
        create(OUTGO, "book", "Commodity")
        create(PL_GROUP, "Beauty")
        create(OUTGO, "clothes", "Beauty")
        create(OUTGO, "hair salons", "Beauty")
        create(OUTGO, "cleaners", "Beauty")
        create(PL_GROUP, "House")
        create(OUTGO, "rent", "House")
        create(OUTGO, "furniture", "House")
        create(OUTGO, "electric", "House")
        create(OUTGO, "gas", "House")
        create(OUTGO, "water", "House")
        create(OUTGO, "communication cost", "House")
        create(PL_GROUP, "Others")
        create(OUTGO, "entertainment", "Others")
        create(OUTGO, "other specials", "Others")
        create(OUTGO, "unknown", "Others")

        create(PL_GROUP, "Income")
        create(INCOME, "salary", "Income")

        repository.account().insertAll(holder.values.toList())
    }
}
