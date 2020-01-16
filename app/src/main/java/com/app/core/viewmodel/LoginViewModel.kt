package com.app.core.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import com.app.core.base.BaseViewModel
import com.app.core.model.*
import com.app.core.network.RestObserver
import com.app.core.repo.AppRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginViewModel @Inject
constructor(appContext: Context, sharedPreferences: SharedPreferences, private val appRepo: AppRepo) :
    BaseViewModel(appContext, sharedPreferences, appRepo) {

    companion object {
        const val OTP_GENERATED = 4;
        const val OTP_INVALID = 5;
        const val OTP_SUCCESS = 6;
        const val OTP_RESEND = 7;
        const val APP_VERSION_NOT_SUPPORTED_ERROR = 28;
        const val MOBILE_UNREGISTERED_ERROR = 29;
    }

    var userMobileNo: String? = null

    override fun setup(args: Bundle?) {}

    fun triggerOtp(mobileNo: String? = userMobileNo) {
        setLoading()
        fetchOtp(mobileNo)
    }

    fun resendOtp() {
        sendEvent(Action(actionId = OTP_RESEND))
        fetchOtp(userMobileNo)
    }

    private fun fetchOtp(mobileNo: String?) {
        disposables.add(
            appRepo.generateOtp(LoginRequest(mobileNo))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : RestObserver<Void>(this) {
                    override fun error(serverException: ServerException) {
                        setLoadingFailed(serverException)
                        sendEvent(
                            Action(
                                actionId = serverException.errorCode,
                                serverException = serverException
                            )
                        )
                    }

                    override fun success(data: Void?) {
                        setLoadingSuccess()
                        userMobileNo = mobileNo
                        sendEvent(Action(actionId = OTP_GENERATED))
                    }
                })
        )
    }

    fun verifyOtp(otp: String?) {
        setLoading()
        disposables.add(
            appRepo.verifyOtp(VerifyOtpRequest(userMobileNo, otp))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : RestObserver<VerifyOtpResponse>(this) {
                    override fun error(serverException: ServerException) {
                        setLoadingFailed(serverException)
                        sendEvent(
                            Action(
                                actionId = OTP_INVALID,
                                serverException = serverException
                            )
                        )
                    }

                    override fun success(data: VerifyOtpResponse?) {
                        setLoadingSuccess()
                        sendEvent(Action(actionId = OTP_SUCCESS))
                        saveUserData(data)
                    }
                })
        )
    }

    fun saveUserData(data: VerifyOtpResponse?) {
        appRepo.run {
            saveAccessToken(data?.token)
            saveForceCreateService(true)
        }
    }


}