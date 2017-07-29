package com.eighthours.flow.domain.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import org.threeten.bp.ZonedDateTime
import java.io.Serializable

/**
 * Account represents a kind of sink of cash.
 *
 * Accounts are categorized by 4 types; Asset, Income, Outgo and Group.
 */
@Entity(indices = arrayOf(Index("type"), Index("groupId")))
data class Account(
        /**
         * null if this account has not been stored.
         */
        @PrimaryKey(autoGenerate = true)
        val id: Long?,

        val type: AccountType,

        val name: String,

        val groupId: Long?,

        val updatedDateTime: ZonedDateTime?,

        val isDeleted: Boolean = false

) : Serializable {

    init {
        assert(name.isNotBlank())
        if (type == AccountType.ASSET || type == AccountType.INCOME || type == AccountType.OUTGO) {
            assert(groupId != null)
        }
    }
}

enum class AccountType {

    ASSET,

    INCOME,

    OUTGO,

    /** Group only assets */
    ASSET_GROUP,

    /** Group only incomes and/or outgoes */
    PL_GROUP
}
