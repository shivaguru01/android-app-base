package com.app.core.model

data class PermissionStatus(val pendingPermission: Array<String>) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (this.javaClass != other?.javaClass) return false

        other as PermissionStatus

        if (!pendingPermission.contentEquals(other.pendingPermission)) return false

        return true
    }

    override fun hashCode(): Int  = pendingPermission.contentHashCode()

    fun isAllPermissionGranted(): Boolean {
        return pendingPermission.isNullOrEmpty()
    }

}