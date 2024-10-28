package com.example.ecommerce.features.authentication.data.repositories

import com.example.ecommerce.core.data.mapper.UserMapper
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.authentication.data.datasources.localdatasource.AuthenticationLocalDataSource
import com.example.ecommerce.features.authentication.data.datasources.localdatasource.AuthenticationSharedPreferencesDataSource
import com.example.ecommerce.features.authentication.data.datasources.remotedatasource.AuthenticationRemoteDataSource
import com.example.ecommerce.features.authentication.data.mapper.AuthenticationMapper
import com.example.ecommerce.features.authentication.data.mapper.MessageResponseMapper
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationResponseEntity
import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity
import com.example.ecommerce.features.authentication.domain.entites.SignUpRequestEntity
import com.example.ecommerce.features.authentication.domain.repositories.AuthenticationRepository
import javax.inject.Inject

class AuthenticationRepositoryImp @Inject constructor(
    private val remoteDataSource: AuthenticationRemoteDataSource,
    private val localDataSource: AuthenticationLocalDataSource,
    private val preferences: AuthenticationSharedPreferencesDataSource,
    private val networkInfo: InternetConnectionChecker
) : AuthenticationRepository {
    override suspend fun login(loginParams: AuthenticationRequestEntity): AuthenticationResponseEntity {
        return try {
            if (networkInfo.hasConnection()) {
                val login = remoteDataSource.login(loginParams)
                try {
                    preferences.saveToken(login.token)
                    val mapper = UserMapper.mapToEntity(login)
                    localDataSource.insertUser(mapper)
                } catch (e: FailureException) {
                    throw Failures.CacheFailure(e.message ?: "UnKnown Cache error")
                }
                AuthenticationMapper.mapToEntity(login)
            } else {
                throw Failures.ConnectionFailure("No Internet Connection")
            }
        } catch (e: FailureException) {
            throw Failures.ServerFailure(e.message ?: "Unknown server error")
        }
    }

    override suspend fun signUp(singUpParams: SignUpRequestEntity): MessageResponseEntity {
        return try {
            if (networkInfo.hasConnection()) {
                val signUp = remoteDataSource.signUp(singUpParams)
                MessageResponseMapper.mapToEntity(signUp)
            } else {
                throw Failures.ConnectionFailure("No Internet Connection")
            }
        } catch (e: FailureException) {
            throw Failures.ServerFailure(e.message ?: "Unknown server error")
        }
    }

    override suspend fun resetPassword(email: String): MessageResponseEntity {
        return try {
            if (networkInfo.hasConnection()) {
                val resetPassword = remoteDataSource.resetPassword(email)
                MessageResponseMapper.mapToEntity(resetPassword)
            } else {
                throw Failures.ConnectionFailure("No Internet Connection")
            }
        } catch (e: FailureException) {
            throw Failures.ServerFailure(e.message ?: "Unknown server error")
        }
    }

    override suspend fun logout() {
        return try {
            if (networkInfo.hasConnection()) {
             preferences.clearToken()
            } else {
                throw Failures.ConnectionFailure("No Internet Connection")
            }
        } catch (e: FailureException) {
            throw Failures.CacheFailure(e.message ?: "Unknown server error")
        }
    }
}