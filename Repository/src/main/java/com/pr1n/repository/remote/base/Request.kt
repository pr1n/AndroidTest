package com.pr1n.repository.remote.base

import okhttp3.RequestBody
import java.io.File

data class Request constructor(
    val config: RequestConfig,
    val url: String?,
    val urlFormat: Pair<String, Array<out Any>>?,
    val headers: Map<String, String>,
    val params: Map<String, Any>,
    val querys: Map<String, Any>,
    val files: Map<String, File>,
    val parts: List<RequestBody>
)