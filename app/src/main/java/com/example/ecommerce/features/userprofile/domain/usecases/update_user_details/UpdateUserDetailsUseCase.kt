package com.example.ecommerce.features.userprofile.domain.usecases.update_user_details

import com.example.ecommerce.features.userprofile.domain.entites.UpdateUserDetailsRequestEntity
import com.example.ecommerce.features.userprofile.domain.repositories.UserProfileRepository
import javax.inject.Inject

class UpdateUserDetailsUseCase @Inject constructor(private val repository: UserProfileRepository) :
    IUpdateUserDetailsUseCase {
    override suspend fun invoke(updateUserDetailsParams: UpdateUserDetailsRequestEntity) {
        repository.updateUserDetails(updateUserDetailsParams = updateUserDetailsParams)
    }
}