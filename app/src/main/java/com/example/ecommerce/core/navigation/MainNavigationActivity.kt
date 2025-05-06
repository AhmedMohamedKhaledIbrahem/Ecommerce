package com.example.ecommerce.core.navigation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.ecommerce.EcommerceApp
import com.example.ecommerce.R
import com.example.ecommerce.core.app.AppLifecycleObserver
import com.example.ecommerce.core.app.AppLifecycleViewModel
import com.example.ecommerce.core.fragment.LoadingDialogFragment
import com.example.ecommerce.core.network.NetworkHelperViewModel
import com.example.ecommerce.databinding.ActivityMainNavigationBinding
import com.example.ecommerce.features.product.presentation.viewmodel.DetectScrollEndViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainNavigationActivity : AppCompatActivity() {
    private lateinit var detectViewModel: DetectScrollEndViewModel
    private lateinit var loadingDialog: LoadingDialogFragment
    private val networkStatusViewModel: NetworkHelperViewModel by viewModels()
    private val appLifecycleViewModel: AppLifecycleViewModel by viewModels()
    private lateinit var appLifecycleObserver: AppLifecycleObserver
    private lateinit var binding: ActivityMainNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainNavigationBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        requestNotificationPermission()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, systemBars.bottom)
            insets
        }
        (application as EcommerceApp).hideSystemUI(this)
        loadingDialog = LoadingDialogFragment.Companion.getInstance(supportFragmentManager)
        appLifecycleViewModel.rootView = binding.root
        appLifecycleViewModel.loadingDialog = loadingDialog
        appLifecycleViewModel.fragmentManager = supportFragmentManager
        appLifecycleObserver = AppLifecycleObserver(
            networkStatusViewModel = networkStatusViewModel,
            appLifecycleViewModel = appLifecycleViewModel
        )
        ProcessLifecycleOwner.Companion.get().lifecycle.addObserver(appLifecycleObserver)
        initView()
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
        detectViewModel = ViewModelProvider(this)[DetectScrollEndViewModel::class.java]
    }

    private fun toolbar(navController: NavController) {
        var nameOfLabel: String?
        setSupportActionBar(binding.profileToolbar)

        NavigationUI.setupActionBarWithNavController(this, navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            nameOfLabel = if (destination.id != R.id.editProfileFragment) {
                destination.label?.toString()?.replace("fragment_", "")?.replace("_", " ")
            } else {
                "Setting"
            }

            binding.profileToolbar.title = ""
            binding.titleToolBarTextView.text = nameOfLabel?.replaceFirstChar { it.uppercase() }
            if (
                destination.id == R.id.editAddressFragment ||
                destination.id == R.id.productDetailsFragment ||
                destination.id == R.id.orderDetailsFragment
            ) {
                binding.bottomNavigationBar.visibility = View.GONE
            } else {
                binding.bottomNavigationBar.visibility = View.VISIBLE
            }

        }


    }

    private fun navigationView(navController: NavController) {
        binding.bottomNavigationBar.setupWithNavController(navController)
        binding.bottomNavigationBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.productFragment -> {
                    navController.apply {
                        navigate(R.id.productFragment)
                        popBackStack(R.id.productFragment, inclusive = false)
                    }
                    true
                }

                R.id.settingFragment -> {
                    navController.apply {
                        navigate(R.id.settingFragment)
                        popBackStack(R.id.settingFragment, inclusive = false)
                    }

                    true
                }

                R.id.cartFragment -> {
                    navController.apply {
                        navigate(R.id.cartFragment)
                        popBackStack(R.id.cartFragment, inclusive = false)
                    }
                    true
                }

                R.id.ordersFragment -> {
                    navController.apply {
                        navigate(R.id.ordersFragment)
                        popBackStack(R.id.ordersFragment, inclusive = false)
                    }
                    true
                }

                else -> false
            }
        }
    }

    private fun checkScrollEnd() {
        detectViewModel.detectScroll.observe(this) {
            if (it) {
                binding.bottomNavigationBar.visibility = View.GONE
            } else {
                binding.bottomNavigationBar.visibility = View.VISIBLE
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED -> {
                    requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }


    override fun onSupportNavigateUp(): Boolean {
        val navController: NavController = findNavController(R.id.fragmentContainer)
        navController.navigateUp()
        return true
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            (application as EcommerceApp).hideSystemUI(this)
        }
    }

}