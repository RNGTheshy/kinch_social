package com.nino.blindbox.base

/**
 * @description: 权限申请回调的接口
 */
interface PermissionListener {
    open fun onGranted()
    open fun onDenied(deniedPermissions: MutableList<String?>?)
}