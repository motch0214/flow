package com.eighthours.flow.data.initializer

import com.eighthours.flow.domain.entity.Account
import com.eighthours.flow.domain.entity.AccountType
import com.eighthours.flow.domain.entity.AccountType.*
import com.eighthours.flow.domain.entity.Amount
import com.eighthours.flow.domain.entity.Position
import com.eighthours.flow.domain.repository.Repository
import com.eighthours.flow.utility.async
import com.orhanobut.logger.Logger
import org.threeten.bp.YearMonth
import org.threeten.bp.ZonedDateTime

class DatabaseInitializer {

    var needInitialization = false

    fun initializeIfNeeded(repository: Repository) {
        if (needInitialization) {
            async { initialize(repository) }
        }
    }

    private fun initialize(repository: Repository) {
        Logger.i("initialize database")
        val now = ZonedDateTime.now()

        run {
            var id = 0L
            val holder = mutableMapOf<String, Account>()

            fun create(type: AccountType, name: String, group: String? = null) = holder.put(name, Account(
                    id = ++id,
                    type = type,
                    name = name,
                    groupId = group?.let { holder[it]?.id },
                    updatedDateTime = now))

            create(ASSET_GROUP, "Banks")
            create(ASSET, "Mizuho Bank", "Banks")
            create(ASSET_GROUP, "On Hand")
            create(ASSET, "wallet", "On Hand")
            create(ASSET, "Suica", "On Hand")
            create(PL_GROUP, "PL")
            create(OUTGO, "Outgo", "PL")
            create(INCOME, "Income", "PL")

            repository.account().insertAll(holder.values.toList())
        }

        run {
            val accounts = repository.account().loadAll().blockingFirst()
                    .map { it.name to it.id!! }.toMap()
            val holder = mutableListOf<Position>()

            fun create(account: String, month: YearMonth?, amount: Amount) = holder.add(Position(
                    id = null,
                    accountId = accounts.getValue(account),
                    month = month,
                    amount = amount,
                    updatedDateTime = now
            ))

            create("Mizuho Bank", null, Amount.valueOf(10_000_000L))

            repository.position().insertAll(holder)
        }
    }
}
