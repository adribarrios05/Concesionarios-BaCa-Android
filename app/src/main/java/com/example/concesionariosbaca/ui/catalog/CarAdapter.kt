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
import com.example.concesionariosbaca.data.entities.CarEntity


/**
 * Adaptador para mostrar una lista de coches en el RecyclerView del catálogo.
 *
 * @param context Contexto necesario para inflar vistas.
 * @param onDetailsClick Callback que se ejecuta al pulsar el botón "Ver detalles".
 */
class CarAdapter(
    private val context: Context,
    private val onDetailsClick: (CarEntity) -> Unit
) : ListAdapter<CarEntity, CarAdapter.CarViewHolder>(CarDiffCallback()) {

    /**
     * ViewHolder para mostrar los datos de un coche.
     */
    inner class CarViewHolder(private val binding: CatalogItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        /**
         * Enlaza los datos del coche con las vistas.
         *
         * @param car El coche a mostrar.
         */
        @SuppressLint("SetTextI18n")
        fun bind(car: CarEntity) {
            binding.apply {
                carName.text = "${car.brand} ${car.model}"

                carImage.load(car.pictureUrl ?: R.color.blue) {
                    crossfade(true)
                    placeholder(R.drawable.car_img_placeholder)
                    error(R.drawable.car_img_placeholder)
                }

                btnViewDetails.setOnClickListener {
                    onDetailsClick(car)
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

/**
 * Utiliza DiffUtil para optimizar el rendimiento del RecyclerView.
 */
class CarDiffCallback : DiffUtil.ItemCallback<CarEntity>() {
    override fun areItemsTheSame(oldItem: CarEntity, newItem: CarEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CarEntity, newItem: CarEntity): Boolean {
        return oldItem == newItem
    }
}
