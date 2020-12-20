package com.demo.themoviedb.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.demo.themoviedb.data.dao.MovieDao
import com.demo.themoviedb.entity.MoviesResponse

@Database(entities = [MoviesResponse.Result::class],version = 1)
@TypeConverters(Converter::class)
abstract class MoviesDatabase:RoomDatabase() {

    abstract fun movieDao():MovieDao

    companion object {

        @Volatile
        var INSTANCE:MoviesDatabase?=null

        fun getDatabase(context:Context):MoviesDatabase{
            val tempInstance = INSTANCE
            if (tempInstance!=null){
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,MoviesDatabase::class.java,"movies_database").build()

                INSTANCE =instance

                return  instance
            }
        }
    }


}