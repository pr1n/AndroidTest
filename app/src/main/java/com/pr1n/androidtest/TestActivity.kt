package com.pr1n.androidtest

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.apesmedical.commonsdk.UiKnife.UIConfig
import com.apesmedical.commonsdk.base.newbase.BaseVMView
import com.apesmedical.commonsdk.loadsir.observeAndShow
import com.apesmedical.commonsdk.loadsir.regLoadSir
import com.apesmedical.commonsdk.loadsir.show
import com.library.sdk.ext.logi
import com.pr1n.androidtest.databinding.ActivityTestBinding
import com.pr1n.androidtest.viewmodel.ViewModel1
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@UIConfig(isAction = true, title = "Test")
class TestActivity : BaseVMView<ActivityTestBinding, ViewModel1>(R.layout.activity_test) {

    override fun initView(savedInstanceState: Bundle?) {
        val bannerAdapter = BannerAdapter()
        val bannerLoadSir = mDataBinding.banner.regLoadSir { viewModel.getBanner() }
        PagerSnapHelper().attachToRecyclerView(mDataBinding.banner)
        mDataBinding.banner.adapter = bannerAdapter
        viewModel.getBannerLiveData.observeAndShow(this, bannerLoadSir){
            it?.let{ bannerAdapter.addData(it.banner)}
        }
        viewModel.getBanner()

        val doctorAdapter = DoctorAdapter()
        val doctorLoadSir = mDataBinding.doctorRecyclerView.regLoadSir { doctorAdapter.refresh() }
        doctorAdapter.addLoadStateListener(doctorLoadSir::show)
        mDataBinding.doctorRecyclerView.adapter = doctorAdapter.getBaseFooterAdapter()
        lifecycleScope.launch { viewModel.getDoctorList().collectLatest(doctorAdapter::submitData) }
    }

}