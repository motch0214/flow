package com.eighthours.flow.domain.repository

import com.eighthours.flow.domain.entity.Account
import io.reactivex.Flowable

interface AccountRepository {

    fun loadAll(): Flowable<List<Account>>

    fun findAll(): List<Account>

    fun insert(account: Account): Long

    fun insertAll(accounts: List<Account>)

    fun updateAll(accounts: List<Account>)
}
