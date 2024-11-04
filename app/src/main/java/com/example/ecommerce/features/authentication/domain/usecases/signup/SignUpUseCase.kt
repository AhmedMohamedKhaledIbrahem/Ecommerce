package com.example.ecommerce.features.authentication.domain.usecases.signup

import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity
import com.example.ecommerce.features.authentication.domain.entites.SignUpRequestEntity
import com.example.ecommerce.features.authentication.domain.repositories.AuthenticationRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val repository: AuthenticationRepository) :
    ISignUpUseCase {
    override suspend operator fun invoke(signUpParams: SignUpRequestEntity): MessageResponseEntity {
        return repository.signUp(singUpParams = signUpParams)
    }
}