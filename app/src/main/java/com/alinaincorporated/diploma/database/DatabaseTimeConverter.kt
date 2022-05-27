package com.alinaincorporated.diploma.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import kotlinx.datetime.Instant

@ProvidedTypeConverter
internal class DatabaseTimeConverter {

    @TypeConverter
    fun instantToMilliseconds(instant: Instant?): Long? {
        return instant?.toEpochMilliseconds()
    }

    @TypeConverter
    fun millisecondsToInstant(milliseconds: Long?): Instant? {
        if (milliseconds == null) return null
        return Instant.fromEpochMilliseconds(milliseconds)
    }
}