package com.example.concesionariosbaca.ui.sellCar

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.concesionariosbaca.data.api.ApiService
import com.example.concesionariosbaca.data.database.CarDao
import com.example.concesionariosbaca.data.entities.CarEntity
import com.example.concesionariosbaca.data.repository.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class SellCarViewModel @Inject constructor(
    private val repository: CarRepository,
    private val apiService: ApiService
) : ViewModel() {

    private val _photo = MutableLiveData<Uri?>()
    val photo: LiveData<Uri?> = _photo

    fun onImageCaptured(uri: Uri?) {
        _photo.value = uri
    }

    suspend fun uploadCar(car: CarEntity) {
        repository.addCar(car)
    }

    suspend fun uploadImage(context: Context): String? {
        val imageUri = _photo.value ?: return null
        val imageFile = convertUriToFile(context, imageUri) ?: return null

        val requestFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("files", imageFile.name, requestFile)

        val response = apiService.uploadImage(body)
        return if (response.isSuccessful) {
            response.body()?.firstOrNull()?.url
        } else {
            null
        }
    }

    private fun convertUriToFile(context: Context, uri: Uri): File? {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "uploaded_image.jpg")
        val outputStream = FileOutputStream(file)

        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()

        return file
    }
}

