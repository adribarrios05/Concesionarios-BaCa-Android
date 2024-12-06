package com.example.concesionariosbaca.ui.carDetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.concesionariosbaca.R
import com.example.concesionariosbaca.databinding.FragmentCarDetailsBinding
import com.example.concesionariosbaca.model.entities.CarEntity
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CarDetailsFragment : Fragment() {

    private lateinit var binding: FragmentCarDetailsBinding
    private val carDetailsViewModel: CarDetailsViewModel by viewModels()

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

        val menuButton: MaterialButton = view.findViewById(R.id.menu_button)
        val popupMenu = PopupMenu(requireContext(), menuButton)
        val backButton: MaterialButton = view.findViewById(R.id.back_button)
        val carId = arguments?.getString("carId")
        if (carId != null) {
            carDetailsViewModel.getCarDetails(carId)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                carDetailsViewModel.carDetails.collect { car ->
                    car?.let { displayCarDetails(it) }
                }
            }
        }

        backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        menuButton.setOnClickListener {
            popupMenu.show()
        }

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.item1 -> {

                    true
                }
                R.id.item2 -> {

                    true
                }
                R.id.item3 -> {

                    true
                }
                else -> false
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayCarDetails(car: CarEntity) {
        binding.apply {
            carImage.load(car.pictureUrl) {
                placeholder(R.color.dark_red)
                error(R.color.blue)
                crossfade(true)
            }
            carName.text = "${car.brand} ${car.model}"
            carDescription.text = car.description
            carPrice.text = getString(R.string.car_price, car.price)
            carHorsePower.text = getString(R.string.car_horse_power, car.horsePower)
            carDoors.text = getString(R.string.car_doors, car.doors)
            carType.text = getString(R.string.car_type, car.type)
            carColor.text = getString(R.string.car_color, car.color)
        }
    }
}
