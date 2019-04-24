/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.presenter.strategy

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.ViewCommand
import com.arellomobile.mvp.viewstate.strategy.StateStrategy
import com.jelvix.jelvixkotlindemo.presentation.util.AppConstants

class ProgressDialogStrategy : StateStrategy {

    override fun <View : MvpView> beforeApply(currentState: MutableList<ViewCommand<View>>, incomingCommand: ViewCommand<View>) {
        val iterator = currentState.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()

            if (entry.strategyType == ProgressDialogStrategy::class.java) {
                iterator.remove()
                break
            }
        }
        currentState.add(incomingCommand)
    }

    override fun <View : MvpView> afterApply(currentState: MutableList<ViewCommand<View>>, incomingCommand: ViewCommand<View>) {
        if (incomingCommand.tag == AppConstants.ACTION_HIDE) {
            currentState.remove(incomingCommand)
        }
    }
}