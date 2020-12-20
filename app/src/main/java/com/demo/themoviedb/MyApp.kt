package com.demo.themoviedb

import android.app.Application
import com.demo.themoviedb.data.MoviesDatabase
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

class MyApp :Application() {


    // Routing layer (VIPER)
    lateinit var cicerone: Cicerone<Router>

    companion object{
        internal lateinit var INSTANCE: MyApp
            private set

        var database:MoviesDatabase ? =null
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE =this
        database  = MoviesDatabase.getDatabase(this)

        this.initCicerone()
    }

    private fun MyApp.initCicerone() {
        this.cicerone = ru.terrakok.cicerone.Cicerone.create()
    }
}