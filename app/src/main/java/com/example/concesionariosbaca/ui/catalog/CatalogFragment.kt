package com.example.concesionariosbaca.ui.catalog

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
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.concesionariosbaca.R
import com.example.concesionariosbaca.data.entities.CarEntity
import com.example.concesionariosbaca.databinding.FragmentCatalogBinding
import com.example.concesionariosbaca.ui.login.ui.login.LoginViewModel
import com.example.concesionariosbaca.ui.profile.ProfileViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Fragmento que muestra la lista de coches disponibles en el catálogo.
 */
@AndroidEntryPoint
class CatalogFragment : Fragment() {

    private lateinit var binding: FragmentCatalogBinding

    /** ViewModel para manejar los datos del catálogo. */
    private val catalogViewModel: CatalogViewModel by viewModels()

    /** ViewModel del perfil (no utilizado directamente aquí). */
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adapter para la lista de coches
        val carAdapter = CarAdapter(requireContext()) { car ->
            val action = CatalogFragmentDirections.actionCatalogFragmentToCarDetailsFragment(car.id)
            findNavController().navigate(action)
        }

        val recyclerView = binding.catalogList
        val emptyState = binding.emptyState
        val menuButton: MaterialButton = view.findViewById(R.id.menu_button)
        val popupMenu = PopupMenu(requireContext(), menuButton)
        val backButton: MaterialButton = view.findViewById(R.id.back_button)

        popupMenu.menuInflater.inflate(R.menu.main_menu, popupMenu.menu)

        recyclerView.apply {
            adapter = carAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // Observa los cambios en el catálogo y actualiza la vista
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                catalogViewModel.catalog.collect { cars ->
                    if (cars.isNullOrEmpty()) {
                        recyclerView.visibility = View.GONE
                        emptyState.visibility = View.VISIBLE
                    } else {
                        recyclerView.visibility = View.VISIBLE
                        emptyState.visibility = View.GONE
                        carAdapter.submitList(cars)
                    }
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
                    findNavController().navigate(R.id.mapsFragment)
                    true
                }
                else -> false
            }
        }
    }
}
