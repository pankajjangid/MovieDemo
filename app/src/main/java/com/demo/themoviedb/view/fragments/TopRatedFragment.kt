package com.demo.themoviedb.view.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.themoviedb.MyApp
import com.demo.themoviedb.R
import com.demo.themoviedb.comman.BackButtonListener
import com.demo.themoviedb.comman.RouterProvider
import com.demo.themoviedb.data.model.MovieType
import com.demo.themoviedb.databinding.FragmentPopularBinding
import com.demo.themoviedb.databinding.FragmentTopRatedBinding
import com.demo.themoviedb.entity.MoviesResponse
import com.demo.themoviedb.presenter.TopRatedPresenter
import com.demo.themoviedb.utils.Coroutines
import com.demo.themoviedb.utils.MovieParser
import com.demo.themoviedb.view.acitivty.DetailActivity
import com.demo.themoviedb.view.adapters.MoviesAdapter
import com.demo.themoviedb.view.contract.TopRatedContract

import kotlinx.android.synthetic.main.fragment_top_rated.*
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward


class TopRatedFragment : Fragment(), TopRatedContract.View, BackButtonListener, RouterProvider {

    // initialize Room db
    private val movieDao = MyApp.database?.movieDao()

    // initialize the navigator
    private val navigator: Navigator? by lazy {
        object : Navigator {
            override fun applyCommand(command: Command) {
                if (command is Forward) {
                    forward(command)
                }
            }

            private fun forward(command: Forward) {
                val data = (command.transitionData as MoviesResponse.Result)

                when (command.screenKey) {
                    DetailActivity.TAG -> startActivity(
                        Intent(requireActivity(), DetailActivity::class.java)
                            .putExtra("data", data as Parcelable)
                    )   // 4
                    else -> Log.e("Cicerone", "Unknown screen: " + command.screenKey)
                }
            }
        }
    }


    var topRatedList: ArrayList<MoviesResponse.Result> = ArrayList()
    var page = 1
    var totalPages = 15


    private var mPresenter: TopRatedContract.Presenter? = null
    lateinit var mBinding: FragmentTopRatedBinding
    private var mAdapter: MoviesAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentTopRatedBinding.inflate(inflater)


        // Set Menu
        setHasOptionsMenu(true)

        //initialize presenter
        mPresenter = TopRatedPresenter(this)

        return mBinding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            TopRatedFragment().apply {

            }
    }


    override val router: Router
        get() = MyApp.INSTANCE.cicerone.router

    //Show progress bar
    override fun showLoading() {
        try {
            mBinding.rvTopRated.visibility = View.GONE
            progressBarTopRated.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //Hide progress bar

    override fun hideLoading() {
        try {
            mBinding.rvTopRated.visibility = View.VISIBLE
            progressBarTopRated.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //API data publish
    override fun publishData(movies: List<MoviesResponse.Result>) {

        setMovieAdapter(movies)
    }

    //Set the recyclerview adapter
    private fun setMovieAdapter(movies: List<MoviesResponse.Result>) {

        if (mAdapter == null) {
            topRatedList.clear()

            topRatedList.addAll(movies)

            val layoutManager =
                LinearLayoutManager(requireActivity())

            mBinding.rvTopRated.isNestedScrollingEnabled = false

            mBinding.rvTopRated.layoutManager = layoutManager
            mAdapter = MoviesAdapter(
                { result -> mPresenter?.listItemClicked(result) },
                topRatedList, mBinding.rvTopRated
            )

            mBinding.rvTopRated.adapter = mAdapter
            mBinding.rvTopRated.itemAnimator = DefaultItemAnimator()


            mAdapter!!.setOnLoadMoreListener(object : MoviesAdapter.OnLoadMoreListener {
                override fun onLoadMore() {
                    if (totalPages > page) {

                        val pojo = MoviesResponse.Result(
                            false,
                            "",

                            "",
                            "",
                            "",
                            "",
                            0.0,
                            "",
                            "",
                            "",
                            false,
                            0.0,
                            0,
                            MovieType.TOP_RATED
                        )

                        topRatedList.add(pojo)
                        mAdapter!!.notifyItemInserted(topRatedList.size - 1)
                        page += 1


                        mPresenter?.onLoadNextPage(page)

                    }

                }
            })

        } else {
            try {

                topRatedList.removeAt(topRatedList.size - 1)
                mAdapter!!.notifyItemRemoved(topRatedList.size)
                topRatedList.addAll(movies)


                mAdapter!!.notifyDataSetChanged()
                mAdapter!!.setLoaded()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        for (i in topRatedList.indices) {
            Log.d("Title $i", topRatedList[i].title)
        }

    }


    //Show information
    override fun showInfo(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    // Delete movie list data from local db

    override fun deleteTable() {


        Coroutines.main {
            movieDao?.deleteTopRateMovies()
        }
    }

    // Add movie list data to local db

    override fun addToTable(data: List<MoviesResponse.Result>) {

        Coroutines.main {
            data.iterator().forEach {
                it.movieType = MovieParser.parseMovieType(MovieParser.TOP_RATED)
                if (it.poster_path.isNullOrBlank()) {
                    it.poster_path = ""
                }
                if (it.backdrop_path.isNullOrBlank()) {
                    it.backdrop_path = ""
                }
            }
            movieDao?.insertAll(data)


        }
    }


    // Get movie list data from local db
    override fun getFromTable() {
        Coroutines.main {
            movieDao?.getAllTopRateMovies()?.observe(this, Observer {
                setMovieAdapter(it)
            })
        }

    }

    //Set the search view menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.search_menu, menu)
        val menuItem = menu.findItem(R.id.menu_search)
        val searchView = menuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                try {
                    mAdapter?.filter?.filter(newText)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                return false
            }
        })


    }


    override fun onResume() {
        super.onResume()
        MyApp.INSTANCE.cicerone.navigatorHolder.setNavigator(navigator)
        mPresenter?.onViewCreated()
    }

    override fun onPause() {
        super.onPause()
        MyApp.INSTANCE.cicerone.navigatorHolder.removeNavigator()
    }

    override fun onDestroy() {
        mPresenter?.onDestroyed()
        mPresenter = null
        super.onDestroy()

    }

    override fun onBackPressed(): Boolean {
        val fragment = childFragmentManager.findFragmentById(R.id.root_top_rated)
        return if (fragment != null && fragment is BackButtonListener
            && (fragment as BackButtonListener).onBackPressed()
        ) {
            true
        } else {
            (activity as RouterProvider?)!!.router.exit()
            true
        }
    }


}