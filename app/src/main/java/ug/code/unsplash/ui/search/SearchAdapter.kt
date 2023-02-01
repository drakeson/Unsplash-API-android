package ug.code.unsplash.ui.search

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import ug.code.unsplash.R
import ug.code.unsplash.domain.models.photo.Photo
import ug.code.unsplash.ui.widget.AspectRatioImageView
import ug.code.unsplash.util.Constants.Companion.CROSS_FADE_DURATION
import ug.code.unsplash.util.WallpaperClickListener

class SearchAdapter(
    private val listenerWallpaperClick: WallpaperClickListener.WallpaperClick,
    private val listenerLongClick: WallpaperClickListener.LongClick
) : PagingDataAdapter<Photo, SearchAdapter.ViewHolder>(DiffUtilCallBack) {

    override fun getItemViewType(position: Int) = R.layout.card_image_display

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(viewType, parent, false),
            listenerWallpaperClick,
            listenerLongClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    object DiffUtilCallBack : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(
        view: View,
        private val listenerWallpaperClick: WallpaperClickListener.WallpaperClick,
        private val listenerLongClick: WallpaperClickListener.LongClick
    ) : RecyclerView.ViewHolder(view) {

        private val image: AspectRatioImageView = view.findViewById(R.id.card_image_display)
        private val downloadAnim: LottieAnimationView = view.findViewById(R.id.download_anim_view)

        fun bind(item: Photo) {
            image.setAspectRatio(item.width, item.height)

            Glide.with(image).load(item.urls?.thumb)
                .placeholder(ColorDrawable(Color.parseColor(item.color)))
                .transition(DrawableTransitionOptions.withCrossFade(CROSS_FADE_DURATION))
                .into(image).clearOnDetach()

            image.apply {
                setOnClickListener {
                    listenerWallpaperClick.onWallpaperClick(
                        item.id!!,
                        item.id_photo_is_local,
                        item.user?.profileImage?.large!!
                    )
                }
                setOnLongClickListener {
                    listenerLongClick.onLongClick(
                        item.id!!,
                        item.links?.download!!,
                        item,
                        downloadAnim
                    )
                    false
                }
            }
        }
    }
}