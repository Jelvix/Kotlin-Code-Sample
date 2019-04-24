/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.util.alert

class Alert private constructor() {

    companion object {
        fun newBuilder(): Builder {
            return Alert().Builder()
        }

        const val ERROR : Long = 0
        const val SUCCESS : Long = 1
    }

    var type : Long? = null
        private set
    var title: String? = null
        private set
    var message: String? = null
        private set
    var doOnSuccess: (() -> Unit)? = null
        private set
    var successText: String? = null
        private set
    var doOnError: (() -> Unit)? = null
        private set
    var doOnCancel: (() -> Unit)? = null
        private set
    var cancelText: String? = null
        private set
    var isCancelable: Boolean? = null
        private set


    inner class Builder {

        fun setType(type: Long): Builder {
            this@Alert.type = type
            return this
        }

        fun setTitle(title: String?): Builder {
            this@Alert.title = title
            return this
        }

        fun setMessage(message: String?): Builder {
            this@Alert.message = message
            return this
        }

        fun setDoOnSuccess(doOnSuccess: (() -> Unit)?, successText: String? = null): Builder {
            this@Alert.doOnSuccess = doOnSuccess
            this@Alert.successText = successText
            return this
        }

        fun setDoOnError(doOnError: (() -> Unit)?): Builder {
            this@Alert.doOnError = doOnError
            return this
        }

        fun setDoOnCancel(doOnCancel: (() -> Unit)?, cancelText: String? = null): Builder {
            this@Alert.doOnCancel = doOnCancel
            this@Alert.cancelText = cancelText
            return this
        }

        fun setCancelable(isCancelable: Boolean?): Builder {
            this@Alert.isCancelable = isCancelable
            return this
        }

        fun create(): Alert {
            return this@Alert
        }
    }
}