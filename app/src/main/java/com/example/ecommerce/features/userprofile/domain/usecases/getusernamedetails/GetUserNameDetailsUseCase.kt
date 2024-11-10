package com.example.ecommerce.features.userprofile.domain.usecases.getusernamedetails

import com.example.ecommerce.features.userprofile.domain.repositories.UserProfileRepository
import javax.inject.Inject

class GetUserNameDetailsUseCase @Inject constructor(
    private val repository: UserProfileRepository
) : IGetUserNameDetailsUseCase {
    override suspend fun invoke() {
        repository.getUserNameDetails()
    }

}