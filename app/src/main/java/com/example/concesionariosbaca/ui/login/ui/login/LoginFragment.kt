package com.example.concesionariosbaca.ui.login.ui.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.concesionariosbaca.R
import com.example.concesionariosbaca.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            if (loginViewModel.isUserLoggedIn()) {
                findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton: MaterialButton = view.findViewById(R.id.back_button)

        binding.login.setOnClickListener {
            val username = binding.usernameOrEmail.text.toString()
            val password = binding.password.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                Log.d("logginBtn", "Datos pasados al login")
                viewLifecycleOwner.lifecycleScope.launch {
                    val success = loginViewModel.login(username, password)
                    if (success) {
                        findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
                    } else {
                        Toast.makeText(context, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.registerText.setOnClickListener {
            findNavController().navigate(R.id   .action_loginFragment_to_registerFragment)
        }

        backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
