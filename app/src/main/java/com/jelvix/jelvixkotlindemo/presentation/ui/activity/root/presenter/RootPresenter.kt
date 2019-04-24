/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.ui.activity.root.presenter

import com.arellomobile.mvp.InjectViewState
import com.jelvix.jelvixkotlindemo.presentation.presenter.BasePresenter
import com.jelvix.jelvixkotlindemo.presentation.ui.activity.root.IRootView
import javax.inject.Inject

@InjectViewState
class RootPresenter @Inject constructor() : BasePresenter<IRootView>()