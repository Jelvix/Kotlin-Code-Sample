/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.ui.activity.base.presenter

import com.arellomobile.mvp.InjectViewState
import com.jelvix.jelvixkotlindemo.presentation.presenter.BasePresenter
import com.jelvix.jelvixkotlindemo.presentation.ui.activity.base.IBaseView
import javax.inject.Inject

@InjectViewState
class BaseActivityPresenter @Inject constructor() : BasePresenter<IBaseView>()