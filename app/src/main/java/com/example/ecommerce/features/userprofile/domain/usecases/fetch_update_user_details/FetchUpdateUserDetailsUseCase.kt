package com.example.ecommerce.features.userprofile.domain.usecases.fetch_update_user_details

import com.example.ecommerce.features.userprofile.domain.repositories.UserProfileRepository
import javax.inject.Inject

class FetchUpdateUserDetailsUseCase @Inject constructor(
    private val repository: UserProfileRepository
) : IFetchUpdateUserDetailsUseCase {
    override suspend fun invoke(): String {
      return  repository.fetchUpdateUserDetails()
    }

}