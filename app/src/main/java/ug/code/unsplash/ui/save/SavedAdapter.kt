package ug.code.unsplash.ui.save

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import ug.code.unsplash.R
import ug.code.unsplash.domain.models.photo.Photo
import ug.code.unsplash.ui.widget.AspectRatioImageView
import ug.code.unsplash.util.Constants
import ug.code.unsplash.util.WallpaperClickListener

class SavedAdapter(
    private val listener: WallpaperClickListener.WallpaperClick,
) : RecyclerView.Adapter<SavedAdapter.ViewHolder>() {

    private var data = arrayListOf<Photo>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: AspectRatioImageView = view.findViewById(R.id.card_image_display)
    }

    override fun getItemViewType(position: Int) = R.layout.card_image_display

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        holder.image.setAspectRatio(item.width, item.height)

        Glide.with(holder.image).load(item.urls?.thumb)
            .placeholder(ColorDrawable(Color.parseColor(item.color)))
            .transition(DrawableTransitionOptions.withCrossFade(Constants.CROSS_FADE_DURATION))
            .into(holder.image)
            .clearOnDetach()

        holder.apply {
            image.apply {
                setOnClickListener {
                    listener.onWallpaperClick(
                        item.id!!,
                        item.id_photo_is_local,
                        item.user?.profileImage?.large!!
                    )
                }
            }
        }
    }

    fun getPhotoByPosition(position: Int): Photo {
        return data.get(index = position)
    }

    fun updateItems(updateData: List<Photo>) {
        data.clear()
        data.addAll(updateData)
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size
}