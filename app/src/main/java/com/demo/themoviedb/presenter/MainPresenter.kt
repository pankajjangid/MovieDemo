package com.demo.themoviedb.presenter

import com.demo.themoviedb.MyApp
import com.demo.themoviedb.view.contract.MainContract
import ru.terrakok.cicerone.Router

class MainPresenter(private var view: MainContract.View?) : MainContract.Presenter {


    private val router :Router by lazy { MyApp.INSTANCE.cicerone.router }
    override fun onViewCreated() {
        view?.initViews()
    }

    override fun onDestroyed() {
        view = null
    }

    override fun onBackPressed() {

        router.exit()
    }
}