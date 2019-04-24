/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package  com.jelvix.jelvixkotlindemo.presentation.util.alert

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.jelvix.jelvixkotlindemo.R

class AlertDialogHelper (private val context: Context) {

    private var alertDialog: AlertDialog? = null
    var doOnDismiss: (() -> Unit)? = null

    fun showAlertDialog(message: String, doOnError: (() -> Unit)?) {
        val alertBuilder = Alert.newBuilder().setMessage(message).setDoOnError(doOnError).create()
        showAlertDialog(alertBuilder)
    }

    fun showAlertDialog(alert: Alert) {
        if (alertDialog != null) return
        val alertDialogBuilder = AlertDialog.Builder(context, R.style.AppAlertDialog)
        when (alert.type) {
            Alert.ERROR -> alertDialogBuilder.setTitle(R.string.warning)
            Alert.SUCCESS -> alertDialogBuilder.setTitle(R.string.success)
            else -> alertDialogBuilder.setTitle(alert.title)
        }
        if (alert.message != null) {
            alertDialogBuilder.setMessage(alert.message)
        }
        alertDialogBuilder.setOnDismissListener { _ ->
            alert.doOnError?.invoke()
            doOnDismiss?.invoke()
        }
        alertDialogBuilder.setPositiveButton(alert.successText ?: context.getString(R.string.ok)) { _, _ ->
            alert.doOnSuccess?.invoke()
        }
        if (alert.doOnCancel != null) {
            alertDialogBuilder.setNegativeButton(alert.cancelText ?: context.getString(R.string.cancel)) { _, _ ->
                alert.doOnCancel?.invoke()
            }
        }
        if (alert.isCancelable != null) {
            alertDialogBuilder.setCancelable(alert.isCancelable ?: false)
        }

        alertDialog = alertDialogBuilder.create()
        alertDialog?.show()
    }

    fun dismissDialog(isActivityDestroy: Boolean) {
        if (alertDialog == null) {
            return
        }
        if (isActivityDestroy) {
            alertDialog?.setOnDismissListener(null)
        }
        dismissDialog()
    }

    fun dismissDialog() {
        if (alertDialog == null) {
            return
        }
        if (alertDialog?.isShowing == true) {
            alertDialog?.dismiss()
        }
        alertDialog = null
    }
}