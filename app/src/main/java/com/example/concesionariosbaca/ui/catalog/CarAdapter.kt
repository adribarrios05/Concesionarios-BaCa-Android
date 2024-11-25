package com.example.concesionariosbaca.ui.catalog

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.concesionariosbaca.R
import com.example.concesionariosbaca.databinding.CatalogItemBinding
import com.example.concesionariosbaca.model.entities.CarEntity


class CarAdapter(private val context: Context):
    ListAdapter<CarEntity, CarAdapter.CarViewHolder>(CarDiffCallback()) {



    inner class CarViewHolder(private val binding: CatalogItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(car: CarEntity) {
            binding.apply {
                carName.text = "${car.brand} ${car.model}"

                carImage.load(car.imageUrl) {
                    placeholder(R.color.dark_red)
                    error(R.color.black)
                    crossfade(true)
                }

                btnViewDetails.setOnClickListener {
                    // Implementa la navegación o acción al pulsar "Ver más"
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val binding = CatalogItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarAdapter.CarViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CarDiffCallback : DiffUtil.ItemCallback<CarEntity>() {
    override fun areItemsTheSame(oldItem: CarEntity, newItem: CarEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CarEntity, newItem: CarEntity): Boolean {
        return oldItem == newItem
    }
}