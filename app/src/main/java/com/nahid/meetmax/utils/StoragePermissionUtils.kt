package com.nahid.meetmax.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.nahid.meetmax.R


fun Context.checkStoragePermission(): Boolean {

    val granted = PackageManager.PERMISSION_GRANTED


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val hasMediaAudio =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO)
        val hasMediaVideo =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO)
        val hasMediaImage =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)

        return hasMediaAudio == granted && hasMediaVideo == granted && hasMediaImage == granted


    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager()
        } else {
            val writePermission = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val readPermission = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )

            return writePermission == granted && readPermission == granted
        }

    }

}

/**
Check Permission API Version Wise
 */
fun Activity.checkStoragePermissionResult(permissions:Map<String, @JvmSuppressWildcards Boolean>,
                                          listener: (Boolean) -> Unit){

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        checkGrantResult(this,permissions){
            listener(it)
        }
    } else {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                listener(false)
            }
        } else {
            checkGrantResult(this,permissions){
                listener(it)
            }
        }
    }

}

/**
Check Every Storage Related Permission  (Read,Write)
 */
fun checkGrantResult(activity: Activity, permissions: Map<String, Boolean>, listener: (Boolean) -> Unit) {
    var allGranted = true
    var permissionName = ""

    for (permission in permissions) {
        permissionName = permission.key
        val isGranted = permission.value
        if (!isGranted) {
            allGranted = false
            break
        }
    }

    if (!allGranted) {
        val hasPermission = permissions.getOrDefault(permissionName, false)
        if (!hasPermission) {
            var selectRational = activity.shouldShowRequestPermissionRationale(permissionName)//Return false when selected always deny
            if (selectRational) {//Ask Permission Second Time
                StoragePermissionUtils.askRationalePermissionDialog(activity) {
                    if (it) {//Ok Selected
                        listener(false)
                    }
                }
            }else{//Never Ask Again Chosen
                StoragePermissionUtils.askSettingsPermissionDialog(activity)
            }
        }
    }else{
        listener(true)
    }

}

object StoragePermissionUtils {
    fun askSettingsPermissionDialog(context: Context) {

        val message = "Since Storage Permission has not been granted, this app will not be able to store data.\n Press OK and go to Permissions -> Photos and Videos/ Storage -> Select Allow ."

        CustomDialog.createPositiveOnlyDialog(
            context, "Functionality Limited", message, R.color.theme
        ) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts(
                "package", context.packageName, null
            )
            intent.data = uri
            context.startActivity(intent)
        }
    }

    fun askRationalePermissionDialog(context: Context, listener:(Boolean) -> Unit) {

        val message = "Since Storage Permission has not been granted, this app will not be able to store data.\n Press OK and Allow Permissions."

        CustomDialog.createPositiveOnlyDialog(
            context, "Functionality Limited", message, R.color.theme
        ) {
            listener(true)
        }
    }
}