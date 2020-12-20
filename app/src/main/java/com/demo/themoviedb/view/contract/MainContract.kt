package com.demo.themoviedb.view.contract

interface MainContract {

    interface  View{
        fun initViews()
    }


    interface Presenter{
        fun onViewCreated()

        fun onDestroyed()

        fun onBackPressed()
    }
}