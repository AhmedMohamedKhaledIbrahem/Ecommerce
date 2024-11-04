package com.example.ecommerce.features.authentication.presentation.viewmodel.resendcodetimerviewmodel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData

interface IResendCodeTimerViewModel {
    val remainingTime:LiveData<Long>
    val isTimerRunning:LiveData<Boolean>
    var timer:CountDownTimer?
    fun startTimer(duration:Long = 30000)
    fun stopTimer()
    fun clearTimer()

}