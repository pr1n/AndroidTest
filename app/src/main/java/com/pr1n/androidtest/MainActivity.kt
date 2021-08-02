package com.pr1n.androidtest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apes.annotation.nav.Destination
import com.apesmedical.commonsdk.base.newbase.BaseVMTripleView
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.library.sdk.ext.logi
import com.pr1n.androidtest.databinding.ActivityMainBinding
import com.pr1n.androidtest.screen.HostNavScreen
import com.pr1n.androidtest.viewmodel.ViewModel1
import com.pr1n.androidtest.viewmodel.ViewModel2
import com.pr1n.androidtest.viewmodel.ViewModel3
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.koin.androidx.viewmodel.ext.android.getStateViewModel


//@UIConfig(isAction = false, title = "Main")
class MainActivity :
    BaseVMTripleView<ActivityMainBinding, ViewModel1, ViewModel2, ViewModel3>(R.layout.activity_main) {

    override fun initView(savedInstanceState: Bundle?) {
        logi("viewModel1: $viewModel1")
        logi("viewModel2: $viewModel2")
        logi("viewModel3: $viewModel3")
        logi("getViewModel1: ${getStateViewModel<ViewModel1>()}")
        logi("getViewModel2: ${getStateViewModel<ViewModel2>()}")
        logi("getViewModel3: ${getStateViewModel<ViewModel3>()}")

        setContent {
            AppCompatTheme {
                HostNavScreen()
            }
        }

//        val adapter = DoctorListAdapter()
//        mDataBinding.recyclerView.adapter = adapter.getBaseFooterAdapter()
//        (mDataBinding.recyclerView.layoutManager as GridLayoutManager).spanSizeLookup =
//            object : GridLayoutManager.SpanSizeLookup() {
//                override fun getSpanSize(position: Int) =
//                    if (adapter.itemCount - 1 < position) 2 else 1
//            }
//
//        val loadsir = mDataBinding.recyclerView.regLoadSir { adapter.refresh() }
//
//        lifecycleScope.launch(Dispatchers.IO) {
//            viewModel1.getPagerData().collectLatest(adapter::submitData)
//        }
//        adapter.addLoadStateListener(loadsir::show)
    }
}

@Destination("main/first", true)
class FirstFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false).also {
            it.findViewById<TextView>(R.id.textView).onClick {
                findNavController().navigate("main/second")
            }
        }
    }
}

@Destination("main/second")
class SecondFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_second, container, false)
//            .also {
//            it.findViewById<TextView>(R.id.textView).onClick {
//                findNavController().navigate("user/home")
//            }
//        }
    }
}