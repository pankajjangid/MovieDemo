package com.demo.themoviedb.view.acitivty

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.demo.themoviedb.Constants
import com.demo.themoviedb.MyApp
import com.demo.themoviedb.R
import com.demo.themoviedb.entity.MoviesResponse
import com.demo.themoviedb.presenter.DetailPresenter
import com.demo.themoviedb.view.contract.DetailContract
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.view_toolbar.*
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.commands.Back
import ru.terrakok.cicerone.commands.Command

class DetailActivity : AppCompatActivity(), DetailContract.View {

    companion object {
        val TAG = "DetailActivity"
    }




    private var mPresenter: DetailContract.Presenter? = null
    private val navigation: Navigator by lazy {
        object : Navigator {
            override fun applyCommand(command: Command) {
                if (command is Back) {
                    back()
                }
            }

            private fun back() {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)

        setSupportActionBar(tool_bar)

        mPresenter = DetailPresenter(this)

    }

    override fun onResume() {
        super.onResume()
        // add back arrow to toolbar
        supportActionBar?.let {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        // load invoking arguments
        val argument = intent.getParcelableExtra<MoviesResponse.Result>("data")
        argument?.let { mPresenter?.onViewCreated(it) }


        MyApp.INSTANCE.cicerone.navigatorHolder.setNavigator(navigation)
    }

    override fun onPause() {
        super.onPause()
        MyApp.INSTANCE.cicerone.navigatorHolder.removeNavigator()
    }

    override fun showMoviesData(movie: MoviesResponse.Result) {


        try {
            release_date_tv.text = "Release Date : ${movie.release_date}"
            title_tv.text = movie.title
            rating_tv.text = "Rating : ${movie.vote_average}"
            overview_tv.text = movie.overview

            Glide.with(this)
                .load(Constants.IMAGE_BASE_URL + movie.backdrop_path).placeholder(R.drawable.no_image).into(
                    poster_img
                );
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    override fun showInfo(msg: String) {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                mPresenter?.onBackButtonClicked()
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        mPresenter?.onViewDestroyed()
        mPresenter = null
        super.onDestroy()
    }


}