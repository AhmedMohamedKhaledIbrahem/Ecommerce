package com.example.ecommerce

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.ecommerce.core.app.AppLifecycleObserver
import com.example.ecommerce.core.app.AppLifecycleViewModel
import com.example.ecommerce.core.fragment.LoadingDialogFragment
import com.example.ecommerce.core.network.NetworkHelperViewModel
import com.example.ecommerce.features.product.presentation.viewmodel.DetectScrollEndViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainNavigationActivity : AppCompatActivity() {
    private lateinit var titleToolBarTextView: TextView
    private lateinit var profileToolbar: Toolbar
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var detectViewModel: DetectScrollEndViewModel
    private val loadingDialog by lazy {
        LoadingDialogFragment().getInstance(fragmentManager = supportFragmentManager)
    }

    private val networkStatusViewModel: NetworkHelperViewModel by viewModels()
    private val appLifecycleViewModel: AppLifecycleViewModel by viewModels()
    private lateinit var appLifecycleObserver: AppLifecycleObserver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_navigation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, systemBars.bottom)
            insets
        }
        val rootView = findViewById<View>(android.R.id.content)

        (application as EcommerceApp).hideSystemUI(this)
        initView()
        appLifecycleViewModel.rootView = rootView
        appLifecycleViewModel.loadingDialog = loadingDialog
        appLifecycleViewModel.fragmentManager = supportFragmentManager
        appLifecycleObserver = AppLifecycleObserver(
            networkStatusViewModel = networkStatusViewModel,
            appLifecycleViewModel = appLifecycleViewModel
        )
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)

    }

    override fun onResume() {
        super.onResume()
        val navController: NavController = findNavController(R.id.fragmentContainer)
        navigationView(navController)
        toolbar(navController)
        checkScrollEnd()

    }

    override fun onRestart() {
        super.onRestart()
        (application as EcommerceApp).hideSystemUI(this)
    }

    private fun initView() {
        profileToolbar = findViewById(R.id.profileToolbar)
        bottomNavigationView = findViewById(R.id.bottomNavigationBar)
        titleToolBarTextView = findViewById(R.id.titleToolBarTextView)
        detectViewModel = ViewModelProvider(this)[DetectScrollEndViewModel::class.java]

    }

    private fun toolbar(navController: NavController) {
        var nameOfLabel: String? = null
        setSupportActionBar(profileToolbar)

        NavigationUI.setupActionBarWithNavController(this, navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            nameOfLabel = if (destination.id != R.id.editProfileFragment) {
                destination.label?.toString()?.replace("fragment_", "")?.replace("_", " ")
            } else {
                "Setting"
            }

            profileToolbar.title = ""
            titleToolBarTextView.text = nameOfLabel?.replaceFirstChar { it.uppercase() }
            if (destination.id == R.id.editAddressFragment || destination.id == R.id.productDetailsFragment) {
                Log.e("test", "yes")
                bottomNavigationView.visibility = android.view.View.GONE
            } else {
                bottomNavigationView.visibility = android.view.View.VISIBLE
            }

        }


    }

    private fun navigationView(navController: NavController) {
        bottomNavigationView.setupWithNavController(navController)
    }

    private fun checkScrollEnd() {
        detectViewModel.detectScroll.observe(this) {
            if (it) {
                bottomNavigationView.visibility = View.GONE
            } else {
                bottomNavigationView.visibility = View.VISIBLE
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController: NavController = findNavController(R.id.fragmentContainer)
        navController.navigateUp()
        return true
    }


}


