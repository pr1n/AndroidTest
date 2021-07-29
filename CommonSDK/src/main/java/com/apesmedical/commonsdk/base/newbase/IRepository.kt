package com.apesmedical.commonsdk.base.newbase

interface IRepository {
    val remote: RemoteService
    val local: LocalService
}