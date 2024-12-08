package com.example.ecommerce.features.userprofile.domain.usecases.updateusernamedetails

import com.example.ecommerce.features.userprofile.domain.entites.UpdateUserNameDetailsRequestEntity
import com.example.ecommerce.features.userprofile.domain.repositories.UserProfileRepository
import javax.inject.Inject

class UpdateUserNameDetailsUseCase @Inject constructor(private val repository: UserProfileRepository) :
    IUpdateUserNameDetailsUseCase {
    override suspend fun invoke(updateUserNameDetailsParams: UpdateUserNameDetailsRequestEntity) {
        repository.updateUserNameDetails(updateUserNameDetailsParams = updateUserNameDetailsParams)
    }
}