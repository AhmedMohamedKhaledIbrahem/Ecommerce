package com.example.ecommerce.features.userprofile.data.repositories

import com.example.ecommerce.core.data.entities.UserEntity
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.userprofile.data.datasources.localdatasource.UserProfileLocalDataSource
import com.example.ecommerce.features.userprofile.data.datasources.remotedatasource.UserProfileRemoteDataSource
import com.example.ecommerce.features.userprofile.data.mapper.GetImageProfileMapper
import com.example.ecommerce.features.userprofile.data.mapper.UpdateUserNameDetailsMapper
import com.example.ecommerce.features.userprofile.data.mapper.UploadImageProfileMapper
import com.example.ecommerce.features.userprofile.domain.entites.GetImageProfileResponseEntity
import com.example.ecommerce.features.userprofile.domain.entites.UpdateUserNameDetailsRequestEntity
import com.example.ecommerce.features.userprofile.domain.entites.UploadImageProfileResponseEntity
import com.example.ecommerce.features.userprofile.domain.repositories.UserProfileRepository
import java.io.File
import javax.inject.Inject

class UserProfileRepositoryImp @Inject constructor(
    private val localDataSource: UserProfileLocalDataSource,
    private val remoteDataSource: UserProfileRemoteDataSource,
    private val internetConnectionChecker: InternetConnectionChecker
) : UserProfileRepository {
    override suspend fun uploadImageProfile(image: File): UploadImageProfileResponseEntity {
        return try {
            if (internetConnectionChecker.hasConnection()) {
                val uploadImage = remoteDataSource.uploadImageProfile(image = image)
                localDataSource.updateImageUserProfile(
                    uploadImage.imageLink,
                    userId = uploadImage.userid
                )
                UploadImageProfileMapper.mapToEntity(uploadImage)
            } else {
                throw Failures.ConnectionFailure("No Internet Connection")
            }
        } catch (e: FailureException) {
            throw Failures.ServerFailure(e.message ?: " Unknown Error")
        }
    }

    override suspend fun getImageProfileById(userId: Int): GetImageProfileResponseEntity {
        return try {
            if (internetConnectionChecker.hasConnection()) {
                val getImage = remoteDataSource.getImageProfileById(userId = userId)
                localDataSource.updateImageUserProfile(
                    image = getImage.profileImage,
                    userId = getImage.userId
                )
                GetImageProfileMapper.mapToEntity(getImage)
            } else {
                throw Failures.ConnectionFailure("No Internet Connection")
            }
        } catch (e: FailureException) {
            throw Failures.ServerFailure(e.message ?: " Unknown Error")
        }
    }

    override suspend fun getUserProfile(): UserEntity {
        return try {
            localDataSource.getUserProfile()
        } catch (e: FailureException) {
            throw Failures.CacheFailure(e.message ?: " Unknown Error")
        }
    }

    override suspend fun updateUserNameDetails(
        updateUserNameDetailsParams: UpdateUserNameDetailsRequestEntity
    ) {
        try {
            if (internetConnectionChecker.hasConnection()) {
                val updateUserNameRequestModel = UpdateUserNameDetailsMapper.mapToModel(
                    entity = updateUserNameDetailsParams
                )
                val updateUserNameDetailsRemote = remoteDataSource.updateUserNameDetails(
                    updateUserNameDetailsParams = updateUserNameRequestModel
                )
                try {
                    localDataSource.updateUserNameDetails(
                        updateUserNameDetailsParams = updateUserNameDetailsRemote
                    )
                } catch (e: FailureException) {
                    throw Failures.CacheFailure(e.message ?: "Unknown Error")
                }
            } else {
                throw Failures.ConnectionFailure("No Internet Connection")
            }
        } catch (e: FailureException) {
            throw Failures.ServerFailure(e.message ?: "Unknown Error")
        }
    }

    override suspend fun getUserNameDetails() {
        try {
            if (internetConnectionChecker.hasConnection()) {
                val isUpdate = remoteDataSource.checkUserNameDetailsUpdate()
                if (isUpdate.update) {
                    val getUserNameDetailsRemote = remoteDataSource.getUserNameDetails()

                    try {
                        localDataSource.updateUserNameDetails(
                            updateUserNameDetailsParams = getUserNameDetailsRemote
                        )
                    } catch (e: FailureException) {
                        throw Failures.CacheFailure(e.message ?: " Unknown Error")
                    }
                }
            } else {
                throw Failures.ConnectionFailure("No Internet Connection")
            }

        } catch (e: FailureException) {
            throw Failures.ServerFailure(e.message ?: "Unknown Error")
        }
    }


}