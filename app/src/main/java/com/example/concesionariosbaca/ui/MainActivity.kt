package com.example.concesionariosbaca.ui

import android.os.Bundle
import android.widget.PopupMenu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.concesionariosbaca.R
import com.example.concesionariosbaca.databinding.ActivityMainBinding
import com.example.concesionariosbaca.ui.profile.ProfileViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener el NavHostFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Configurar Bottom Navigation con NavController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)

        // Vincular ViewModel
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        // Observar cambios en la sesión del usuario y actualizar el título del menú
        profileViewModel.loggedInUser.observe(this) { user ->
            val menuItem = bottomNavigationView.menu.findItem(R.id.profileFragment)
            menuItem.title = if (user != null) "Perfil" else "Iniciar sesión"
        }

        // Manejar navegación en el botón de perfil
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profileFragment -> {
                    val user = profileViewModel.loggedInUser.value
                    if (user != null) {
                        navController.navigate(R.id.profileFragment)
                    } else {
                        navController.navigate(R.id.auth_nav_graph) // Cambio importante
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
