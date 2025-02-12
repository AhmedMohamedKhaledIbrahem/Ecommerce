package com.example.ecommerce.features.userprofile.modules

import com.example.ecommerce.features.userprofile.domain.repositories.UserProfileRepository
import com.example.ecommerce.features.userprofile.domain.usecases.getimageprofilebyid.GetImageProfileByIdUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.getimageprofilebyid.IGetImageProfileByIdUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.getusernamedetails.GetUserNameDetailsUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.getusernamedetails.IGetUserNameDetailsUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.getuserprofile.GetUserProfileUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.getuserprofile.IGetUserProfileUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.updateusernamedetails.IUpdateUserNameDetailsUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.updateusernamedetails.UpdateUserNameDetailsUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.uploadimageprofile.IUploadImageProfileUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.uploadimageprofile.UploadImageProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    @Singleton
    fun provideGetImageProfileByIdUseCase(repository: UserProfileRepository)
            : IGetImageProfileByIdUseCase {
        return GetImageProfileByIdUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideGetUserProfileByIdUseCase(repository: UserProfileRepository)
            : IGetUserProfileUseCase {
        return GetUserProfileUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideGetUserNameDetailsUseCase(repository: UserProfileRepository)
            : IGetUserNameDetailsUseCase {
        return GetUserNameDetailsUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideUpdateUserNameDetailsUseCase(repository: UserProfileRepository)
            : IUpdateUserNameDetailsUseCase {
        return UpdateUserNameDetailsUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideUploadImageProfileUseCase(repository: UserProfileRepository)
            : IUploadImageProfileUseCase {
        return UploadImageProfileUseCase(repository = repository)
    }
}