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
import com.demo.themoviedb.databinding.FragmentUpcomingBinding
import com.demo.themoviedb.entity.MoviesResponse
import com.demo.themoviedb.presenter.UpcomingPresenter
import com.demo.themoviedb.utils.Coroutines
import com.demo.themoviedb.utils.MovieParser
import com.demo.themoviedb.view.acitivty.DetailActivity
import com.demo.themoviedb.view.adapters.MoviesAdapter
import com.demo.themoviedb.view.contract.UpcomingContract
import kotlinx.android.synthetic.main.fragment_popular.*
import kotlinx.android.synthetic.main.fragment_upcoming.*

import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward

    class UpcomingFragment : Fragment(), UpcomingContract.View, BackButtonListener, RouterProvider {


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


    var upcomingList: ArrayList<MoviesResponse.Result> = ArrayList()
    var page = 1
    var totalPages = 15


    private var mPresenter: UpcomingContract.Presenter? = null
    lateinit var mBinding: FragmentUpcomingBinding
    private var mAdapter: MoviesAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentUpcomingBinding.inflate(inflater)


        // Set Menu
        setHasOptionsMenu(true)

        //initialize presenter
        mPresenter = UpcomingPresenter(this)

        return mBinding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            UpcomingFragment().apply {

            }
    }


    override val router: Router
        get() = MyApp.INSTANCE.cicerone.router

    //Show progress bar
    override fun showLoading() {
        mBinding.rvUpcoming.visibility = View.GONE
        mBinding.progressBarUpcoming.visibility = View.VISIBLE
    }

    //Hide progress bar

    override fun hideLoading() {
        try {
            mBinding.rvUpcoming.visibility = View.VISIBLE
            mBinding.progressBarUpcoming.visibility = View.GONE
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
            upcomingList.clear()

            upcomingList.addAll(movies)

            val layoutManager =
                LinearLayoutManager(requireActivity())

            mBinding.rvUpcoming.isNestedScrollingEnabled = false

            mBinding.rvUpcoming.layoutManager = layoutManager
            mAdapter = MoviesAdapter(
                { result -> mPresenter?.listItemClicked(result) },
                upcomingList, mBinding.rvUpcoming
            )

            mBinding.rvUpcoming.adapter = mAdapter
            mBinding.rvUpcoming.itemAnimator = DefaultItemAnimator()


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

                        upcomingList.add(pojo)
                        mAdapter!!.notifyItemInserted(upcomingList.size - 1)
                        page += 1


                        mPresenter?.onLoadNextPage(page)

                    }

                }
            })

        } else {
            try {

                upcomingList.removeAt(upcomingList.size - 1)
                mAdapter!!.notifyItemRemoved(upcomingList.size)
                upcomingList.addAll(movies)


                mAdapter!!.notifyDataSetChanged()
                mAdapter!!.setLoaded()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        for (i in upcomingList.indices) {
            Log.d("Title $i", upcomingList[i].title)
        }

    }


    //Show information
    override fun showInfo(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    // Delete movie list data from local db

    override fun deleteTable() {


        Coroutines.main {
            movieDao?.deleteUpcomingMovies()
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
        val fragment = childFragmentManager.findFragmentById(R.id.root_popular)
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