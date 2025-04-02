package com.plcoding.bookpedia.book.data.database

import androidx.room.TypeConverter

object StringListTypeConverter {
    @TypeConverter
    fun fromString(value: String): List<String> {
        val list = value.split(",")
        return list.map { it.trim() }
    }

    @TypeConverter
    fun toString(value: List<String>): String = value.joinToString(",")
}