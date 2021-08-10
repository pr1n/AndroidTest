package com.pr1n.repository.base

import com.pr1n.repository.local.LocalService
import com.pr1n.repository.remote.RemoteService

interface IRepository {
    val remote: RemoteService
    val local: LocalService
}