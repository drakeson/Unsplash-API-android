package ug.code.unsplash.ui.image

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ug.code.unsplash.domain.models.photo.Photo
import ug.code.unsplash.domain.usecase.detailI.DeletePhotoUseCase
import ug.code.unsplash.domain.usecase.detailI.GetPhotoFromApiByIDUseCase
import ug.code.unsplash.domain.usecase.detailI.GetPhotoFromFavoriteByIDUseCase
import ug.code.unsplash.domain.usecase.detailI.InsertPhotoUseCase
import ug.code.unsplash.util.Constants.Companion.CLIENT_ID
import ug.code.unsplash.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailImageViewModel @Inject constructor(
    private val getPhotoFromFavoriteByIDUseCase: GetPhotoFromFavoriteByIDUseCase,
    private val getPhotoFromApiByIDUseCase: GetPhotoFromApiByIDUseCase,
    private val insertPhotoUseCase: InsertPhotoUseCase,
    private val deletePhotoUseCase: DeletePhotoUseCase
) : ViewModel() {

    private val photoMutableLiveData = MutableLiveData<Resource<Photo>>()
    val photoLiveData: LiveData<Resource<Photo>> = photoMutableLiveData

    fun getPhoto(id: String, idLocalPhoto: Int) {
        photoMutableLiveData.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val photoFromDataBASE = getPhotoFromFavoriteByIDUseCase.invoke(idLocalPhoto)
                if (photoFromDataBASE != null) {
                    photoMutableLiveData.postValue(Resource.success(photoFromDataBASE))
                } else {
                    val photoDataAPI = getPhotoFromApiByIDUseCase.invoke(id, CLIENT_ID)
                    photoMutableLiveData.postValue(Resource.success(photoDataAPI))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                photoMutableLiveData.postValue(Resource.error(e.message ?: "none"))
            }
        }
    }

    fun downloadPhoto(fileName: String, linkDownload: String, requireActivity: FragmentActivity) {
        val dm: DownloadManager =
            requireActivity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(linkDownload))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
        request.setTitle(fileName)
        request.setDescription("Wait a second...")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_PICTURES,
            "/Unsplash/$fileName"
        )
        dm.enqueue(request)
    }

    fun saveToFavorite(photo: Photo) = viewModelScope.launch {
        insertPhotoUseCase.invoke(photo)
    }

    fun deleteToFavorites(photo: Photo) = viewModelScope.launch {
        deletePhotoUseCase.invoke(photo)
    }

    fun showButtonDelete(idLocalPhoto: Int?): Boolean {
        return idLocalPhoto != 0
    }
}