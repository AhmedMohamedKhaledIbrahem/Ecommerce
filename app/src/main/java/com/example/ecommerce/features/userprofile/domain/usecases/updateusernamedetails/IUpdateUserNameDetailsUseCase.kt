package com.example.ecommerce.features.userprofile.domain.usecases.updateusernamedetails

import com.example.ecommerce.features.userprofile.domain.entites.UpdateUserNameDetailsRequestEntity

interface IUpdateUserNameDetailsUseCase {
    suspend operator fun invoke(updateUserNameDetailsParams: UpdateUserNameDetailsRequestEntity)
}