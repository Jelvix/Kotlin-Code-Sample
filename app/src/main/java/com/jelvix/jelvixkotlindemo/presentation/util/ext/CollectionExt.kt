/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.util.ext

fun <E> MutableList<E>.mergeNewItemList(items: List<E>) {
    items.forEach { item ->
        if (contains(item)) {
            val position = indexOf(item)
            removeAt(position)
            add(position, item)
        } else {
            add(item)
        }
    }
}