package ug.code.unsplash.util

import com.airbnb.lottie.LottieAnimationView
import ug.code.unsplash.domain.models.photo.Photo

interface WallpaperClickListener {
    interface WallpaperClick {
        fun onWallpaperClick(
            idNetworkPhoto: String, idLocalPhoto: Int, urlImageUser: String
        )
    }

    interface LongClick{
        fun onLongClick(
            fileName: String, linkDownload: String, photo: Photo, lottieAnimationView: LottieAnimationView
        )
    }

    interface HomeFragment {
        fun onLatestPhoto()

        fun onColorClick(
            name: String,
        )

        fun onTopicClick(
            name: String,
            tittle: String,
            totalPhotos: Int
        )
    }
}