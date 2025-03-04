package com.example.concesionariosbaca.ui.sellCar

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.concesionariosbaca.data.entities.CarEntity
import com.example.concesionariosbaca.databinding.FragmentSellCarBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import android.Manifest
import android.util.Log
import java.util.UUID

@AndroidEntryPoint
class SellCarFragment : Fragment() {

    private var _binding: FragmentSellCarBinding? = null
    private val binding get() = _binding!!
    private val sellCarViewModel: SellCarViewModel by viewModels()
    private var imageUri: Uri? = null
    private lateinit var cameraController: LifecycleCameraController

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            sellCarViewModel.onImageCaptured(it)
            binding.carImageView.setImageURI(it)
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            sellCarViewModel.onImageCaptured(imageUri)
            binding.carImageView.setImageURI(imageUri)
        }
    }

    private val cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            openCamera()
        } else {
            Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            openCamera()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellCarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraController = LifecycleCameraController(requireContext())
        cameraController.bindToLifecycle(viewLifecycleOwner)
        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        binding.uploadFromGalleryButton.setOnClickListener { openGallery() }
        binding.openCameraButton.setOnClickListener { checkCameraPermission() }
        binding.submitButton.setOnClickListener { submitCar() }

        sellCarViewModel.photo.observe(viewLifecycleOwner) { uri ->
            if(uri != null) {
                binding.carImageView.setImageURI(uri)
            }
        }
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun openCamera() {
        val photoFile = File(requireContext().cacheDir, "temp_image.jpg")
        imageUri = FileProvider.getUriForFile(requireContext(), "com.example.concesionariosbaca.fileprovider", photoFile)
        cameraLauncher.launch(imageUri)
    }

    private fun submitCar() {
        lifecycleScope.launch {
            try{
                val imageFile = sellCarViewModel.getImageFile(requireContext())

                val car = CarEntity(
                    id = UUID.randomUUID().toString(),
                    brand = binding.brandEditText.text.toString(),
                    model = binding.modelEditText.text.toString(),
                    horsePower = binding.horsePowerEditText.text.toString().toInt(),
                    description = binding.descriptionEditText.text.toString(),
                    color = binding.colorEditText.text.toString(),
                    type = binding.typeEditText.text.toString(),
                    price = binding.priceEditText.text.toString().toDouble(),
                    plate = binding.plateEditText.text.toString(),
                    pictureUrl = null,
                    doors = binding.doorsEditText.text.toString().toInt(),
                    customerId = null
                )

                sellCarViewModel.uploadCar(car, imageFile)
                Toast.makeText(requireContext(), "Coche subido con éxito", Toast.LENGTH_SHORT).show()
            } catch (e: Exception){
                Log.e("submitCar: ", "Error al subir el coche: $e")
                Toast.makeText(requireContext(), "No se pudo subir el coche. Inténtelo de nuevo.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
