/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.util.bus

import com.jelvix.jelvixkotlindemo.data.entity.MediaItem

sealed class RxBusEvent

class MediaItemAddedToFavorites(
    val mediaItem: MediaItem
) : RxBusEvent()

class MediaItemRemovedFromFavorites(
    val mediaItem: MediaItem
) : RxBusEvent()
