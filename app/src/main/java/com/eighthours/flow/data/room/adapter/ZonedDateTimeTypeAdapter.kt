package com.eighthours.flow.data.room.adapter

import android.arch.persistence.room.TypeConverter
import org.threeten.bp.ZonedDateTime

class ZonedDateTimeTypeAdapter private constructor() {

    companion object {
        @TypeConverter @JvmStatic
        fun serialize(source: ZonedDateTime): String {
            return source.toString()
        }

        @TypeConverter @JvmStatic
        fun deserialize(serialized: String): ZonedDateTime {
            return ZonedDateTime.parse(serialized)
        }
    }
}
