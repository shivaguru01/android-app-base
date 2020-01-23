package com.app.core.ui.fragment

import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.app.core.R
import com.app.core.base.BaseFragment
import com.app.core.common.AnimationUtils
import com.app.core.common.DialogUtils
import com.app.core.common.Utils
import com.app.core.common.Utils.afterTextChanged
import com.app.core.common.Utils.launchKeyboard
import com.app.core.model.Action
import com.app.core.model.AppDialogInfo
import com.app.core.model.NetworkState
import com.app.core.model.NetworkState.Companion.ERROR
import com.app.core.model.NetworkState.Companion.LOADED
import com.app.core.model.NetworkState.Companion.LOADING
import com.app.core.viewmodel.LoginViewModel
import javax.inject.Inject

class LoginPhoneFragment : BaseFragment<LoginViewModel>() {

    @Inject
    lateinit var loginViewModel: LoginViewModel
    lateinit var phoneLayout: LinearLayout
    lateinit var phoneEditText: EditText
    lateinit var phoneEditTextLabel: TextView
    lateinit var phoneErrorMsgTxtView: TextView
    lateinit var phoneErrorImageView: ImageView
    lateinit var submitContent: RelativeLayout
    lateinit var submitLabel: TextView
    lateinit var submitRoot: RelativeLayout
    lateinit var loader: ImageView
    lateinit var resend: TextView
    lateinit var clearOtpIv: ImageView

    override fun getViewModel(): LoginViewModel = loginViewModel

    override fun getAppBarTitle(): String = getString(R.string.welcome)

    override fun getFragmentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.login_phone_screen, container, false);

    override fun setUi() {
        phoneLayoutUi()
        footerUi()
    }

    override fun handleNetworkState(networkState: NetworkState?) {
        manageUserTouch(networkState)
        when (networkState) {
            LOADING -> {
                showloader()
                hidePhoneErrorMessage()
            }
            LOADED -> {
            }
            ERROR -> {
                enableSubmit()
            }
        }
    }

    override fun handleEvents(action: Action?) {
        when (action?.actionId) {
            LoginViewModel.APP_VERSION_NOT_SUPPORTED_ERROR -> {
                showAppUpdateDialog();
            }
            LoginViewModel.MOBILE_UNREGISTERED_ERROR -> {
                showPhoneErrorMessage()
                disableSubmit()
            }
            LoginViewModel.OTP_GENERATED -> {
                Utils.showToast(context, getString(R.string.otp_msg, loginViewModel.userMobileNo))
                navigateTo(R.id.action_loginPhoneFragment_to_loginOtpFragment)
            }
        }
    }

    private fun showAppUpdateDialog() {
        context?.let {
            val dialogInfo = AppDialogInfo(
                title = getString(R.string.app_update_required),
                subtitle1 = getString(R.string.app_version_unsupported),
                actionRightLabel = getString(R.string.app_update_label),
                isSingleAction = true
            )
            DialogUtils.showAppDialog(context = context,
                dialogInfo = dialogInfo,
                actionLeftClick = null,
                actionRightClick = {
                    Utils.goToPlayStore(context)
                }
            )
        }
    }

    private fun showPhoneErrorMessage() {
        phoneErrorMsgTxtView.text = getString(R.string.phone_no_error)
        phoneErrorMsgTxtView.visibility = View.VISIBLE
        phoneErrorImageView.visibility = View.VISIBLE
    }

    private fun hidePhoneErrorMessage() {
        if (phoneErrorImageView.visibility == View.VISIBLE) {
            phoneErrorMsgTxtView.visibility = View.INVISIBLE
            phoneErrorImageView.visibility = View.INVISIBLE
        }
    }

    private fun showloader() {
        submitRoot.isClickable = false
        loader.visibility = View.VISIBLE
        loader.startAnimation(AnimationUtils.getRotateAnimation())
        submitContent.visibility = View.GONE
    }

    private fun hideloader() {
        submitRoot.isClickable = true
        loader.visibility = View.GONE
    }

    private fun footerUi() {
        submitRoot = view!!.findViewById(R.id.submit) as RelativeLayout
        loader = submitRoot.findViewById(R.id.loader)
        resend = view!!.findViewById(R.id.resend)
        submitContent = submitRoot.findViewById(R.id.submit_content)
        submitLabel = submitRoot.findViewById(R.id.button_text)
        submitLabel.text = getString(R.string.next)
        submitRoot.setOnClickListener {
            getViewModel().triggerOtp(phoneEditText.text.toString())
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

    private fun phoneLayoutUi() {
        phoneLayout = view!!.findViewById(R.id.phone_root)
        phoneLayout.run {
            phoneErrorMsgTxtView = findViewById(R.id.error_msg)
            phoneErrorImageView = findViewById(R.id.clear)
            phoneEditText = findViewById(R.id.content)
            phoneEditTextLabel = findViewById(R.id.label)
        }
        phoneEditTextLabel.text = getString(R.string.enter_phone)
        phoneEditText.run {
            inputType = InputType.TYPE_CLASS_NUMBER
            filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10))
            launchKeyboard()
            afterTextChanged {
                hidePhoneErrorMessage()
                when (it.length) {
                    10 -> enableSubmit()
                    else -> disableSubmit()
                }
            }
        }
        clearOtpIv = view!!.findViewById(R.id.clear)
        clearOtpIv.setOnClickListener { phoneEditText.setText("") }
    }

    override fun getMenuResId(): Int = 0

    override fun onMenuItemClick(item: MenuItem?): Boolean = true

    override fun showNavigationBack() = false

    override fun onBackPressed() {
        checkActivityStatus {
            activity?.finish()
        }
    }


}