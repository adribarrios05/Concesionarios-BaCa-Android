package com.example.concesionariosbaca.ui.profile

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.concesionariosbaca.R
import com.example.concesionariosbaca.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 * Fragmento que muestra la información del perfil del usuario
 * y permite cerrar sesión o cambiar el idioma de la app.
 */
@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loadLocale(requireContext())
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.loggedInUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.usernameEditText.setText(it.username)
                binding.emailEditText.setText(it.email)
            }
        }

        // Cambiar idioma (es/en)
        binding.languageSwitch.isChecked = getSavedLanguage(requireContext()) == "en"

        binding.languageSwitch.setOnCheckedChangeListener { _, isChecked ->
            val lang = if (isChecked) "en" else "es"
            setLocale(requireContext(), lang)
            requireActivity().recreate()
        }

        binding.logoutButton.setOnClickListener {
            profileViewModel.logout()
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }

    /**
     * Establece el idioma de la aplicación y guarda la configuración.
     */
    private fun setLocale(context: Context, lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
            .edit()
            .putString("App_Lang", lang)
            .apply()
    }

    /**
     * Carga el idioma previamente guardado al iniciar la app.
     */
    private fun loadLocale(context: Context) {
        val lang = getSavedLanguage(context)
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    /**
     * Devuelve el idioma guardado en SharedPreferences.
     */
    private fun getSavedLanguage(context: Context): String {
        return context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
            .getString("App_Lang", "es") ?: "es"
    }
}
