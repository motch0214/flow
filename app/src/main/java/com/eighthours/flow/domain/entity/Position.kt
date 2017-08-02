package com.eighthours.flow.domain.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import org.threeten.bp.YearMonth
import org.threeten.bp.ZonedDateTime
import java.io.Serializable
import java.math.BigDecimal

/**
 * Position represents amount of an account.
 */
@Entity(foreignKeys = arrayOf(ForeignKey(entity = Account::class,
        parentColumns = arrayOf("id"), childColumns = arrayOf("accountId"))),
        indices = arrayOf(Index("accountId"), Index("month")))
data class Position(
        /**
         * null if this position has not been stored.
         */
        @PrimaryKey(autoGenerate = true)
        val id: Long?,

        val accountId: Long,

        val month: YearMonth?,

        val amount: Amount,

        val updatedDateTime: ZonedDateTime?

) : Serializable


typealias Amount = BigDecimal

val ZERO: BigDecimal = BigDecimal.ZERO

fun List<Amount>.sum(): Amount {
    return this.fold(ZERO) { acc, current -> acc + current }
}

fun Amount.isZero(): Boolean = this.compareTo(ZERO) == 0

fun Amount.isNotZero(): Boolean = this.compareTo(ZERO) != 0
