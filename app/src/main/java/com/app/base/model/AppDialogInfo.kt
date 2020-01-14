package com.app.base.model

data class AppDialogInfo @JvmOverloads constructor(
    val title: String? = "",
    val subtitle1: String? = "",
    val subtitle2: String? = "",
    val iconResId: Int = 0,
    val hideIcon: Boolean = false,
    val isSingleAction: Boolean = false,
    val actionLeftLabel: String? = "",
    val actionRightLabel: String? = ""
)