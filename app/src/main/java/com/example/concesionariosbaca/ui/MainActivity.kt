package com.example.concesionariosbaca.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.concesionariosbaca.R
import com.example.concesionariosbaca.databinding.ActivityMainBinding
import com.example.concesionariosbaca.ui.profile.ProfileViewModel
import com.example.concesionariosbaca.data.repository.AuthRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

/**
 * Actividad principal de la aplicación. Controla la navegación global
 * y gestiona el comportamiento del `BottomNavigationView`.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)

        // Cambia el nombre del item según el estado de sesión
        profileViewModel.loggedInUser.observe(this) { user ->
            val menuItem = bottomNavigationView.menu.findItem(R.id.profileFragment)
            menuItem.title = if (user != null) "Perfil" else "Iniciar sesión"
        }

        // Navegación personalizada para botón de perfil
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profileFragment -> {
                    val isLoggedIn = profileViewModel.loggedInUser.value != null
                    if (isLoggedIn) {
                        navController.navigate(R.id.profileFragment)
                    } else {
                        navController.navigate(R.id.loginFragment)
                    }
                    true
                }
                else -> {
                    navController.navigate(item.itemId)
                    true
                }
            }
        }
    }
}
