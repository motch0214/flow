package com.eighthours.flow.domain.model

import com.eighthours.flow.domain.entity.Account
import com.eighthours.flow.domain.entity.Amount
import com.eighthours.flow.domain.entity.Position
import com.eighthours.flow.domain.repository.Repository
import com.eighthours.flow.utility.async
import com.orhanobut.logger.Logger
import javax.inject.Inject

class SaveAccountAction
@Inject constructor(
        private val repository: Repository) {

    fun createAsset(asset: Account, amount: Amount) = async {
        Logger.v("save $asset with $amount")
        repository.transaction {
            val id = repository.account().insert(asset)
            val position = Position(
                    id = null,
                    accountId = id,
                    month = null,
                    amount = amount,
                    updatedDateTime = asset.updatedDateTime)
            repository.position().insertAll(listOf(position))
        }
    }

    fun saveAccount(account: Account) = async {
        Logger.v("save $account")
        insertOrUpdate(account)
    }

    private fun insertOrUpdate(account: Account) {
        if (account.id == null) {
            repository.account().insertAll(listOf(account))
        } else {
            repository.account().updateAll(listOf(account))
        }
    }
}
