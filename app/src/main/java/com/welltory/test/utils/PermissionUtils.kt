package com.welltory.test.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun isPermissionsGranted(permissions: Array<out String>, grantResults: IntArray, vararg perm: String): Boolean {
    return perm.all { permission ->
        val indexOfGrantResult = permissions.indexOf(permission)
        val grantResult = grantResults.getOrElse(indexOfGrantResult) { PackageManager.PERMISSION_DENIED }
        grantResult == PackageManager.PERMISSION_GRANTED
    }
}
