package com.example.ecommerce.features.userprofile.modules

import com.example.ecommerce.features.userprofile.domain.repositories.UserProfileRepository
import com.example.ecommerce.features.userprofile.domain.usecases.get_image_profile_by_id.GetImageProfileByIdUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.get_image_profile_by_id.IGetImageProfileByIdUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.fetch_update_user_details.FetchUpdateUserDetailsUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.fetch_update_user_details.IFetchUpdateUserDetailsUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.get_user_profile.GetUserProfileUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.get_user_profile.IGetUserProfileUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.update_user_details.IUpdateUserDetailsUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.update_user_details.UpdateUserDetailsUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.upload_image_profile.IUploadImageProfileUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.upload_image_profile.UploadImageProfileUseCase
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
            : IFetchUpdateUserDetailsUseCase {
        return FetchUpdateUserDetailsUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideUpdateUserNameDetailsUseCase(repository: UserProfileRepository)
            : IUpdateUserDetailsUseCase {
        return UpdateUserDetailsUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideUploadImageProfileUseCase(repository: UserProfileRepository)
            : IUploadImageProfileUseCase {
        return UploadImageProfileUseCase(repository = repository)
    }
}