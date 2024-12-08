package com.example.ecommerce.features.userprofile.data.datasources

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
    @POST("wp-json/custom/v1/upload")
    suspend fun uploadImageProfile(
        @Part profileImage: MultipartBody.Part
    ): Response<UploadImageProfileResponseModel>

    @GET("wp-json/custom/v1/profile-image")
    suspend fun getImageProfile(
        @Query("user_id") userId: Int
    ): Response<GetImageProfileResponseModel>

    @GET("wp-json/custom/v1/profile-updated")
    suspend fun checkUserNameDetailsUpdate(): Response<CheckUserNameDetailsResponseModel>

    @GET("wp-json/wp/v2/users/me")
    suspend fun getUserNameDetails(
        @Query("context") context: String = "edit"
    ): Response<UpdateUserNameDetailsResponseModel>

    @PUT("wp-json/wp/v2/users/me")
    suspend fun updateUserNameDetails(
        @Body updateUserNameDetailsParams: UpdateUserNameDetailsRequestModel
    ): Response<UpdateUserNameDetailsResponseModel>

}