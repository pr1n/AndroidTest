package com.apesmedical.commonsdk.base

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.apesmedical.commonsdk.UiKnife.UIConfig
import com.apesmedical.commonsdk.http.Complete
import com.apesmedical.commonsdk.http.Failure
import com.apesmedical.commonsdk.http.Loading
import com.apesmedical.commonsdk.http.ResultData
import com.apesmedical.commonsdk.http.Success
import com.library.sdk.ext.logi

abstract class BaseVMView<DB : ViewDataBinding, VM : ViewModel>(@LayoutRes private val layoutRes: Int) :
    BaseView<DB>(layoutRes) {
    protected val viewModel by this.stateViewModel<VM>()
}

@UIConfig
abstract class BaseVMFragment<DB : ViewDataBinding, VM : ViewModel>(@LayoutRes private val layoutRes: Int) :
    BaseFragment<DB>(layoutRes) {
    protected val viewModel by stateViewModel<VM>()
}

@UIConfig
abstract class BaseAVMFragment<DB : ViewDataBinding, VM : ViewModel, AVM : ViewModel>(@LayoutRes private val layoutRes: Int) :
    BaseFragment<DB>(layoutRes) {
    protected val viewModel by stateViewModel<VM>()
    protected val activityViewModel by sharedStateViewModel<AVM>()
}

abstract class BaseVMTripleView<DB : ViewDataBinding, VM1 : ViewModel, VM2 : ViewModel, VM3 : ViewModel>(
    @LayoutRes private val layoutRes: Int
) :
    BaseView<DB>(layoutRes) {
    private val viewModelTriple = this.stateViewModelTriple<VM1, VM2, VM3>()
    protected val viewModel1 by viewModelTriple.first
    protected val viewModel2 by viewModelTriple.second
    protected val viewModel3 by viewModelTriple.third
}