package com.example.ecommerce

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainNavigationActivity : AppCompatActivity() {
    private lateinit var titleToolBarTextView: TextView
    private lateinit var profileToolbar: Toolbar
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_navigation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, systemBars.bottom)
            insets
        }

        (application as EcommerceApp).hideSystemUI(this)
        initView()

    }

    override fun onResume() {
        super.onResume()
        val navController: NavController = findNavController(R.id.fragmentContainer)
        navigationView(navController)
        toolbar(navController)
    }

    override fun onRestart() {
        super.onRestart()
        (application as EcommerceApp).hideSystemUI(this)
    }

    private fun initView() {
        profileToolbar = findViewById(R.id.profileToolbar)
        bottomNavigationView = findViewById(R.id.bottomNavigationBar)
        titleToolBarTextView = findViewById(R.id.titleToolBarTextView)

    }

    private fun toolbar(navController: NavController) {
        var nameOfLabel: String? = null
        setSupportActionBar(profileToolbar)
        NavigationUI.setupActionBarWithNavController(this, navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            nameOfLabel = if (destination.id != R.id.editProfileFragment) {
                destination.label?.toString()?.replace("fragment_", "")?.replace("_"," ")
            } else {
                "Setting"
            }

            profileToolbar.title = ""
            titleToolBarTextView.text = nameOfLabel?.replaceFirstChar { it.uppercase() }
            if (destination.id == R.id.editAddressFragment){
                bottomNavigationView.visibility = android.view.View.GONE
            }else{
                bottomNavigationView.visibility = android.view.View.VISIBLE
            }
        }


    }

    private fun navigationView(navController: NavController) {
        bottomNavigationView.setupWithNavController(navController)
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController: NavController = findNavController(R.id.fragmentContainer)
        navController.navigateUp()
        return true
    }


}


