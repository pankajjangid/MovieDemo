package com.demo.themoviedb.data

import androidx.room.TypeConverter
import com.demo.themoviedb.data.model.MovieType

class Converter {

    @TypeConverter
    fun fromPriority(priority: MovieType): String {
        return priority.name
    }

    @TypeConverter
    fun toPriority(priority: String): MovieType {

        return MovieType.valueOf(priority)
    }
}