package com.app.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import javax.inject.Inject
import android.text.InputType
import android.text.InputFilter
import android.widget.*
import com.app.core.R
import com.app.core.base.BaseFragment
import com.app.core.common.AnimationUtils
import com.app.core.common.Utils
import com.app.core.common.Utils.afterTextChanged
import com.app.core.common.Utils.launchKeyboard
import com.app.core.model.Action
import com.app.core.model.NetworkState
import com.app.core.model.NetworkState.Companion.ERROR
import com.app.core.model.NetworkState.Companion.LOADED
import com.app.core.model.NetworkState.Companion.LOADING
import com.app.core.viewmodel.LoginViewModel


class LoginOtpFragment : BaseFragment<LoginViewModel>() {

    @Inject
    lateinit var loginViewModel: LoginViewModel
    private lateinit var otpLayout: LinearLayout
    private lateinit var otpEditText: EditText
    private lateinit var otpEditTextLabel: TextView
    private lateinit var otpErrorMsgTxtView: TextView
    private lateinit var otpErrorImageView: ImageView
    private lateinit var otpIconView: ImageView
    private lateinit var submitContent: RelativeLayout
    private lateinit var submitLabel: TextView
    private lateinit var submitRoot: RelativeLayout
    private lateinit var loader: ImageView
    private lateinit var resend: TextView
    private lateinit var backButton: RelativeLayout
    private lateinit var otpMsg: TextView
    private lateinit var clearOtpIv: ImageView

    override fun getViewModel(): LoginViewModel = loginViewModel

    override fun getAppBarTitle(): String = getString(R.string.login)

    override fun getFragmentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.login_otp_screen, container, false);

    override fun setUi() {
        otpLayoutUi()
        footerUi()
    }

    override fun handleNetworkState(networkState: NetworkState?) {
        manageUserTouch(networkState)
        when (networkState) {
            LOADING -> {
                showloader()
                hideOtpErrorMessage()
            }
            LOADED -> {}
            ERROR -> {
                enableSubmit()
                resendOtpUi()
            }
        }
    }

    override fun handleEvents(action: Action?) {
        when (action?.actionId) {
            LoginViewModel.MOBILE_UNREGISTERED_ERROR -> {
                resendOtpUi()
            }
            LoginViewModel.OTP_RESEND -> {
                otpInProgressUi()
            }
            LoginViewModel.OTP_INVALID -> {
                showOtpErrorMessage()
                disableSubmit()
            }
            LoginViewModel.OTP_SUCCESS -> {
                //val intent = Intent(activity, TaskActivity::class.java)
                //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                //startActivity(intent)
            }
            LoginViewModel.OTP_GENERATED -> {
                Utils.showToast(context, getString(R.string.otp_msg, loginViewModel.userMobileNo))
                resendOtpUi()
            }
        }
    }

    private fun otpInProgressUi() {
        resend.text = getString(R.string.resending)
        resend.isClickable = false
    }

    private fun resendOtpUi() {
        resend.text = getString(R.string.resend_code)
        resend.isClickable = true
    }

    private fun showOtpErrorMessage() {
        otpErrorMsgTxtView.text = getString(R.string.otp_error)
        otpErrorMsgTxtView.visibility = View.VISIBLE
        otpErrorImageView.visibility = View.VISIBLE
    }

    private fun hideOtpErrorMessage() {
        if (otpErrorImageView.visibility == View.VISIBLE) {
            otpErrorMsgTxtView.visibility = View.INVISIBLE
            otpErrorImageView.visibility = View.INVISIBLE
        }
    }

    private fun showloader() {
        submitRoot.isClickable = false
        loader.visibility = View.VISIBLE
        loader.startAnimation(AnimationUtils.getRotateAnimation())
        submitContent.visibility = View.GONE
    }

    private fun footerUi() {
        submitRoot = view!!.findViewById(R.id.submit) as RelativeLayout
        loader = submitRoot.findViewById(R.id.loader)
        resend = view!!.findViewById(R.id.resend)
        resend.visibility = View.VISIBLE
        submitContent = submitRoot.findViewById(R.id.submit_content)
        submitLabel = submitRoot.findViewById(R.id.button_text)
        submitLabel.text = getString(R.string.login)
        submitRoot.setOnClickListener {
            getViewModel().verifyOtp(otpEditText.text.toString())
        }
        resend.setOnClickListener {
            getViewModel().resendOtp()
        }
        disableSubmit()
    }

    private fun enableSubmit() {
        submitRoot.isEnabled = true
        submitRoot.isClickable = true
        loader.visibility = View.GONE
        loader.clearAnimation()
        submitContent.visibility = View.VISIBLE
    }

    private fun disableSubmit() {
        submitRoot.isEnabled = false
        submitRoot.isClickable = false
        loader.visibility = View.INVISIBLE
        loader.clearAnimation()
        submitContent.visibility = View.VISIBLE
    }

    private fun otpLayoutUi() {
        otpLayout = view!!.findViewById(R.id.otp_root)
        otpIconView = otpLayout.findViewById(R.id.icon)
        otpIconView.setImageResource(R.drawable.key)
        otpErrorMsgTxtView = otpLayout.findViewById(R.id.error_msg)
        otpErrorImageView = otpLayout.findViewById(R.id.clear)
        otpEditTextLabel = otpLayout.findViewById(R.id.label)
        otpEditTextLabel.text = getString(R.string.enter_otp)
        otpEditText = otpLayout.findViewById(R.id.content)
        otpEditText.run {
            letterSpacing = 1.25f
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
            filters = arrayOf<InputFilter>(InputFilter.LengthFilter(4))
            launchKeyboard()
            afterTextChanged {
                hideOtpErrorMessage()
                when (it.length) {
                    4 -> enableSubmit()
                    else -> disableSubmit()
                }
            }
        }
        otpMsg = otpLayout.findViewById(R.id.otp_instruction)
        otpMsg.text = getString(R.string.otp_msg, loginViewModel.userMobileNo)
        clearOtpIv = view!!.findViewById(R.id.clear)
        clearOtpIv.setOnClickListener { otpEditText.setText("") }
        setBackButton()
    }

    private fun setBackButton() {
        backButton = view!!.findViewById(R.id.navigate_back_layout)
        backButton.setOnClickListener { onBackPressed() }
    }

    override fun onBackPressed() {
        fragmentInteractionListener?.getNavController()?.navigateUp()
    }

    override fun getMenuResId(): Int = 0

    override fun onMenuItemClick(item: MenuItem?): Boolean = true

    override fun showNavigationBack() = true

}