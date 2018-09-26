package com.gnz.getamovie.features.nowplaying.pagination

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.gnz.getamovie.R
import com.gnz.getamovie.data.movies.MovieItem
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.now_playing_view_holder.view.*


class NowPlayingAdapter(private val glide: RequestManager) : PagedListAdapter<MovieItem, NowPlayingLoadingViewHolder>(ItemCallback) {

    private val clickSubject = PublishSubject.create<MovieDetails>()
    private var isLoading: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NowPlayingLoadingViewHolder =
            when (viewType) {
                TYPE_ITEM -> NowPlayingViewHolder(parent)
                else -> LoadingViewHolder(parent)
            }

    override fun onBindViewHolder(holder: NowPlayingLoadingViewHolder, position: Int) {
        if (holder is NowPlayingViewHolder) {
            val item = getItem(position)
            item?.poster_path?.let { path ->
                glide.load(path)
                        .apply(RequestOptions.fitCenterTransform())
                        .apply(RequestOptions().error(R.drawable.default_image))
                        .into(holder.poster)
                holder.poster.setOnClickListener {
                    clickSubject.onNext(MovieDetails(item.title, item.overview, path))
                }
            }
        }
    }

    fun observeMovieClick(): Observable<MovieDetails> = clickSubject

    fun shouldShowLoading(should: Boolean) {
        if (should) {
            notifyItemInserted(itemCount)
        }
        isLoading = should
    }

    override fun getItemViewType(position: Int): Int =
            if (isLoading && isLastItemPosition(position)) TYPE_PROGRESS
            else TYPE_ITEM

    fun isLastItemPosition(position: Int): Boolean = position == (itemCount - 1)

    private object ItemCallback : DiffUtil.ItemCallback<MovieItem>() {

        override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean =
                oldItem == newItem

        override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean =
                oldItem.id != newItem.id
    }

    companion object {
        private const val TYPE_PROGRESS = 0
        private const val TYPE_ITEM = 1
    }
}

data class MovieDetails(val title: String, val details: String, val posterPath: String)

class NowPlayingViewHolder(parent: ViewGroup) : NowPlayingLoadingViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.now_playing_view_holder, parent, false)) {
    val poster = itemView.posterImageView
}

class LoadingViewHolder(parent: ViewGroup) : NowPlayingLoadingViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.loading_view_holder, parent, false))

abstract class NowPlayingLoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)