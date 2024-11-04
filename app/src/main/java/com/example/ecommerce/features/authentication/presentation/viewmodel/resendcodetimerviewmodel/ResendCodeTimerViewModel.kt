package com.example.ecommerce.features.authentication.presentation.viewmodel.resendcodetimerviewmodel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel


class ResendCodeTimerViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), IResendCodeTimerViewModel {
    private val _remainingTime = MutableLiveData<Long>()
    override val remainingTime: LiveData<Long> get() = _remainingTime

    private val _isTimerRunning = MutableLiveData<Boolean>()
    override val isTimerRunning: LiveData<Boolean> = _isTimerRunning

    override var timer: CountDownTimer? = null


    init {
        _remainingTime.value = savedStateHandle["remaining_time"] ?: 0L
        _isTimerRunning.value = savedStateHandle["is_timer_running"] ?: false
        if (_isTimerRunning.value == true) {
          startTimer(_remainingTime.value ?: 30000)
        }
    }

    override fun startTimer(duration: Long) {
        _isTimerRunning.value = true
        _remainingTime.value = duration
        timer?.cancel()
        timer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _remainingTime.value = millisUntilFinished
            }

            override fun onFinish() {
                _isTimerRunning.value = false
                _remainingTime.value = 0L
            }

        }.start()
    }

    override fun stopTimer() {
        timer?.cancel()
        _isTimerRunning.value = false
    }

    override fun clearTimer() {
        super.onCleared()
        timer?.cancel()
    }

}