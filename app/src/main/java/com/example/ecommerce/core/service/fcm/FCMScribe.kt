package com.example.ecommerce.core.service.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

object FCMScribe : IFCMScribe {
    override fun subscribeToTopic(topic: String) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
            .addOnSuccessListener {
                Log.e("subscribeToTopic", "subscribeToTopic: Success")
            }
            .addOnCompleteListener{
                Log.e("subscribeToTopic", "subscribeToTopic: Complete")
            }
            .addOnFailureListener { failure ->
                Log.e("subscribeToTopic", "subscribeToTopic: ${failure.message}")
            }
    }
}

interface IFCMScribe {
    fun subscribeToTopic(topic: String)

}