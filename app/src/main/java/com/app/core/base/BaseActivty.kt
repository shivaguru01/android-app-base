package com.app.core.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import com.app.core.R
import com.app.core.listener.OnFragmentInteractionListener
import com.app.core.model.PermissionStatus
import com.app.core.permission.PermissionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity(), OnFragmentInteractionListener {

    private var settingDialog: AlertDialog? = null

    internal lateinit var navController: NavController;

    override fun getNavController(): NavController? = navController

    var bottomNavigationView: BottomNavigationView? = null;

    private companion object {
        const val permissionReqCode = 7777;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySettings()
        setContentView(getLayoutId())
        setUi()
        activityLayoutSettings()
    }

    override fun getBottomNavView(): BottomNavigationView? = bottomNavigationView


    private fun setUi() {
        //bottomNavigationView = findViewById(R.id.nav_view)
    }

    private fun activitySettings() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onResume() {
        super.onResume()
        validatePermissionResult(PermissionManager.checkAppPermissions(this))
    }

    abstract fun getLayoutId(): Int

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permissionStatus =
            PermissionManager.verifyAppPermission(permissionReqCode, requestCode, permissions, grantResults)
        permissionStatus?.let { validatePermissionResult(it) }
    }

    private fun validatePermissionResult(permissionStatus: PermissionStatus) {
        if (!permissionStatus.isAllPermissionGranted()) {
            // if user has denied and checked never ask again for
            // permission needed for tha app, we show
            // settings dialog to manage permissions
            if (PermissionManager.hasUserDeniedPermissionPreviously(this, permissionStatus.pendingPermission)) {
                showSettingDialog()
            } else {
                PermissionManager.requestPermission(this, permissionStatus.pendingPermission, permissionReqCode)
            }
        }
    }

    private fun dismissSettingDialog() {
        settingDialog?.dismiss()
    }

    private fun showSettingDialog() {
        if (settingDialog?.isShowing == true) return
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage(getString(R.string.permission_req_message))
            .setTitle(getString(R.string.permission_req_title))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.go_setting)) { _, _ ->
                dismissSettingDialog()
                PermissionManager.startApplicationSettingActivity(this)
                finishAndRemoveTask()
            }
            .setNegativeButton(getString(R.string.exit_app)) { _, _ ->
                dismissSettingDialog()
                finishAndRemoveTask()
            }
        settingDialog = alertDialogBuilder.create()
        settingDialog?.show()
    }

    abstract fun activityLayoutSettings()

    protected fun showBotNav() {
        bottomNavigationView?.visibility = View.VISIBLE
    }

    protected fun hideBotNav() {
        bottomNavigationView?.visibility = View.GONE
    }

}