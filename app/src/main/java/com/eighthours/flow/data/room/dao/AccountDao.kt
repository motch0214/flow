package com.eighthours.flow.data.room.dao

import android.arch.persistence.room.*
import com.eighthours.flow.domain.entity.Account
import com.eighthours.flow.domain.entity.AccountType
import com.eighthours.flow.domain.repository.AccountRepository
import io.reactivex.Flowable

@Dao
interface AccountDao
    : AccountRepository {

    @Query("""
        SELECT * FROM Account
    """)
    override fun loadAll(): Flowable<List<Account>>

    @Query("""
        SELECT * FROM Account WHERE id = :id
    """)
    override fun findAccountById(id: Long): Account?

    @Query("""
        SELECT * FROM Account WHERE type = :type
    """)
    override fun findAccountsByType(type: AccountType): List<Account>

    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    override fun insert(account: Account): Long

    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    override fun insertAll(accounts: List<Account>)

    @Update(onConflict = OnConflictStrategy.ROLLBACK)
    override fun updateAll(accounts: List<Account>)
}
