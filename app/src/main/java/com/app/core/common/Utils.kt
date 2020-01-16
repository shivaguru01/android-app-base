package com.app.core.common

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import com.app.core.helper.ComponentHelper
import com.app.core.model.NetworkState
import java.text.SimpleDateFormat
import java.util.*


object Utils {

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }

    fun EditText.launchKeyboard() {
        try {
            this.let {
                val imm = this.context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                it.requestFocus()
                it.postDelayed({ imm?.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT) }, 450)
            }
        } catch (e: Exception) {
            Log.e(APP_TAG, e.toString())
        }
    }

    fun EditText.hideKeyBoard() {
        try {
            this.let {
                val imm = this.context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        } catch (e: Exception) {
            Log.e(APP_TAG, e.toString())
        }
    }

    fun getComponentHelper(context: Context?): ComponentHelper? {
        var componentHelper: ComponentHelper? = null;
        context?.let {
            componentHelper = when (context) {
                is Application -> {
                    context as ComponentHelper
                }
                else -> {
                    context.applicationContext as ComponentHelper
                }
            }
        }
        return componentHelper
    }

    fun showToast(context: Context?, msg: String?) {
        if (context == null || msg.isNullOrBlank()) return
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun showToast(context: Context?, msg: String, duration: Int) {
        if (context == null) return
        Toast.makeText(context, msg, duration).show()
    }

    fun setContentLoadingSettings(window: Window?, networkState: NetworkState?) {
        if (networkState === NetworkState.LOADING) {
            Log.d(APP_TAG, "loading")
            window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else if (networkState === NetworkState.ERROR || networkState === NetworkState.LOADED) {
            Log.d(APP_TAG, "error or loaded")
            window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    fun enableScreenTouch(window: Window?) {
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    fun setContentLoadingSettings(
        contentLoadingProgressBar: ContentLoadingProgressBar,
        networkState: NetworkState?
    ) {
        if (networkState == null) return
        if (networkState === NetworkState.LOADING) {
            contentLoadingProgressBar.visibility = View.VISIBLE
        } else {
            contentLoadingProgressBar.visibility = View.GONE
        }
    }

    fun getRotateAnimation(): Animation {
        val rotate = RotateAnimation(
            0f, 360f, Animation.RELATIVE_TO_SELF,
            0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotate.interpolator = LinearInterpolator()
        rotate.duration = 700
        rotate.repeatCount = Animation.INFINITE
        return rotate
    }

    fun logoutApplications(context: Context, sharedPreferences: SharedPreferences) {
        clearUserData(sharedPreferences)
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent!!.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    private fun clearUserData(preference: SharedPreferences) {
        preference.edit().apply {
            clear()
            apply()
        }
    }


    fun getStringFromResId(context: Context?, resId: Int): String? {
        return context?.getString(resId)
    }

    fun vibrate(appContext: Context?, duration: Long) {
        val v = appContext?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v?.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            v?.vibrate(duration)
        }
    }

    fun vibrateError(appContext: Context?) {
        val vibrator = appContext?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        val pattern: LongArray = longArrayOf(0, 150, 30, 150, 50, 250)
        val amplitude: IntArray = intArrayOf(0, 255, 0, 255, 0, 255)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createWaveform(pattern, amplitude, -1));
        } else {
            vibrator?.vibrate(pattern, -1);
        }
    }

    fun getDateIn12HourFormat(inputDate: String?, pattern: String = "yyyy-MM-dd'T'HH:mm:ss'Z'"): String? {
        if (inputDate == null) return null
        return try {
            val inputUtcDateFormat = SimpleDateFormat(pattern)
            inputUtcDateFormat.timeZone = TimeZone.getTimeZone("GMT")
            val dateParsed: Date = inputUtcDateFormat.parse(inputDate)
            val outputFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
            outputFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
            outputFormat.format(dateParsed)
        } catch (ex: Exception) {
            null
        }
    }

    fun goToPlayStore(context: Context?) {
        context?.let { ctx ->
            try {
                ctx.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=${ctx.packageName}")
                    )
                );
            } catch (exception: ActivityNotFoundException) {
                ctx.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=${ctx.packageName}")
                    )
                );
            }
        }
    }

}
