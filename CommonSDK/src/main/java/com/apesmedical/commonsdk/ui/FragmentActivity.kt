package com.apesmedical.commonsdk.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView

/**
 * Created by Beetle_Sxy
 * on 2020/10/11.
 * Fragment 模版
 */
@Deprecated("未完成")
class FragmentActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(FragmentContainerView(this).also {
            
        })
    }

}