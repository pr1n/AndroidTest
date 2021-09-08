package com.pr1n.repository.base

import com.apesmedical.commonsdk.base.newbase.IRepo
import com.pr1n.repository.local.LocalService
import com.pr1n.repository.remote.RemoteService

interface Repo: IRepo {
    val remote: RemoteService
    val local: LocalService
}