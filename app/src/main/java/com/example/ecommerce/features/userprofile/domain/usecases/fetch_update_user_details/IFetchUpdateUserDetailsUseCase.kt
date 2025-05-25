package com.example.ecommerce.features.userprofile.domain.usecases.fetch_update_user_details

interface IFetchUpdateUserDetailsUseCase {
    suspend operator fun invoke(): String
}