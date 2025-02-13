package com.example.concesionariosbaca.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.concesionariosbaca.R
import com.example.concesionariosbaca.databinding.FragmentProfileBinding
import com.example.concesionariosbaca.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.isAuthenticated.observe(viewLifecycleOwner) { isLoggedIn ->
            if (!isLoggedIn && findNavController().currentDestination?.id == R.id.profileFragment) {
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }
        }

        profileViewModel.loggedInUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.usernameEditText.setText(it.username)
                binding.emailEditText.setText(it.email)
            }
        }

        binding.logoutButton.setOnClickListener {
            authViewModel.logout()
        }
    }
}
