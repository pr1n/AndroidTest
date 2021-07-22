package com.apes.annotation.nav


@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class Destination(
    val pageUrl: String,
    val asStarter: Boolean = false
)
