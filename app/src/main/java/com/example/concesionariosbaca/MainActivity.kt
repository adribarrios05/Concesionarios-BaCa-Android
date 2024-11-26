package com.example.concesionariosbaca

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.concesionariosbaca.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMainBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        if (savedInstanceState == null){
            val navHostFragment = NavHostFragment.create(R.navigation.main)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, navHostFragment)
                .setPrimaryNavigationFragment(navHostFragment)
                .commit()
        }
    }

}
