/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.util

import android.text.format.DateUtils
import java.util.*

object DateTimeUtils {

    fun formatRelativeDays(date: Date?): String {
        date ?: return ""
        return DateUtils.getRelativeTimeSpanString(date.time, Calendar.getInstance().timeInMillis, DateUtils.DAY_IN_MILLIS).toString()
    }
}