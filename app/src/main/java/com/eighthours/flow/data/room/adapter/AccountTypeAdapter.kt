package com.eighthours.flow.data.room.adapter

import android.arch.persistence.room.TypeConverter
import com.eighthours.flow.domain.entity.AccountType

class AccountTypeAdapter private constructor() {

    companion object {
        @TypeConverter @JvmStatic
        fun serialize(source: AccountType): String {
            return source.name
        }

        @TypeConverter @JvmStatic
        fun deserialize(serialized: String): AccountType {
            return AccountType.valueOf(serialized)
        }
    }
}
