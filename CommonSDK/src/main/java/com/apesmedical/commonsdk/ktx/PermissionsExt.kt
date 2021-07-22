package com.library.sdk.ext

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.Utils

/**
 * Created by Beetle_Sxy on 2019/4/29.
 */
fun permissions(@PermissionConstants.PermissionGroup vararg permissions: String, onCallback: (Boolean) -> Unit) {
	if (PermissionUtils.isGranted(*permissions)) {
		onCallback(true)
	} else {
		PermissionUtils.permission(*permissions)
			.callback(object : PermissionUtils.SimpleCallback {
				override fun onGranted() {
					onCallback(true)
				}
				
				override fun onDenied() {
					onCallback(false)
				}
			})
            .rationale { _, _ ->
                //permissions()
                PermissionUtils.launchAppDetailsSettings()
            }
            .request()
    }
}

@SuppressLint("ObsoleteSdkInt")
private fun permissions() {
    ActivityUtils.startActivity(Intent().apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        Build.VERSION.RELEASE
        if (Build.VERSION.SDK_INT >= 9) {
            action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            data = Uri.fromParts("package", Utils.getApp().packageName, null)
        } else if (Build.VERSION.SDK_INT <= 8) {
            action = Intent.ACTION_VIEW
            setClassName("com.android.settings", "com.android.settings.InstalledAppDetails")
            putExtra("com.android.settings.ApplicationPkgName", Utils.getApp().packageName)
        }
    })
}

/**
 * 或者权限组（真实权限字段）
 */
fun getPermissionsGroup(@PermissionConstants.PermissionGroup vararg permissions: String): Array<String> {
	val permissionsGroup: MutableList<String> = mutableListOf()
	for (permission in permissions) {
		permissionsGroup.addAll(PermissionConstants.getPermissions(permission))
	}
	return permissionsGroup.toTypedArray()
}