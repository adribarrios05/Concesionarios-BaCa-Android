package com.example.concesionariosbaca.ui.login.ui.login

import androidx.lifecycle.Observer
import android.os.Bundle
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
import com.example.concesionariosbaca.R
import com.example.concesionariosbaca.databinding.FragmentLoginBinding
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.example.concesionariosbaca.ui.login.data.Result


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (loginViewModel.isLoggedIn()) {
            findNavController().navigate(R.id.action_loginFragment_to_catalogFragment)
            return
        }

        val menuButton: MaterialButton = view.findViewById(R.id.menu_button)
        val popupMenu = PopupMenu(requireContext(), menuButton)
        val backButton: MaterialButton = view.findViewById(R.id.back_button)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.isLoggedIn.collect { loggedIn ->
                    if (loggedIn) {
                        findNavController().navigate(R.id.action_loginFragment_to_catalogFragment)
                    }
                }
            }
        }

        binding.login.setOnClickListener {
            val username = binding.usernameOrEmail.text.toString()
            val password = binding.password.text.toString()

            lifecycleScope.launch {
                when (val result = loginViewModel.login(username, password)) {
                    is Result.Success -> {
                        Toast.makeText(context, "Bienvenido ${result.data.username}", Toast.LENGTH_SHORT).show()

                        val navController = findNavController()
                        if (navController.currentDestination?.id == R.id.loginFragment) {
                            navController.navigate(R.id.action_loginFragment_to_catalogFragment)
                        }
                    }
                    is Result.Error -> {
                        Toast.makeText(context, "Error: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        loginViewModel.loginResult.observe(viewLifecycleOwner, Observer { loginResult ->
            loginResult?.let {
                if (it.success != null) {
                    updateUiWithUser(it.success)
                } else if (it.error != null) {
                    showLoginFailed(it.error)
                }
            }
        })

        backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.registerText.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
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



    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.username
        Toast.makeText(requireContext(), welcome, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(errorString: Int) {
        Toast.makeText(requireContext(), getString(errorString), Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
