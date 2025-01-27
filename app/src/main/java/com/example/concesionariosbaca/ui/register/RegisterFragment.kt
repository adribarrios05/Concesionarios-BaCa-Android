package com.example.concesionariosbaca.ui.register

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.concesionariosbaca.R
import com.example.concesionariosbaca.databinding.FragmentRegisterBinding
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuButton: MaterialButton = view.findViewById(R.id.menu_button)
        val popupMenu = PopupMenu(requireContext(), menuButton)
        val backButton: MaterialButton = view.findViewById(R.id.back_button)
        val ageEditText: EditText = view.findViewById(R.id.ageEditText)

        ageEditText.setOnClickListener{
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val date = "${selectedMonth + 1}/$selectedDay/$selectedYear"
                    ageEditText.setText(date)
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val username = binding.usernameEditText.text.toString()
            val name = binding.nameEditText.text.toString()
            val surname = binding.surnameEditText.text.toString()
            val dni = binding.dniEditText.text.toString()
            val phone = binding.phoneEditText.text.toString()
            val age = binding.ageEditText.text.toString()

            if (validateInput(email, password, username, name, surname, dni, phone, age)) {
                lifecycleScope.launch {
                    register(email, password, username, name, surname, dni, phone, age)
                }
            }
        }

        backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.loginText.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        menuButton.setOnClickListener {
            popupMenu.show()
        }

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.item1 -> {
                    // Acción para item1
                    true
                }
                R.id.item2 -> {
                    findNavController().navigate(R.id.catalogFragment)
                    true
                }
                R.id.item3 -> {
                    findNavController().navigate(R.id.loginFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun validateInput(
        email: String,
        password: String,
        username: String,
        name: String,
        surname: String,
        dni: String,
        phone: String,
        age: String
    ): Boolean {
        return when {
            email.isEmpty() || password.isEmpty() || username.isEmpty() ||
                    name.isEmpty() || surname.isEmpty() || dni.isEmpty() ||
                    phone.isEmpty() || age.isEmpty() -> {
                Toast.makeText(context, "Todos los campos son obligatorios", Toast.LENGTH_LONG).show()
                false
            }
            password.length < 8 -> {
                Toast.makeText(context, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_LONG).show()
                false
            }
            else -> true
        }
    }

    private suspend fun register(
        email: String,
        password: String,
        username: String,
        name: String,
        surname: String,
        dni: String,
        phone: String,
        age: String
    ) {
        try {
            val result = registerViewModel.register(email, password, username, name, surname, dni, phone, age)
            if (result.isSuccess) {
                Toast.makeText(context, "Cliente registrado exitosamente", Toast.LENGTH_SHORT).show()
                navigateToLogin()
            } else {
                val error = result.exceptionOrNull()?.message ?: "Error desconocido"
                Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
                Log.e("Error: ", error)
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error inesperado: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
