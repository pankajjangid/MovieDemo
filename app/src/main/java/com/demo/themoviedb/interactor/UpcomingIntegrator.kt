package com.demo.themoviedb.interactor

import android.util.Log
import com.demo.themoviedb.Constants
import com.demo.themoviedb.view.contract.PopularContract
import com.demo.themoviedb.view.contract.UpcomingContract
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result

class UpcomingIntegrator : UpcomingContract.Interactor{

    override fun fetchPopularMovies(pageCount: Int,interactorOutput: (result: Result<Json, FuelError>) -> Unit) {

        Log.d("URL",Constants.getCompleteURL(Constants.UPCOMING_MOVIES,pageCount))
        Constants.getCompleteURL(Constants.UPCOMING_MOVIES,pageCount).httpGet().responseJson { request, response, result ->

            interactorOutput(result)
        }
    }




}