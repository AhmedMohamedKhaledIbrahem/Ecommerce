package com.example.ecommerce.features.userprofile.presentation.screens.settingscreen

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ContentResolver
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
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.user.UserEntity
import com.example.ecommerce.core.fragment.LoadingDialogFragment
import com.example.ecommerce.core.ui.state.UiState
import com.example.ecommerce.core.utils.AddressUtil
import com.example.ecommerce.core.utils.PreferencesUtils
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.databinding.FragmentSettingBinding
import com.example.ecommerce.features.preferences.presentation.viewmodel.IPreferencesViewModel
import com.example.ecommerce.features.preferences.presentation.viewmodel.PreferencesViewModel
import com.example.ecommerce.features.userprofile.presentation.screens.settingscreen.settingrecyclerview.SettingAdapter
import com.example.ecommerce.features.userprofile.presentation.screens.settingscreen.settingrecyclerview.SettingItem
import com.example.ecommerce.features.userprofile.presentation.viewmodel.imageprofile.IImageProfileViewModel
import com.example.ecommerce.features.userprofile.presentation.viewmodel.imageprofile.ImageProfileViewModel
import com.example.ecommerce.features.userprofile.presentation.viewmodel.userprofile.IUserProfileViewModel
import com.example.ecommerce.features.userprofile.presentation.viewmodel.userprofile.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@AndroidEntryPoint
class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var settingAdapter: SettingAdapter
    private lateinit var pickImageResult: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var rootView: View
    private val userProfileViewModel: IUserProfileViewModel by viewModels<UserProfileViewModel>()
    private val updateImageProfileViewModel: IImageProfileViewModel by viewModels<ImageProfileViewModel>()
    private lateinit var updateDisplayNameViewModel: UpdateDisplayNameViewModel
    private lateinit var loadingDialog: LoadingDialogFragment
    private var isAdapterInitialized = false
    private val preferencesViewModel: IPreferencesViewModel by viewModels<PreferencesViewModel>()


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
        loadingDialog = LoadingDialogFragment.getInstance(childFragmentManager)
        getUserProfile()
        getUserProfileState()
        getDarkMode()
        preferencesState()
        initRecyclerView()
        updateDisplayNameViewModel =
            ViewModelProvider(requireActivity())[UpdateDisplayNameViewModel::class.java]
        uploadImageProfileState()
        updateUserDisplayName()


    }


    private fun getUserProfile() {
        userProfileViewModel.getUserProfile()
    }


    private fun getUserProfileState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userProfileViewModel.userProfileState.collect { state ->
                    getUserProfileUiStates(state)
                }
            }
        }
    }

    private fun uploadImageProfileState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                updateImageProfileViewModel.imageProfileState.collect { state ->
                    uploadImageProfileUiState(state)
                }
            }
        }
    }

    private fun preferencesState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                preferencesViewModel.preferencesState.collect { state ->
                    preferencesUiState(state)
                }
            }
        }
    }

    private fun preferencesUiState(state: UiState<Any>) {
        when (state) {
            is UiState.Loading -> {
                preferencesLoadingState(state)
            }

            is UiState.Success -> {
                preferencesSuccessState(state)
            }

            is UiState.Error -> {
                preferencesErrorState(state)
            }

        }
    }

    private fun preferencesErrorState(state: UiState.Error) {
        when (state.source) {
            "setDarkModeEnabled" -> {
                SnackBarCustom.showSnackbar(
                    rootView,
                    state.message
                )
            }

            "isDarkModeEnabled" -> {
                SnackBarCustom.showSnackbar(
                    rootView,
                    state.message
                )
            }
        }
    }

    private fun preferencesSuccessState(state: UiState.Success<Any>) {
        when (state.source) {
            "setDarkModeEnabled" -> {
                //updateDarkMode()
            }

            "isDarkModeEnabled" -> {
                val isDarkModeEnabled = state.data as Boolean
                Log.e("isDarkModeEnabled", "isDarkModeEnabled: $isDarkModeEnabled")
                PreferencesUtils.isDarkMode = isDarkModeEnabled
            }
        }
    }

    private fun preferencesLoadingState(state: UiState.Loading) {
        when (state.source) {
            "setDarkModeEnabled" -> {}
            "isDarkModeEnabled" -> {}
        }
    }

    private fun uploadImageProfileUiState(state: UiState<Any>) {
        when (state) {
            is UiState.Loading -> {
                uploadImageProfileLoadingState(state)
            }

            is UiState.Success -> {
                uploadImageProfileSuccessState(state)
            }

            is UiState.Error -> {
                uploadImageProfileErrorState(state)
            }

        }
    }

    private fun uploadImageProfileErrorState(state: UiState.Error) {
        when (state.source) {
            "uploadImageProfile" -> {
                loadingDialog.dismissLoading()
                SnackBarCustom.showSnackbar(
                    view = rootView,
                    message = state.message
                )
                Log.d("uploadImageProfileError", "ErrorImage:${state.message} ")
            }
        }
    }

    private fun uploadImageProfileSuccessState(state: UiState.Success<Any>) {
        when (state.source) {
            "uploadImageProfile" -> {
                loadingDialog.dismissLoading()
            }
        }
    }

    private fun uploadImageProfileLoadingState(state: UiState.Loading) {
        when (state.source) {
            "uploadImageProfile" -> {
                loadingDialog.showLoading(fragmentManager = parentFragmentManager)
            }
        }
    }

    private fun getUserProfileUiStates(state: UiState<Any>) {
        when (state) {
            is UiState.Loading -> {
                getUserProfileLoadingState(state)
            }

            is UiState.Success -> {
                getUserProfileSuccessState(state)
            }

            is UiState.Error -> {
                getUserProfileErrorState(state)

            }

        }
    }

    private fun getUserProfileErrorState(state: UiState.Error) {
        when (state.source) {
            "getUserProfile" -> {
                loadingDialog.dismissLoading()
                SnackBarCustom.showSnackbar(
                    view = rootView,
                    message = state.message
                )
            }
        }
    }

    private fun getUserProfileSuccessState(state: UiState.Success<Any>) {
        when (state.source) {
            "getUserProfile" -> {
                val userProfile = state.data as UserEntity
                binding.userNameTextView.text = userProfile.displayName

                val userEmail = userProfile.userEmail
                binding.userEmailTextView.text = userEmail
                userProfile.imagePath?.let { imagePath ->
                    userImageProfileView(imagePath.toUri())
                }
                AddressUtil.addressId = userProfile.userId
                Log.d("getUserProfileSuccess", "Success:${userProfile.userId} ")

            }
        }
    }

    private fun getUserProfileLoadingState(state: UiState.Loading) {
        when (state.source) {
            "getUserProfile" -> {
                // loadingDialog.showLoading(fragmentManager = parentFragmentManager)
            }
        }
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
                    navigateToDestination(settingItem.destinationId)
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
        preferencesViewModel.setDarkModeEnabled(isChecked)
    }

    private fun setData(): List<SettingItem> {
        val settingItem = listOf(
            SettingItem(
                R.drawable.round_person_24,
                getString(R.string.accountDetails),
                enableSwitch = false,
                isChecked = false,
                R.id.editProfileFragment,
            ),
            SettingItem(
                R.drawable.online_delivery,
                getString(R.string.myorders),
                enableSwitch = false,
                isChecked = false,
                R.id.ordersFragment,
            ),
            SettingItem(
                R.drawable.location,
                getString(R.string.address),
                enableSwitch = false,
                isChecked = false,
                R.id.addressFragment,
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
                R.drawable.privacy,
                getString(R.string.policePrivacy),
                enableSwitch = false,
                isChecked = false,
                R.id.productFragment,
            ),
            SettingItem(
                R.drawable.log_out,
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

    private fun updateImageProfileViewModel(imagePath: Uri) {
        val imagePathFromUri = getImagePathFromUri(imagePath)
        val fileImagePath = File(imagePathFromUri)
        updateImageProfileViewModel.uploadImageProfile(fileImagePath)
    }


    private fun userImageProfileView(imagePath: Uri) {
        binding.userImageView.load(imagePath) {
            error(R.drawable.round_placeholder_24)
            transformations(CircleCropTransformation())
        }
    }

    private fun pickImageResult() {
        pickImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val imageUri: Uri? = result.data?.data
                    imageUri?.let { imagePath ->
                        userImageProfileView(imagePath)
                        updateImageProfileViewModel(imagePath)
                    }
                } else {
                    SnackBarCustom.showSnackbar(
                        view = rootView,
                        message = getString(R.string.imageNotSelected)
                    )
                }
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


    private fun getImagePathFromUri(uri: Uri): String {
        var path = ""
        val context: Context = requireContext()
        val contentResolver: ContentResolver = context.contentResolver
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            path = it.getString(columnIndex)
        }
        cursor?.close()
        return path
    }


    private fun getDarkMode() {
        preferencesViewModel.isDarkModeEnabled()
    }

    private fun findDarkModeItemPosition(): Int? {
        if (!::settingAdapter.isInitialized) {
            Log.e("AdapterError", "SettingAdapter is not initialized")
            return null
        }

        return settingAdapter.items.indexOfFirst { it.title == getString(R.string.darkMode) }
            .takeIf { it != -1 } // Return null if no matching item is found
    }

    private fun defaultNightMode(toggle: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (toggle) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}