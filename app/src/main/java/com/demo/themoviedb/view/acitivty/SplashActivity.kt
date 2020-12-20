package com.demo.themoviedb.view.acitivty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.demo.themoviedb.MainActivity
import com.demo.themoviedb.MyApp
import com.demo.themoviedb.R
import com.demo.themoviedb.presenter.SplashPresenter
import com.demo.themoviedb.utils.Coroutines
import com.demo.themoviedb.view.contract.SplashContract
import kotlinx.coroutines.delay
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward


class SplashActivity : AppCompatActivity(),SplashContract.View {

    private val navigator:Navigator by lazy { object :Navigator{
        override fun applyCommand(command: Command?) {

            if (command is Forward)
            {
                goToMainActivity()
            }
        }

        private fun goToMainActivity() {

            startActivity(Intent(this@SplashActivity,MainActivity::class.java))

        }
    } }
    private var presenter : SplashContract.Presenter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        presenter = SplashPresenter(this)
    }




    override fun onResume() {
        super.onResume()
        MyApp.INSTANCE.cicerone.navigatorHolder.setNavigator(navigator)

        Coroutines.main {
            delay(2000)
            presenter?.onViewCreated()

        }

    }

    override fun onPause() {
        super.onPause()
        MyApp.INSTANCE.cicerone.navigatorHolder.removeNavigator()
    }
    override fun finishView() {

        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.onDestroy()
        presenter=null
    }
}