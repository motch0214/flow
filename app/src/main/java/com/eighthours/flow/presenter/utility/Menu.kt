package com.eighthours.flow.presenter.utility

import android.content.Context
import android.support.annotation.IdRes
import android.support.v7.widget.PopupMenu
import android.view.View

class Menu(
        context: Context,
        anchor: View,
        @IdRes resourceId: Int,
        vararg selections: Pair<Int, () -> Unit>)
    : PopupMenu(context, anchor) {

    private val selection = selections.toMap()

    init {
        menuInflater.inflate(resourceId, menu)
        setOnMenuItemClickListener { menu ->
            val item = selection[menu.itemId]
            item?.invoke()
            item != null
        }
    }
}
