package com.example.ecommerce.features.userprofile.data.datasources

import com.example.ecommerce.core.constants.CHECK_USER_NAME_DETAILS_UPDATE_END_POINT
import com.example.ecommerce.core.constants.GET_IMAGE_PROFILE_END_POINT
import com.example.ecommerce.core.constants.GET_USER_NAME_DETAILS_END_POINT
import com.example.ecommerce.core.constants.UPDATE_USER_NAME_DETAILS_END_POINT
import com.example.ecommerce.core.constants.UPLOAD_IMAGE_PROFILE_END_POINT
import com.example.ecommerce.features.userprofile.data.models.CheckUserNameDetailsResponseModel
import com.example.ecommerce.features.userprofile.data.models.GetImageProfileResponseModel
import com.example.ecommerce.features.userprofile.data.models.UpdateUserNameDetailsRequestModel
import com.example.ecommerce.features.userprofile.data.models.UpdateUserNameDetailsResponseModel
import com.example.ecommerce.features.userprofile.data.models.UploadImageProfileResponseModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface UserProfileApi {

    @Multipart
    @POST(UPLOAD_IMAGE_PROFILE_END_POINT)
    suspend fun uploadImageProfile(
        @Part profileImage: MultipartBody.Part
    ): Response<UploadImageProfileResponseModel>

    @GET(GET_IMAGE_PROFILE_END_POINT)
    suspend fun getImageProfile(
        @Query("user_id") userId: Int
    ): Response<GetImageProfileResponseModel>

    @GET(CHECK_USER_NAME_DETAILS_UPDATE_END_POINT)
    suspend fun checkUserNameDetailsUpdate(): Response<CheckUserNameDetailsResponseModel>

    @GET(GET_USER_NAME_DETAILS_END_POINT)
    suspend fun getUserNameDetails(
    ): Response<UpdateUserNameDetailsResponseModel>

    @PUT(UPDATE_USER_NAME_DETAILS_END_POINT)
    suspend fun updateUserNameDetails(
        @Body updateUserNameDetailsParams: UpdateUserNameDetailsRequestModel
    ): Response<UpdateUserNameDetailsResponseModel>

}