package com.eighthours.flow.data.room.adapter

import android.arch.persistence.room.TypeConverter
import org.threeten.bp.YearMonth

class YearMonthTypeAdapter private constructor() {

    companion object {
        @TypeConverter @JvmStatic
        fun serialize(source: YearMonth?): String? {
            return source?.toString()
        }

        @TypeConverter @JvmStatic
        fun deserialize(serialized: String?): YearMonth? {
            return serialized?.let { YearMonth.parse(it) }
        }
    }
}
