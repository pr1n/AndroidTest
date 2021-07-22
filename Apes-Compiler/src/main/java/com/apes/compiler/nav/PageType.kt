package com.apes.compiler.nav

@JvmInline
value class PageType private constructor(val type: String) {
    companion object {
        val PAGE_TYPE_ACTIVITY = PageType("Activity")
        val PAGE_TYPE_FRAGMENT = PageType("Fragment")
        val PAGE_TYPE_DIALOG = PageType("DialogFragment")
    }
}