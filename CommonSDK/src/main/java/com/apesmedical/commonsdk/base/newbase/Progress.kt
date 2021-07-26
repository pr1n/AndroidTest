package com.apesmedical.commonsdk.base.newbase

data class Progress(
    val progress: Int,      // 当前进度 0-100
    val currentSize: Long,  // 当前已完成的字节大小
    val totalSize: Long     // 总字节大小
)