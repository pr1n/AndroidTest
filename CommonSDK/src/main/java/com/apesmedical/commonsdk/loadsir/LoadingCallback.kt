package com.apesmedical.commonsdk.loadsir

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.apesmedical.commonsdk.R
import com.kingja.loadsir.callback.Callback

class LoadingCallback : Callback() {
    override fun onCreateView() = R.layout.loadsir_loading

    private lateinit var objectAnimator: ObjectAnimator

    override fun onAttach(context: Context?, view: View?) {
        super.onAttach(context, view)

        objectAnimator = ObjectAnimator.ofFloat(
            view?.findViewById<ImageView>(R.id.loadingImg),
            "rotation",
            0F,
            360F
        ).also {
            it.interpolator = LinearInterpolator()
            it.repeatCount = ValueAnimator.INFINITE
            it.duration = 3000
            it.start()
        }
    }

    override fun onDetach() {
        super.onDetach()
        objectAnimator.cancel()
    }

}