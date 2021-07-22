package com.apesmedical.commonsdk.UiKnife

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner

/**
 * Created by Beetle_Sxy on 2019-06-21.
 * 布局 ui 帮助
 */
interface UiHplesInte {

    fun bind(container: LifecycleOwner?, uiConfig: UIConfig?): ViewGroup?

    fun setAction(actionHples: ActionHplesInte, onCallback: (ViewGroup.() -> Unit)? = null): ActionHplesInte
    fun setNavigation(navigationHples: NavigationHplesInte, onCallback: (ViewGroup.() -> Unit)? = null): NavigationHplesInte

    fun <T : ActionHplesInte> getActionHples(): T?
    fun <T : NavigationHplesInte> getNavigationHples(): T?

    fun getRootLayout(): ViewGroup?
    fun getLayout(): View?
}