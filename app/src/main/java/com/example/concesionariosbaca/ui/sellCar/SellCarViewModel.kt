package com.example.concesionariosbaca.ui.sellCar

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.concesionariosbaca.data.api.ApiService
import com.example.concesionariosbaca.data.database.CarDao
import com.example.concesionariosbaca.data.entities.CarEntity
import com.example.concesionariosbaca.data.repository.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SellCarViewModel @Inject constructor(
    private val repository: CarRepository
) : ViewModel() {

    private val _photo = MutableLiveData<Uri?>()
    val photo: LiveData<Uri?> = _photo

    fun onImageCaptured(uri: Uri?) {
        _photo.value = uri
    }

    suspend fun uploadCar(car: CarEntity) {
        repository.addCar(car)
    }
}

