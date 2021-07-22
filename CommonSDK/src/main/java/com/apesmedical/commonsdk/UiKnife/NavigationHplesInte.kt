package com.apesmedical.commonsdk.UiKnife

import android.view.View
import android.view.ViewGroup

/**
 * Created by Beetle_Sxy on 2019-06-22.
 */
interface NavigationHplesInte {
    fun bind(viewGroup: ViewGroup):ViewGroup
    fun getView(): View?
}