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
import dagger.hilt.android.AndroidEntryPoint
import com.example.concesionariosbaca.ui.login.data.Result
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val user = profileViewModel.loggedInUser.value
        if(user == null){
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            return null
        }

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.loggedInUser.observe(viewLifecycleOwner) { user ->
            if (user == null) {
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            } else {
                binding.usernameEditText.setText(user.username)
                binding.emailEditText.setText(user.email)
            }
        }

        binding.saveButton.setOnClickListener {
            val updatedUsername = binding.usernameEditText.text.toString()
            val updatedEmail = binding.emailEditText.text.toString()

            profileViewModel.updateUserProfile(updatedUsername, updatedEmail).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Success -> {
                        Toast.makeText(context, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                    }
                    is Result.Error -> {
                        Toast.makeText(context, "Error al actualizar el perfil: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.logoutButton.setOnClickListener {
            lifecycleScope.launch {
                profileViewModel.logout()
                Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }
        }
    }
}
