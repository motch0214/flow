package com.eighthours.flow.data.room.adapter

import android.arch.persistence.room.TypeConverter
import com.eighthours.flow.domain.entity.Amount

class AmountTypeAdapter private constructor() {

    companion object {
        @TypeConverter @JvmStatic
        fun serialize(source: Amount): String {
            return source.toString()
        }

        @TypeConverter @JvmStatic
        fun deserialize(serialized: String): Amount {
            return Amount(serialized)
        }
    }
}
