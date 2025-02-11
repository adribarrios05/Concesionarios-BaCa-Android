package com.example.concesionariosbaca.ui.login.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.concesionariosbaca.R
import com.example.concesionariosbaca.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import com.example.concesionariosbaca.ui.login.data.Result


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel.observeAuthState(viewLifecycleOwner) { isLoggedIn ->
            val currentDestination = findNavController().currentDestination?.id

            if (isLoggedIn && isAdded && currentDestination == R.id.loginFragment) {
                findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
            }
        }

        binding.login.setOnClickListener {
            val username = binding.usernameOrEmail.text.toString()
            val password = binding.password.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginViewModel.login(username, password)
                binding.loading.visibility = View.VISIBLE
            } else {
                Toast.makeText(context, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Observar el resultado del login
        loginViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            binding.loading.visibility = View.GONE // Oculta el ProgressBar al recibir respuesta
            when (result) {
                is Result.Success -> {
                    Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                }
                is Result.Error -> {
                    Toast.makeText(context, "Error: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.registerText.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }
}
