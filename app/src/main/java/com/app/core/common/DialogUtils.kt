package com.app.core.common

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.app.core.R
import com.app.core.databinding.AppDialogBinding
import com.app.core.model.AppDialogInfo

object DialogUtils {

    @MainThread
    fun showAppDialog(
        context: Context?,
        dialogInfo: AppDialogInfo,
        actionLeftClick: (() -> Unit)?,
        actionRightClick: (() -> Unit)?
    ): AlertDialog {
        lateinit var appDialogBinding: AppDialogBinding;
        lateinit var alertDialog: AlertDialog

        val alertDialogBuilder = AlertDialog.Builder(
            context!!,
            R.style.ThemeOverlay_AppCompat_Dialog
        )
        val inflater = LayoutInflater.from(context)
        appDialogBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.app_dialog,
            null,
            false
        );

        val appDialogView = appDialogBinding.root
        appDialogView.findViewById<TextView>(R.id.action_left).let { textView ->
            textView.setOnClickListener {
                alertDialog.dismiss()
                actionLeftClick?.invoke()
            }
        }
        appDialogView.findViewById<TextView>(R.id.action_right).run {
            setOnClickListener {
                alertDialog.dismiss()
                actionRightClick?.invoke()
            }
        }
        alertDialog = alertDialogBuilder.apply {
            setCancelable(false)
            setView(appDialogView)
            appDialogBinding.dialogInfo = dialogInfo
            appDialogBinding.executePendingBindings()
        }.show()
        return alertDialog
    }


}