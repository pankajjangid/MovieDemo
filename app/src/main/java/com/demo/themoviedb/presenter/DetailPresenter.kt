package com.demo.themoviedb.presenter

import com.demo.themoviedb.MyApp
import com.demo.themoviedb.entity.MoviesResponse
import com.demo.themoviedb.view.contract.DetailContract
import ru.terrakok.cicerone.Router

class DetailPresenter(private var view:DetailContract.View?):DetailContract.Presenter {
    private  val router : Router by lazy { MyApp.INSTANCE.cicerone.router }

    override fun onBackButtonClicked() {

router.exit()
    }

    override fun onViewCreated(movie: MoviesResponse.Result) {

        view?.showMoviesData(movie)
    }

    override fun onViewDestroyed() {

        view =null
    }
}