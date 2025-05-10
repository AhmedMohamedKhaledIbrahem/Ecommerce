package com.example.ecommerce

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.ecommerce.core.app.AppLifecycleObserver
import com.example.ecommerce.core.app.AppLifecycleViewModel
import com.example.ecommerce.core.fragment.LoadingDialogFragment
import com.example.ecommerce.core.manager.token.TokenManager
import com.example.ecommerce.core.navigation.MainNavigationViewModel
import com.example.ecommerce.core.network.NetworkHelperViewModel
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.core.utils.PreferencesUtils
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.databinding.ActivityMainNavigationBinding
import com.example.ecommerce.features.product.presentation.viewmodel.DetectScrollEndViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainNavigationActivity : AppCompatActivity() {
    private lateinit var detectViewModel: DetectScrollEndViewModel
    private lateinit var loadingDialog: LoadingDialogFragment
    private val mainNavigationViewModel: MainNavigationViewModel by viewModels()
    private val networkStatusViewModel: NetworkHelperViewModel by viewModels()
    private val appLifecycleViewModel: AppLifecycleViewModel by viewModels()
    private lateinit var appLifecycleObserver: AppLifecycleObserver
    private lateinit var binding: ActivityMainNavigationBinding

    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var shardPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainNavigationBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        requestNotificationPermission()
        windowSystem()
        defaultNightMode()
        initView()
        appLifecycleObserver()
        startDestinationGraph()
        navigationEvent()
    }

    override fun onResume() {
        super.onResume()
        val navController: NavController = findNavController(R.id.fragmentContainer)
        navigationView(navController)
        toolbar(navController)
        navigationChangeListener(navController)
        checkScrollEnd()
    }

    override fun onRestart() {
        super.onRestart()
        (application as EcommerceApp).hideSystemUI(this)
    }

    override fun attachBaseContext(newBase: Context?) {
        val language = PreferencesUtils.languageCode ?: Locale.getDefault().language
        super.attachBaseContext(ContextWrapper(newBase?.setAppLocal(language)))
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

    private fun initView() {
        loadingDialog = LoadingDialogFragment.Companion.getInstance(supportFragmentManager)
        detectViewModel = ViewModelProvider(this)[DetectScrollEndViewModel::class.java]
    }

    private fun toolbar(navController: NavController) {
        setSupportActionBar(binding.profileToolbar)
        NavigationUI.setupActionBarWithNavController(this, navController)

    }

    private fun navigationChangeListener(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (
                destination.id == R.id.editAddressFragment ||
                destination.id == R.id.productDetailsFragment ||
                destination.id == R.id.orderDetailsFragment ||
                destination.id == R.id.checkVerificationCodeFragment||
                destination.id == R.id.loginFragment ||
                destination.id == R.id.signUpFragment ||
                destination.id == R.id.forgetPasswordFragment
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
            mainNavigationViewModel.onBottomNavigationEvent(item.itemId)
            true
        }
    }

    private fun navigationEvent() {
        //val findNavController = findNavController(R.id.fragmentContainer)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainNavigationViewModel.navigationEvent.collectLatest { event ->
                    when (event) {
                        is UiEvent.Navigation.Home -> {
                            findNavController(R.id.fragmentContainer).apply {
                                navigate(event.destinationId)
                                popBackStack(event.destinationId, inclusive = false)
                            }

                        }

                        is UiEvent.Navigation.Cart -> {
                            findNavController(R.id.fragmentContainer).apply {
                                navigate(event.destinationId)
                                popBackStack(event.destinationId, inclusive = false)
                            }
                        }

                        is UiEvent.Navigation.Orders -> {
                            findNavController(R.id.fragmentContainer).apply {
                                navigate(event.destinationId)
                                popBackStack(event.destinationId, inclusive = false)
                            }
                        }

                        is UiEvent.Navigation.Setting -> {
                            findNavController(R.id.fragmentContainer).apply {
                                navigate(event.destinationId)
                                popBackStack(event.destinationId, inclusive = false)
                            }
                        }

                        is UiEvent.ShowSnackBar -> {
                            SnackBarCustom.showSnackbar(
                                view = binding.root,
                                message = event.message
                            )
                            Snackbar.make(binding.root, event.message, Snackbar.LENGTH_SHORT).show()
                        }

                        else -> Unit
                    }
                }
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


    private suspend fun isLogin(): Boolean {
        return !tokenManager.getToken().isNullOrEmpty()
    }


    private fun Context.setAppLocal(language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = this.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return createConfigurationContext(config)
    }

    private fun defaultNightMode() {
        PreferencesUtils.isDarkMode = shardPreferences.getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (PreferencesUtils.isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun startDestinationGraph() {
        lifecycleScope.launch {
            val navHostFragment = supportFragmentManager
                .findFragmentById(binding.fragmentContainer.id) as NavHostFragment
            val navController = navHostFragment.navController
            val graph = navController.navInflater.inflate(R.navigation.navigation_bottom_bar)

            if (isLogin()) {
                graph.setStartDestination(R.id.productFragment)
            } else {
                graph.setStartDestination(R.id.loginFragment)
            }
            navController.graph = graph
        }
    }

    private fun windowSystem() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, systemBars.bottom)
            insets
        }
        (application as EcommerceApp).hideSystemUI(this)
    }

    private fun appLifecycleObserver() {
        appLifecycleViewModel.rootView = binding.root
        appLifecycleViewModel.loadingDialog = loadingDialog
        appLifecycleViewModel.fragmentManager = supportFragmentManager
        appLifecycleObserver = AppLifecycleObserver(
            networkStatusViewModel = networkStatusViewModel,
            appLifecycleViewModel = appLifecycleViewModel
        )
        ProcessLifecycleOwner.Companion.get().lifecycle.addObserver(appLifecycleObserver)
    }


}