package com.example.ecommerce.features.userprofile.domain.usecases.update_user_details

import com.example.ecommerce.features.userprofile.domain.entites.UpdateUserDetailsRequestEntity

interface IUpdateUserDetailsUseCase {
    suspend operator fun invoke(updateUserNameDetailsParams: UpdateUserDetailsRequestEntity)
}