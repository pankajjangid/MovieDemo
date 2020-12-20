package com.demo.themoviedb.view.contract

interface SplashContract {


    interface View{

        fun finishView()
    }

    interface  Presenter{

        fun onViewCreated()
        fun onDestroy()
    }
}