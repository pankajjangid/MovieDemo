package com.demo.themoviedb.view.contract

import com.demo.themoviedb.entity.MoviesResponse

interface DetailContract {

    interface View{

        fun showMoviesData(movie:MoviesResponse.Result)
        fun showInfo(msg:String)
    }

    interface Presenter{

        //user action
        fun onBackButtonClicked()

        //model updates
        fun onViewCreated(movie: MoviesResponse.Result)
        fun onViewDestroyed()
    }
}