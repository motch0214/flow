package com.eighthours.flow.domain.repository

import com.eighthours.flow.domain.entity.Account
import com.eighthours.flow.domain.entity.AccountType
import io.reactivex.Flowable

interface AccountRepository {

    fun loadAll(): Flowable<List<Account>>

    fun findAccountById(id: Long): Account?

    fun findAccountsByType(type: AccountType): List<Account>

    fun insert(account: Account): Long

    fun insertAll(accounts: List<Account>)

    fun updateAll(accounts: List<Account>)
}
