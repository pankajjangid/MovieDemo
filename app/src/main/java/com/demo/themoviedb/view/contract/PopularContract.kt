package com.demo.themoviedb.view.contract

import com.demo.themoviedb.entity.MoviesResponse
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.result.Result

interface PopularContract {

    interface View {
        fun showLoading()
        fun hideLoading()

        // API methods
        fun publishData(data:List<MoviesResponse.Result>)
        fun showInfo(msg:String)

        // Room DB methods
        fun deleteTable()
        fun addToTable(data:List<MoviesResponse.Result>)
        fun getFromTable()
    }

    interface Presenter {

        //User Actions
        fun listItemClicked(movie:MoviesResponse.Result?)

        //Model update
        fun onViewCreated()
        fun onDestroyed()
        fun onLoadNextPage(pageCount: Int)
    }

    interface Interactor {
        fun fetchPopularMovies(pageCount:Int,interactorOutput: (result: Result<Json, FuelError>) -> Unit)
    }

    interface InteractorOutput {

        fun onResultSuccess(data: List<MoviesResponse.Result>)
        fun onResultFailed()


    }
}