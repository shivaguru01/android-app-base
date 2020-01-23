package com.app.core.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.app.core.R
import com.app.core.base.BaseFragment
import com.app.core.model.Action
import com.app.core.model.NetworkState
import com.app.core.ui.HomeActivity
import com.app.core.viewmodel.SplashViewModel
import javax.inject.Inject

class SplashFragment : BaseFragment<SplashViewModel>() {

    @Inject
    lateinit var splashViewModel: SplashViewModel

    private var handler = Handler()

    override fun getViewModel(): SplashViewModel = splashViewModel

    override fun getAppBarTitle(): String = getString(R.string.welcome)

    override fun getFragmentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.splash_screen, container, false);

    override fun setUi() {
        navigate()
    }

    private fun navigate() {
        handler.removeCallbacksAndMessages(null);
        if (getViewModel().isLoggedIn() || true) {
            launchHomeActivity()
        } else {
            launchLoginScreen()
        }
    }

    private fun launchLoginScreen() {
        checkActivityStatus {
            handler.postDelayed({
                navigateTo(R.id.action_splashFragment_to_loginPhoneFragment)
            }, 800)
        }
    }

    private fun launchHomeActivity() {
        checkActivityStatus {
            handler.postDelayed({
                startActivity(Intent(context, HomeActivity::class.java))
            }, 800)
        }
    }

    override fun getMenuResId(): Int = 0

    override fun onMenuItemClick(item: MenuItem?): Boolean = true

    override fun showNavigationBack() = true

    override fun handleNetworkState(networkState: NetworkState?) {}

    override fun handleEvents(action: Action?) {}

}