package com.demo.themoviedb.presenter

import com.demo.themoviedb.MyApp
import com.demo.themoviedb.data.MoviesDatabase
import com.demo.themoviedb.entity.MoviesResponse
import com.demo.themoviedb.interactor.PopularIntegrator
import com.demo.themoviedb.utils.NetworkUtils
import com.demo.themoviedb.view.acitivty.DetailActivity
import com.demo.themoviedb.view.contract.PopularContract
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.terrakok.cicerone.Router

class PopularPresenter(private var view: PopularContract.View?) : PopularContract.Presenter,
    PopularContract.InteractorOutput {


    private val router:Router?  by lazy { MyApp.INSTANCE.cicerone.router }
    private var interactor: PopularContract.Interactor? = PopularIntegrator()
    override fun listItemClicked(movie: MoviesResponse.Result?) {

      router?.navigateTo(DetailActivity.TAG, movie)


    }

    override fun onViewCreated() {

        view?.showLoading()

        //Check internet connections
        if (NetworkUtils.isOnline()){
            interactor?.fetchPopularMovies(pageCount = 1) { result ->

                when (result) {

                    is Result.Success -> {
                        val jokesJsonObject = result.get().obj()

                        //delete the local db data if api call successfully
                        view?.deleteTable()


                        val type = object : TypeToken<List<MoviesResponse.Result>>() {}.type
                        val moviesList: List<MoviesResponse.Result> =
                            Gson().fromJson(jokesJsonObject.getJSONArray("results").toString(), type)

                        //add response to local db for persistent storage
                        view?.addToTable(moviesList)


                        this.onResultSuccess(moviesList)
                    }
                    is Result.Failure -> {
                        val ex = result.getException()
                        println(ex)
                        this.onResultFailed()
                    }
                }
            }
        }else{
            view?.hideLoading()

            //Get the data from local db if user is not connected to internet
            view?.getFromTable()


        }


    }



    override fun onDestroyed() {
        interactor = null
        view = null
    }

    // call this method if user scroll to more than one page
    override fun onLoadNextPage(pageCount: Int) {

        if (pageCount!=1)
        interactor?.fetchPopularMovies(pageCount = pageCount) { result ->

            when (result) {

                is Result.Success -> {
                    val jokesJsonObject = result.get().obj()

                    val type = object : TypeToken<List<MoviesResponse.Result>>() {}.type
                    val moviesList: List<MoviesResponse.Result> =
                        Gson().fromJson(jokesJsonObject.getJSONArray("results").toString(), type)

                    //add response to local db for persistent storage
                    view?.addToTable(moviesList)

                    this.onResultSuccess(moviesList)
                }
                is Result.Failure -> {
                    val ex = result.getException()
                    println(ex)
                    this.onResultFailed()
                }
            }
        }

    }


    override fun onResultSuccess(data: List<MoviesResponse.Result>) {
        view?.hideLoading()
        view?.publishData(data)

    }

    override fun onResultFailed() {
        view?.hideLoading()
        view?.showInfo("Error please try again")

    }

}