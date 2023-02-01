package ug.code.unsplash.ui.image

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import androidx.navigation.navArgs
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import ug.code.unsplash.R
import ug.code.unsplash.databinding.ActivityDetailImageBinding
import ug.code.unsplash.databinding.BottomSheetDownloadBinding
import ug.code.unsplash.databinding.BottomSheetInfoBinding
import ug.code.unsplash.domain.models.photo.Photo
import ug.code.unsplash.util.Status.*
import ug.code.unsplash.util.gone
import ug.code.unsplash.util.invisible
import ug.code.unsplash.util.visible

@AndroidEntryPoint
class DetailImageActivity : AppCompatActivity() {

    private val binding: ActivityDetailImageBinding by viewBinding(CreateMethod.INFLATE)
    private val args: DetailImageActivityArgs by navArgs()
    private val viewModel: DetailImageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.getPhoto(args.idNetworkPhoto, args.idLocalPhoto)
        initObservers()
        hideFragmentComponent()
    }

    private fun hideFragmentComponent() {
        binding.cardBrush.gone()
        binding.cardDown.gone()
        binding.cardInfo.gone()
    }

    private fun showFragmentComponent() {
        binding.cardBrush.visible()
        binding.cardDown.visible()
        binding.cardInfo.visible()
    }

    private fun initObservers() {
        viewModel.photoLiveData.observe(this) {
            when (it.status) {
                SUCCESS -> {
                    it.data?.let { it1 -> setImage(it1) }
                    it.data?.let { it1 -> onClick(it1) }
                }
                ERROR -> {
                    binding.animBar.invisible()
                    binding.cardErrorLayout.apply {
                        root.visible()
                        errorMassageView.text = it.message.toString()
                        repeatButtonView.setOnClickListener {
                            viewModel.getPhoto(args.idNetworkPhoto, args.idLocalPhoto)
                        }
                    }
                }
                LOADING -> {
                    binding.cardErrorLayout.root.gone()
                    binding.animBar.visible()
                }
            }
        }
    }

    private fun setImage(data: Photo) {
        Glide.with(binding.detailImage).load(data.urls?.full)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?, model: Any?, target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    hideFragmentComponent()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?, model: Any?, target: Target<Drawable>?,
                    dataSource: DataSource?, isFirstResource: Boolean
                ): Boolean {
                    binding.cardErrorLayout.root.gone()
                    binding.animBar.gone()
                    showFragmentComponent()
                    return false
                }
            }).into(binding.detailImage)
    }

    @SuppressLint("SetTextI18n")
    private fun onClick(data: Photo) {
        val fileName = data.id + ".jpg"
        binding.cardBrush.setOnClickListener {
            WallpaperManager.getInstance(this).setBitmap(binding.detailImage.drawToBitmap(config = Bitmap.Config.ARGB_8888))
            Toast.makeText(this, getText(R.string.done), Toast.LENGTH_SHORT).show()
        }
        binding.cardDown.setOnClickListener {
            val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogApplyToTheme)
            val bindingBottomSheet = BottomSheetDownloadBinding.inflate(layoutInflater)
            dialog.setContentView(bindingBottomSheet.root)

            bindingBottomSheet.let {
                it.saveToDownloads.setOnClickListener {
                    viewModel.downloadPhoto(fileName, data.links?.download!!,this)
                }
                it.saveToFavorite.setOnClickListener {
                    viewModel.saveToFavorite(data)
                }
                if (viewModel.showButtonDelete(args.idLocalPhoto)) {
                    it.saveToFavorite.gone()
                    it.deleteToFavorites.visible()
                    it.deleteToFavorites.setOnClickListener {
                        viewModel.deleteToFavorites(data)
                    }
                }
            }
            dialog.show()
        }
        binding.cardInfo.setOnClickListener {
            val dialog = BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme)
            val bindingBottomSheet = BottomSheetInfoBinding.inflate(layoutInflater)
            dialog.setContentView(bindingBottomSheet.root)

            bindingBottomSheet.apply {
                Glide.with(imageInfoUser).load(args.urlImageUser).placeholder(R.drawable.ic_launcher_foreground).into(imageInfoUser)
                infoUser.text = data.user?.name ?: getText(R.string.unknown)
                infoLocation.text = "${data.location?.country ?: getText(R.string.unknown)} - ${data.location?.city ?: getText(R.string.unknown)}"
                resolutionInfo.text = "${data.width} x ${data.height}"
                createdAtInfo.text = data.createdAt ?: getText(R.string.unknown)
                colorInfo.text = data.color ?: getText(R.string.unknown)
                downInfo.text = if (data.downloads != null) data.downloads.toString() else getText(R.string.unknown)
                likesInfo.text = if (data.likes != null) data.likes.toString() else getText(R.string.unknown)

                makeCam.text = data.exif?.make ?: getText(R.string.unknown)
                modelCam.text = data.exif?.model ?: getText(R.string.unknown)
                exposureTimeCam.text = if (data.exif?.exposure_time != null) data.exif.exposure_time + "s" else getText(R.string.unknown)
                apertureCam.text = if (data.exif?.aperture != null) "f/" + data.exif.aperture else getText(R.string.unknown)
                focalLengthCam.text = if (data.exif?.focal_length != null) data.exif.focal_length + "mm" else getText(R.string.unknown)
                isoCam.text = data.exif?.iso ?: getText(R.string.unknown)
            }
            dialog.show()
        }
    }
}