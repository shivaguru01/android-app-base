package com.app.base.permission


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.base.common.APP_TAG
import com.app.base.model.PermissionStatus
import java.util.*

object PermissionManager {

    private fun shouldAskPermission(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    fun checkAppPermissions(activity: AppCompatActivity): PermissionStatus {
        val permissionPending = ArrayList<String>()
        if (shouldAskPermission()) {
            try {
                val permissionsForApp = activity.packageManager
                    .getPackageInfo(activity.packageName, PackageManager.GET_PERMISSIONS)
                    .requestedPermissions
                if (permissionsForApp != null) {
                    for (permission in permissionsForApp) {
                        if (ContextCompat.checkSelfPermission(
                                activity,
                                permission
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            permissionPending.add(permission)
                        }
                    }
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
        }
        return PermissionStatus(pendingPermission = permissionPending.toTypedArray())
    }

    fun requestPermission(activity: Activity, permissionNeededArray: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, permissionNeededArray, requestCode)
    }

    fun verifyAppPermission(
        permissionReqCode: Int,
        reqCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ): PermissionStatus? {
        var permissionStatus: PermissionStatus? = null
        if (permissionReqCode == reqCode) {
            val pendingPermission = ArrayList<String>()
            for (i in grantResults.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    pendingPermission.add(permissions[i])
                }
            }
            permissionStatus = PermissionStatus(pendingPermission = pendingPermission.toTypedArray())
        }
        return permissionStatus
    }

    /**
     * if anyone of the permission in list has its rationale status set to false
     * we will return false.
     *
     * @param activity
     * @param permissionList
     * @return
     */
    fun hasUserDeniedPermissionPreviously(activity: AppCompatActivity, permissionList: Array<String>): Boolean {
        for (permission in permissionList) {
            if (!hasUserDeniedPermissionPreviously(activity, permission)) {
                return false
            }
        }
        return true
    }

    private fun hasUserDeniedPermissionPreviously(activity: AppCompatActivity, permission: String): Boolean {
        val status = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
        Log.d(APP_TAG, "Status : $status")
        return status
    }

    fun startApplicationSettingActivity(activityContext: AppCompatActivity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromParts("package", activityContext.packageName, null)
        intent.data = uri
        activityContext.startActivity(intent)
    }


}
