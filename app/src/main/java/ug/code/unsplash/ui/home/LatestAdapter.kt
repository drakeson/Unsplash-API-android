package ug.code.unsplash.ui.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import ug.code.unsplash.R
import ug.code.unsplash.domain.models.photo.Photo
import ug.code.unsplash.util.Constants
import ug.code.unsplash.util.WallpaperClickListener

class LatestAdapter(
    val data: List<Photo>,
    private val listener: WallpaperClickListener.HomeFragment
) : RecyclerView.Adapter<LatestAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wallpaperCardView: ImageView = itemView.findViewById(R.id.wallpaper_card_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_image, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        Glide.with(holder.wallpaperCardView).load(item.urls?.thumb)
            .placeholder(ColorDrawable(Color.parseColor(item.color)))
            .transition(DrawableTransitionOptions.withCrossFade(Constants.CROSS_FADE_DURATION))
            .into(holder.wallpaperCardView)
            .clearOnDetach()
        holder.wallpaperCardView.setOnClickListener {
            listener.onLatestPhoto()
        }
    }

    override fun getItemCount() = data.size
}