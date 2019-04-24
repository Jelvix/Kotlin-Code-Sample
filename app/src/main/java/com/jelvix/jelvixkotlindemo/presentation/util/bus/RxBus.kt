/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.util.bus

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

object RxBus {

    private val events: PublishSubject<RxBusEvent> by lazy {
        PublishSubject.create<RxBusEvent>()
    }

    fun getEvents() = events.retry().observeOn(AndroidSchedulers.mainThread())

    fun sendEvent(rxBusEvent: RxBusEvent) {
        events.onNext(rxBusEvent)
    }
}