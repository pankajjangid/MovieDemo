package com.demo.themoviedb

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.demo.themoviedb.base.BaseActivity
import com.demo.themoviedb.comman.BackButtonListener
import com.demo.themoviedb.comman.RouterProvider
import com.demo.themoviedb.presenter.MainPresenter
import com.demo.themoviedb.view.contract.MainContract
import com.demo.themoviedb.view.fragments.PopularFragment
import com.demo.themoviedb.view.fragments.TopRatedFragment
import com.demo.themoviedb.view.fragments.UpcomingFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_toolbar.*
import ru.terrakok.cicerone.Router

class MainActivity : BaseActivity(), RouterProvider, MainContract.View {

    private var mPresenter: MainContract.Presenter? = null
    private var mActivity: Activity? = null

    companion object {
        val TAG: String = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Init presenter
        mPresenter = MainPresenter(this)

        //set default fragment
        selectTab(PopularFragment.getNewInstance(), "PopularFragment")


    }

    /**
     * Initialize the bottom navigation
     */
    override fun initViews() {

        mActivity = this
        bottomNavBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_popular -> {
                    selectTab(PopularFragment.getNewInstance(), "PopularFragment")
                    true
                }
                R.id.menu_top_rated -> {
                    selectTab(TopRatedFragment.newInstance(), "TopRatedFragment")

                    true
                }
                R.id.menu_upcoming -> {
                    selectTab(UpcomingFragment.newInstance(), "UpcomingFragment")

                    true
                }

                else -> false
            }
        }
    }



    override fun onResume() {
        super.onResume()
        mPresenter?.onViewCreated()
    }

    override fun getToolbarInstance(): Toolbar? = tool_bar


    private fun selectTab(fragment: Fragment, fmTag: String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.ab_container, fragment,fmTag)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onBackPressed() {
        mPresenter?.onBackPressed()

    }

    override val router: Router = MyApp.INSTANCE.cicerone.router
}