package com.eighthours.flow.data.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.eighthours.flow.data.room.adapter.AccountTypeAdapter
import com.eighthours.flow.data.room.adapter.AmountTypeAdapter
import com.eighthours.flow.data.room.adapter.YearMonthTypeAdapter
import com.eighthours.flow.data.room.adapter.ZonedDateTimeTypeAdapter
import com.eighthours.flow.data.room.dao.AccountDao
import com.eighthours.flow.data.room.dao.PositionDao
import com.eighthours.flow.domain.entity.Account
import com.eighthours.flow.domain.entity.Position
import com.eighthours.flow.domain.repository.Repository

@Database(entities = arrayOf(
        Account::class,
        Position::class),
        version = 1, exportSchema = false)
@TypeConverters(
        AccountTypeAdapter::class,
        AmountTypeAdapter::class,
        YearMonthTypeAdapter::class,
        ZonedDateTimeTypeAdapter::class)
abstract class FlowDatabase
    : RoomDatabase(), Repository {

    companion object {
        const val NAME = "flow-db"
    }

    override abstract fun account(): AccountDao

    override abstract fun position(): PositionDao

    override fun transaction(task: () -> Unit) {
        beginTransaction()
        try {
            task()
            setTransactionSuccessful()
        } finally {
            endTransaction()
        }
    }
}
