package com.app.core.ui

import androidx.navigation.Navigation
import com.app.core.R
import com.app.core.base.BaseActivity

class HomeActivity : BaseActivity() {

    override fun activityLayoutSettings() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    override fun getLayoutId(): Int = R.layout.activity_home_bottom_view

}