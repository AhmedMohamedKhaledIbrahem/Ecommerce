package com.example.ecommerce.core.network.websocket

import com.example.ecommerce.features.userprofile.domain.entites.Entity

interface SendProfileNameDetails {
    suspend fun sendProfileNameDetailsUpdate(updateUserProfileEntity: Entity)

}