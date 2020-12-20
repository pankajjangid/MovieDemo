package com.demo.themoviedb.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.demo.themoviedb.entity.MoviesResponse


@Dao
interface MovieDao {
/*

    @Query("SELECT * FROM popular_movies_table ORDER BY id ASC ")
    fun getAllData(): LiveData<List<MoviesResponse.Result>>
*/


    @Query("SELECT * FROM popular_movies_table  WHERE movieType LIKE 'POPULAR' ORDER BY ID")
    fun getAllPopularMovies(): LiveData<List<MoviesResponse.Result>>

    @Query("SELECT * FROM popular_movies_table  WHERE movieType LIKE 'TOP_RATED' ORDER BY ID")
    fun getAllTopRateMovies(): LiveData<List<MoviesResponse.Result>>

    @Query("SELECT * FROM popular_movies_table  WHERE movieType LIKE 'UPCOMING' ORDER BY ID")
    fun getAllUpcomingMovies(): LiveData<List<MoviesResponse.Result>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(toDoData: MoviesResponse.Result)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(order: List<MoviesResponse.Result>)


    @Update
    suspend fun update(toDoData: MoviesResponse.Result)

    @Delete
    suspend fun delete(toDoData: MoviesResponse.Result)

    @Query("DELETE FROM popular_movies_table WHERE movieType LIKE 'POPULAR'")
    suspend fun deletePopularMovies()

    @Query("DELETE FROM popular_movies_table WHERE movieType LIKE 'TOP_RATED'")
    suspend fun deleteTopRateMovies()

    @Query("DELETE FROM popular_movies_table WHERE movieType LIKE 'UPCOMING'")
    suspend fun deleteUpcomingMovies()
}