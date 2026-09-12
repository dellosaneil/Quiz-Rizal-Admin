package com.thelazybattley.joserizalquizadmin.data.local.db

import androidx.room.TypeConverter
import com.thelazybattley.joserizalquizadmin.data.local.entity.ChapterEntity
import com.thelazybattley.joserizalquizadmin.presentation.util.Category
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return json.decodeFromString(value)
    }

    @TypeConverter
    fun fromCategory(category: Category): String {
        return category.name
    }

    @TypeConverter
    fun toCategory(value: String): Category {
        return Category.valueOf(value)
    }

    @TypeConverter
    fun fromChapterEntity(value: List<ChapterEntity>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toChapterEntity(value: String): List<ChapterEntity> {
        return json.decodeFromString(value)
    }
}
