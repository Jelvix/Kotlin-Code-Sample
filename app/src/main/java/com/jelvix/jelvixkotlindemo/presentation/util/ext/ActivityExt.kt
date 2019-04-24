/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.util.ext

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    val windowToken = currentFocus?.windowToken
    if (windowToken != null) {
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }
}