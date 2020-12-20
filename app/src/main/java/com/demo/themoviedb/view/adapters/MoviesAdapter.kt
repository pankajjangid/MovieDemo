package com.demo.themoviedb.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.themoviedb.Constants
import com.demo.themoviedb.R
import com.demo.themoviedb.databinding.EmptyViewBinding
import com.demo.themoviedb.databinding.ItemViewPopularMoviesBinding
import com.demo.themoviedb.databinding.LoadingVerticalBinding
import com.demo.themoviedb.entity.MoviesResponse
import kotlinx.android.synthetic.main.item_view_popular_movies.view.*
import java.util.*
import kotlin.collections.ArrayList

class MoviesAdapter(
    private var listener: (MoviesResponse.Result?) -> Unit,
    private var dataList: List<MoviesResponse.Result>,
    mRecyclerView: RecyclerView,

    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),Filterable {

    var dataFilterList = emptyList<MoviesResponse.Result>()

    companion object {

        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
        private const val EMPTY = 2
    }

    private var isLoading = false
    private val visibleThreshold = 5
    private var lastVisibleItem = 0
    private var totalItemCount = 0

    init {
        dataFilterList = dataList


    }


    private var mOnLoadMoreListener: OnLoadMoreListener? = null

    fun setOnLoadMoreListener(mOnLoadMoreListener: OnLoadMoreListener?) {
        this.mOnLoadMoreListener = mOnLoadMoreListener
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                PopularMoviesVH(
                    ItemViewPopularMoviesBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_TYPE_LOADING -> {
                LoadingViewHolder(
                    LoadingVerticalBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            EMPTY -> {
                EmptyViewHolder(
                    EmptyViewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> EmptyViewHolder(
                EmptyViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        holder.apply {

            when (holder) {
                is PopularMoviesVH -> {
                    //    holder.setIsRecyclable(false)
                    val movies = dataFilterList[position]

                    holder.onBind(movies, position)
                }
                is LoadingViewHolder -> holder.onBind()
                is EmptyViewHolder -> holder.onBind()
            }
        }


    }

    override fun getItemCount(): Int {
        return if (dataFilterList == null) 0 else dataFilterList!!.size
    }

    override fun getItemViewType(position: Int): Int {

        return when {
            dataFilterList[position].id.isNotEmpty() -> {
                VIEW_TYPE_ITEM
            }
            dataFilterList[position].id.isEmpty() -> {
                VIEW_TYPE_LOADING
            }
            else -> {
                EMPTY
            }
        }
    }


    fun setLoaded() {
        isLoading = false
    }

    init {
        val linearLayoutManager = mRecyclerView.layoutManager as LinearLayoutManager?
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = linearLayoutManager?.itemCount!!
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener?.onLoadMore()
                    }
                    isLoading = true
                }
            }
        })
    }


    /**
     * View Holders
     */


    inner class PopularMoviesVH(val mBinding: ItemViewPopularMoviesBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun onBind(movie: MoviesResponse.Result, postion: Int) {
            mBinding.apply {
                // mBinding.callback=callbackListner


                try {
                    setIsRecyclable(false)

                    itemView.release_date_tv.text = movie.release_date
                    itemView.title_tv.text = movie.title
                    itemView.rating_tv.text = movie.vote_average.toString()
                    itemView.vote_count_tv.text = "Total votes ${movie.vote_count}"

                    Glide.with(itemView.context)
                        .load(Constants.IMAGE_BASE_URL + movie.poster_path).placeholder(R.drawable.no_image).into(itemView.poster_img);

                    itemView.rootView.setOnClickListener {
                        listener(movie
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                executePendingBindings()
            }
        }


    }

    inner class LoadingViewHolder(private val mBinding: LoadingVerticalBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun onBind() {
            mBinding.apply {
                executePendingBindings()
            }
        }
    }

    inner class EmptyViewHolder(private val mBinding: EmptyViewBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun onBind() {
            mBinding.apply {
                executePendingBindings()
            }
        }
    }

    /**
     * Filter for searching
     */

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    dataFilterList = dataList
                } else {
                    val resultList = ArrayList<MoviesResponse.Result>()
                    for (row in dataList) {
                        if (row.title.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    dataFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = dataFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                dataFilterList = results?.values as ArrayList<MoviesResponse.Result>
                notifyDataSetChanged()
            }

        }
    }

}