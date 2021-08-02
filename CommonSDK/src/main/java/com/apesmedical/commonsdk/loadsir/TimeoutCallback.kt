package com.apesmedical.commonsdk.loadsir

import com.apesmedical.commonsdk.R
import com.kingja.loadsir.callback.Callback

class TimeoutCallback : Callback() {
    override fun onCreateView() = R.layout.loadsir_timeout
}