package com.example.concesionariosbaca.ui.carDetails

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import coil.load
import com.example.concesionariosbaca.R
import com.example.concesionariosbaca.data.CarNotificationWorker
import com.example.concesionariosbaca.databinding.FragmentCarDetailsBinding
import com.example.concesionariosbaca.data.entities.CarEntity
import com.example.concesionariosbaca.ui.profile.ProfileViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
/**
 * Fragmento que muestra los detalles de un coche seleccionado
 * y permite realizar la compra si el usuario está autenticado.
 */
@AndroidEntryPoint
class CarDetailsFragment : Fragment() {

    private lateinit var binding: FragmentCarDetailsBinding
    private val carDetailsViewModel: CarDetailsViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCarDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val carId = CarDetailsFragmentArgs.fromBundle(requireArguments()).carId
        carDetailsViewModel.getCarDetails(carId)

        // Observa los detalles del coche
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                carDetailsViewModel.carDetails.collect { car ->
                    car?.let { displayCarDetails(it) }
                }
            }
        }

        // Configura botones del menú
        val menuButton: MaterialButton = view.findViewById(R.id.menu_button)
        val popupMenu = PopupMenu(requireContext(), menuButton)
        popupMenu.menuInflater.inflate(R.menu.main_menu, popupMenu.menu)

        menuButton.setOnClickListener {
            popupMenu.show()
        }

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.item1 -> {
                    findNavController().navigate(R.id.mapsFragment)
                    true
                }
                else -> false
            }
        }

        val backButton: MaterialButton = view.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    /**
     * Muestra los datos del coche en pantalla y configura el botón de compra si aplica.
     */
    @SuppressLint("SetTextI18n")
    private fun displayCarDetails(car: CarEntity) {
        binding.apply {
            carImage.load(car.pictureUrl) {
                placeholder(R.drawable.car_img_placeholder)
                error(R.drawable.car_img_placeholder)
                crossfade(true)
            }
            carName.text = "${car.brand} ${car.model}"
            carDescription.text = car.description
            carPrice.text = getString(R.string.car_price, car.price)
            carHorsePower.text = getString(R.string.car_horse_power, car.horsePower)
            carDoors.text = getString(R.string.car_doors, car.doors)
            carType.text = getString(R.string.car_type, car.type)
            carColor.text = getString(R.string.car_color, car.color)

            viewLifecycleOwner.lifecycleScope.launch {
                val isLoggedIn = carDetailsViewModel.isUserLoggedIn()
                if (isLoggedIn && car.customerId == null) {
                    buyButton.visibility = View.VISIBLE
                    buyButton.setOnClickListener {
                        carDetailsViewModel.buyCar(
                            car.id,
                            onSuccess = { updatedCar ->
                                Toast.makeText(requireContext(), "Coche comprado con éxito", Toast.LENGTH_SHORT).show()

                                // Lanza notificación con WorkManager
                                val workRequest = OneTimeWorkRequestBuilder<CarNotificationWorker>()
                                    .setInputData(
                                        workDataOf(
                                            "brand" to updatedCar.brand,
                                            "model" to updatedCar.model
                                        )
                                    )
                                    .build()
                                WorkManager.getInstance(requireContext().applicationContext)
                                    .enqueue(workRequest)

                                findNavController().navigate(R.id.action_carDetailsFragment_to_catalogFragment)
                            },
                            onFailure = { errorMessage ->
                                Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                } else {
                    buyButton.visibility = View.GONE
                }
            }
        }
    }
}

