package com.example.concesionariosbaca.ui.login.ui.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.concesionariosbaca.R
import com.example.concesionariosbaca.databinding.FragmentLoginBinding
import com.example.concesionariosbaca.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.concesionariosbaca.ui.login.data.Result
import com.google.android.material.button.MaterialButton


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*authViewModel.isAuthenticated.observe(viewLifecycleOwner) { isLoggedIn ->
            if (isLoggedIn && findNavController().currentDestination?.id == R.id.loginFragment) {
                findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
            }
        }*/
        val backButton: MaterialButton = view.findViewById(R.id.back_button)


        binding.login.setOnClickListener {
            val username = binding.usernameOrEmail.text.toString()
            val password = binding.password.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                Log.d("logginBtn", "Datos pasados al login")
                authViewModel.login(username, password)
            } else {
                Toast.makeText(context, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Observar el resultado del login
        loginViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                is Result.Error -> Toast.makeText(context, "Error: ${result.exception.message}", Toast.LENGTH_SHORT).show()
            }
        }

        binding.registerText.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
