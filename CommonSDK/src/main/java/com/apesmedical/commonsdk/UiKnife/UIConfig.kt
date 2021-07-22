package com.apesmedical.commonsdk.UiKnife

import androidx.annotation.LayoutRes
import com.apesmedical.commonsdk.UiKnife.UiHplesInte
import kotlin.reflect.KClass

/**
 * Created by Beetle_Sxy on 2019-06-21.
 * 控制页面 ui 显示布局
 */
@Target(AnnotationTarget.CLASS)
annotation class UIConfig(//
    val isAction:Boolean = false,
    val title:String = "",
    @LayoutRes val layout: Int = -1,
    val help : KClass<out UiHplesInte> = DefaultUiHplesImpl::class
)

@Target(AnnotationTarget.CLASS)
annotation class ScrollViewConfig