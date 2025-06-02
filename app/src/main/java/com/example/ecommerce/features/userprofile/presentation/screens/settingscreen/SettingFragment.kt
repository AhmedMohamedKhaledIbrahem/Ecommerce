package com.example.ecommerce.features.userprofile.presentation.screens.settingscreen

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.Edit_profile_result
import com.example.ecommerce.core.constants.Message
import com.example.ecommerce.core.fragment.LoadingDialogFragment
import com.example.ecommerce.core.manager.fcm.FcmDeviceToken
import com.example.ecommerce.core.manager.token.TokenManager
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.core.utils.PreferencesUtils
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.core.utils.checkIsMessageOrResourceId
import com.example.ecommerce.databinding.FragmentSettingBinding
import com.example.ecommerce.features.logout.presentation.event.LogoutEvent
import com.example.ecommerce.features.logout.presentation.viewmodel.LogoutViewModel
import com.example.ecommerce.features.preferences.presentation.event.PreferencesEvent
import com.example.ecommerce.features.preferences.presentation.viewmodel.PreferencesViewModel
import com.example.ecommerce.features.userprofile.presentation.event.ImageProfileEvent
import com.example.ecommerce.features.userprofile.presentation.event.UserDetailsEvent
import com.example.ecommerce.features.userprofile.presentation.event.UserProfileEvent
import com.example.ecommerce.features.userprofile.presentation.screens.settingscreen.settingrecyclerview.SettingAdapter
import com.example.ecommerce.features.userprofile.presentation.screens.settingscreen.settingrecyclerview.SettingItem
import com.example.ecommerce.features.userprofile.presentation.viewmodel.fetch_update_user_details.UserDetailsViewModel
import com.example.ecommerce.features.userprofile.presentation.viewmodel.imageprofile.ImageProfileViewModel
import com.example.ecommerce.features.userprofile.presentation.viewmodel.userprofile.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var settingAdapter: SettingAdapter
    private lateinit var pickImageResult: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var rootView: View
    private val userProfileViewModel by viewModels<UserProfileViewModel>()
    private val userDetailsViewModel by viewModels<UserDetailsViewModel>()
    private val updateImageProfileViewModel by viewModels<ImageProfileViewModel>()
    private val preferencesViewModel by viewModels<PreferencesViewModel>()
    private val logoutViewModel by viewModels<LogoutViewModel>()
    private lateinit var updateDisplayNameViewModel: UpdateDisplayNameViewModel
    private var isAdapterInitialized = false


    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var fcmDeviceToken: FcmDeviceToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher().launch(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            requestPermissionLauncher().launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        pickImageResult()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        rootView = binding.root
        if (!isReadStoragePermissionGranted()) {
            onImageSettingClickListener(pickImageResult)
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // loadingDialog = LoadingDialogFragment.Companion.getInstance(childFragmentManager)
        fetchUpdateUserDetails()
        getUserProfile()
        getUserProfileState()
        getUserProfileEvent()
        userDetailsEvent()
        userDetailsState()
        uploadImageProfileState()
        uploadImageProfileEvent()
        getDarkMode()
        preferencesState()
        preferencesEvent()
        initRecyclerView()
        updateDisplayNameViewModel =
            ViewModelProvider(requireActivity())[UpdateDisplayNameViewModel::class.java]
        updateUserDisplayName()
        showMessageFromEditProfile()
        logoutState()
        logoutEvent()

    }

    private fun fetchUpdateUserDetails() {
        userDetailsViewModel.onEvent(UserDetailsEvent.LoadUserDetailsCheck)
    }

    private fun userDetailsState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userDetailsViewModel.userDetailsState.collect { state ->
                    if (state.displayName != null) {
                        updateDisplayNameViewModel.updateDisplayName(state.displayName)
                    }
                }
            }
        }
    }

    private fun userDetailsEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userDetailsViewModel.userDetailsEvent.collect { event ->
                    when (event) {
                        is UiEvent.ShowSnackBar -> {
                            Log.e("event", "userDetailsEvent: ${event.message}")

                            SnackBarCustom.showSnackbar(rootView, event.message)
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun getUserProfile() {
        userProfileViewModel.onEvent(UserProfileEvent.UserProfileLoad)
    }

    private fun getUserProfileState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userProfileViewModel.userProfileState.collect { state ->
                    if (state.userEntity != null) {
                        binding.userNameTextView.text = state.userEntity.displayName
                        binding.userEmailTextView.text = state.userEntity.userEmail
                        state.userEntity.imagePath?.let { imagePath ->
                            binding.userImageView.load(imagePath) {
                                error(R.drawable.profile_circle)
                                transformations(CircleCropTransformation())
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getUserProfileEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userProfileViewModel.userProfileEvent.collect { event ->
                    when (event) {
                        is UiEvent.ShowSnackBar -> {
                            SnackBarCustom.showSnackbar(rootView, event.message)
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun updateImageProfile(imageFile: File) {

        updateImageProfileViewModel.onEvent(
            ImageProfileEvent.Input.UploadImage(imageFile)
        )
        updateImageProfileViewModel.onEvent(
            ImageProfileEvent.UploadImageProfileButton
        )
    }

    private fun userImageProfileView(imageFile: File) {

        binding.userImageView.load(imageFile) {
            error(R.drawable.profile_circle)
            transformations(CircleCropTransformation())
        }
    }

    private fun pickImageResult() {
        pickImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val imageUri: Uri? = result.data?.data
                    imageUri?.let { imagePath ->
                        val imageFile = uriToTempFile(requireContext(), imagePath)
                            ?: return@registerForActivityResult
                        userImageProfileView(imageFile)
                        updateImageProfile(imageFile)
                    }
                } else {
                    SnackBarCustom.showSnackbar(
                        view = rootView,
                        message = getString(R.string.imageNotSelected)
                    )
                }
            }
    }

    private fun uploadImageProfileState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                updateImageProfileViewModel.imageProfileState.collect { state ->
                    if (state.isUploadImageLoading) {
                        with(binding.shimmerUserImageLayout) {
                            visibility = View.VISIBLE
                            startShimmer()
                        }
                        binding.userImageView.visibility = View.GONE
                    } else {
                        with(binding.shimmerUserImageLayout) {
                            visibility = View.GONE
                            stopShimmer()
                        }
                        binding.userImageView.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun uploadImageProfileEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                updateImageProfileViewModel.imageProfileEvent.collect { event ->
                    when (event) {
                        is UiEvent.ShowSnackBar -> {
                            SnackBarCustom.showSnackbar(rootView, event.message)
                        }

                        else -> Unit
                    }
                }
            }
        }
    }


    private fun preferencesState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                preferencesViewModel.preferencesState.collect { state ->
                    PreferencesUtils.isDarkMode = state.isDarkMode
                }
            }
        }
    }

    private fun preferencesEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                preferencesViewModel.preferencesEvent.collect { event ->
                    when (event) {
                        is UiEvent.ShowSnackBar -> {
                            SnackBarCustom.showSnackbar(rootView, event.message)
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun logoutState() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                logoutViewModel.logoutState.collect { state ->
                    if (state.isLoading) {
                        LoadingDialogFragment.show(parentFragmentManager)
                    } else {
                        LoadingDialogFragment.dismiss(parentFragmentManager)
                    }
                }
            }
        }
    }

    private fun logoutEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                logoutViewModel.logoutEvent.collect { event ->
                    when (event) {
                        is UiEvent.Navigation.SignIn -> {
                            //val navOptions = navigationOption(fragmentId = R.id.settingFragment)

                            findNavController().navigate(
                                event.destinationId, null,
                                NavOptions.Builder()
                                    .setPopUpTo(
                                        R.id.navigation_bottom_bar,
                                        true
                                    ).build()
                            )
                        }

                        is UiEvent.ShowSnackBar -> {
                            checkIsMessageOrResourceId(event, requireContext(), rootView)
                        }

                        else -> Unit
                    }

                }
            }
        }
    }

    private fun logout() {
        val fcmToken = getTokens()
        logoutViewModel.onEvent(LogoutEvent.FcmTokenInput(fcmToken))
        logoutViewModel.onEvent(LogoutEvent.LogoutButton)
    }

    private fun getTokens(): String {
        return fcmDeviceToken.getFcmTokenDevice() ?: ""
    }


    private fun updateUserDisplayName() {
        updateDisplayNameViewModel.displayName.observe(viewLifecycleOwner) { value ->
            binding.userNameTextView.text = value
        }
    }


    private fun initRecyclerView() {
        binding.settingRecyclerView.layoutManager = LinearLayoutManager(context)
        val data = setData()
        settingAdapter = SettingAdapter(
            data,
            onItemClickListener = { settingItem ->
                when (settingItem.title) {
                    getString(R.string.logout) -> {
                        logout()
                    }

                    else -> {
                        navigateToDestination(settingItem.destinationId)
                    }
                }
            },
            onSwitchChangeListener = { settingItem, isChecked ->
                if (settingItem.title == getString(R.string.darkMode)) {
                    toggleDarkMode(isChecked)
                    defaultNightMode(isChecked)
                }
            }

        )
        binding.settingRecyclerView.adapter = settingAdapter
        isAdapterInitialized = true
        val position = findDarkModeItemPosition()
        position?.let {
            settingAdapter.updateItemCheckedState(it, PreferencesUtils.isDarkMode)
        }

    }


    private fun toggleDarkMode(isChecked: Boolean) {
        preferencesViewModel.onEvent(PreferencesEvent.Input.SetDarkMode(isChecked))
        preferencesViewModel.onEvent(PreferencesEvent.Button.DarkModeButton)
    }

    private fun setData(): List<SettingItem> {
        val settingItem = listOf(
            SettingItem(
                R.drawable.user_person_profile,
                getString(R.string.accountDetails),
                enableSwitch = false,
                isChecked = false,
                R.id.editProfileFragment,
            ),
            SettingItem(
                R.drawable.night_mode,
                getString(R.string.darkMode),
                enableSwitch = true,
                isChecked = PreferencesUtils.isDarkMode,
                SettingItem.NO_DESTINATION,
            ),
            SettingItem(
                R.drawable.language,
                getString(R.string.language),
                enableSwitch = false,
                isChecked = false,
                R.id.languageFragment,
            ),
            SettingItem(
                R.drawable.licens,
                getString(R.string.policePrivacy),
                enableSwitch = false,
                isChecked = false,
                R.id.productFragment,
            ),
            SettingItem(
                R.drawable.logout,
                getString(R.string.logout),
                enableSwitch = false,
                isChecked = false,
                SettingItem.NO_DESTINATION,
            ),

            )
        return settingItem
    }

    private fun navigateToDestination(destinationId: Int) {
        if (destinationId == SettingItem.NO_DESTINATION) return
        val navController = findNavController()
        navController.navigate(destinationId)


    }

    private fun openGallery(pickImageResult: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageResult.launch(intent)
    }

    private fun onImageSettingClickListener(pickImageResult: ActivityResultLauncher<Intent>) {

        binding.userImageView.setOnClickListener {
            openGallery(pickImageResult)
        }
    }


    private fun isReadStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissionLauncher(): ActivityResultLauncher<String> {
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                onImageSettingClickListener(pickImageResult)
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

        return requestPermissionLauncher
    }

    private fun uriToTempFile(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val file = File.createTempFile("upload_", ".jpg", context.cacheDir)
            file.outputStream().use { output ->
                inputStream.copyTo(output)
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    private fun getDarkMode() {
        preferencesViewModel.onEvent(PreferencesEvent.Get.GetDarkMode)
    }

    private fun findDarkModeItemPosition(): Int? {
        if (!::settingAdapter.isInitialized) {
            return null
        }
        return settingAdapter.items.indexOfFirst { it.title == getString(R.string.darkMode) }
            .takeIf { it != -1 } //
    }

    private fun defaultNightMode(toggle: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (toggle) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun showMessageFromEditProfile() {
        parentFragmentManager.setFragmentResultListener(
            Edit_profile_result,
            this
        ) { _, bundle ->
            val message = bundle.getString(Message)
            Log.e("message", "showMessageFromEditProfile: $message")
            if (message == null) return@setFragmentResultListener
            SnackBarCustom.showSnackbar(rootView, message)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}