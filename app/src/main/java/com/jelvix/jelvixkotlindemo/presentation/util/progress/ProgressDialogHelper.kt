/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.util.progress

import android.app.ProgressDialog
import android.content.Context
import com.jelvix.jelvixkotlindemo.R

class ProgressDialogHelper(private val context: Context) {

    val title: String = context.resources.getString(R.string.loading)
    val message: String = context.resources.getString(R.string.please_wait)

    private var dialog: ProgressDialog? = null

    fun showProgressDialog(title: String, message: String? = this.message) {
        dismissDialog()
        dialog = ProgressDialog.show(context, title, message ?: this.message, true, true)
    }

    fun showProgressDialog(message: String? = this.message) = showProgressDialog(title, message)

    fun dismissDialog() {
        dialog?.dismiss()
        dialog = null
    }
}