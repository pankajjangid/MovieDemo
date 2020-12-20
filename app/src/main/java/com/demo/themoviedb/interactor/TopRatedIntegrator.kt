package com.demo.themoviedb.interactor

import android.util.Log
import com.demo.themoviedb.Constants
import com.demo.themoviedb.view.contract.PopularContract
import com.demo.themoviedb.view.contract.TopRatedContract
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result

class TopRatedIntegrator : TopRatedContract.Interactor{

    override fun fetchPopularMovies(pageCount: Int,interactorOutput: (result: Result<Json, FuelError>) -> Unit) {

        Log.d("URL",Constants.getCompleteURL(Constants.TOP_RATED_MOVIES,pageCount))
        Constants.getCompleteURL(Constants.TOP_RATED_MOVIES,pageCount).httpGet().responseJson { request, response, result ->

            interactorOutput(result)
        }
    }




}