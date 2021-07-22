package com.apesmedical.commonsdk.base

import org.koin.java.KoinJavaComponent.get

interface IRepository {
    val remote get() = get<RemoteService>(RemoteService::class.java)
    val local get() = get<LocalService>(LocalService::class.java)
}