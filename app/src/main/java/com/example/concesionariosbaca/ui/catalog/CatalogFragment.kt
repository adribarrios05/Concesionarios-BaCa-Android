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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.concesionariosbaca.R
import com.example.concesionariosbaca.databinding.FragmentCatalogBinding
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CatalogFragment : Fragment() {

    private lateinit var binding: FragmentCatalogBinding
    private val catalogViewModel: CatalogViewModel by viewModels()

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

        val carAdapter = CarAdapter(
            requireContext(),
            onDetailsClick = TODO()
        )
        val recyclerView = binding.catalogList
        val emptyState = binding.emptyState
        val menuButton: MaterialButton = view.findViewById(R.id.menu_button)
        val popupMenu = PopupMenu(requireContext(), menuButton)
        popupMenu.inflate(R.menu.main_menu)

        recyclerView.apply {
            adapter = carAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
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
}