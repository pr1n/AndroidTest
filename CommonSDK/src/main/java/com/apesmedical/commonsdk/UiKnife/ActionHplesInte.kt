package com.apesmedical.commonsdk.UiKnife

import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.CoroutineScope

/**
 * Created by Beetle_Sxy on 2019-06-22.
 * 标题栏帮助
 */
interface ActionHplesInte {
    fun bind(viewGroup: ViewGroup): ViewGroup
    suspend fun setBackClick(onClickListener: suspend CoroutineScope.(View?) -> Unit): ActionHplesInte
}