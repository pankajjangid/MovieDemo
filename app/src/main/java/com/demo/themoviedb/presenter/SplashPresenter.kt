package com.demo.themoviedb.presenter

import com.demo.themoviedb.MainActivity
import com.demo.themoviedb.MyApp
import com.demo.themoviedb.view.contract.SplashContract
import ru.terrakok.cicerone.Router


class SplashPresenter(private var view:SplashContract.View?) : SplashContract.Presenter {

    private val router : Router by lazy { MyApp.INSTANCE.cicerone.router }


    override fun onViewCreated() {

        router.navigateTo(MainActivity.TAG)
        view?.finishView()
    }

    override fun onDestroy() {
        view=null
    }


}