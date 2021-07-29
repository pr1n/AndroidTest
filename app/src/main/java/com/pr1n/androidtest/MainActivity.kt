package com.pr1n.androidtest

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apes.annotation.nav.Destination
import com.apesmedical.commonsdk.UiKnife.UIConfig
import com.apesmedical.commonsdk.base.newbase.BaseVMTripleView
import com.apesmedical.commonsdk.base.newbase.NoDataException
import com.apesmedical.commonsdk.base.newbase.ext.gone
import com.apesmedical.commonsdk.base.newbase.ext.show
import com.apesmedical.commonsdk.base.newbase.ext.visibility
import com.bumptech.glide.Glide
import com.library.sdk.ext.logi
import com.pr1n.androidtest.databinding.ActivityMainBinding
import com.pr1n.androidtest.databinding.ItemBinding
import com.pr1n.androidtest.databinding.ItemFooterBinding
import com.pr1n.androidtest.viewmodel.ViewModel1
import com.pr1n.androidtest.viewmodel.ViewModel2
import com.pr1n.androidtest.viewmodel.ViewModel3
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.koin.androidx.viewmodel.ext.android.getStateViewModel


@UIConfig(isAction = false, title = "Main")
class MainActivity :
    BaseVMTripleView<ActivityMainBinding, ViewModel1, ViewModel2, ViewModel3>(R.layout.activity_main) {

    override fun initView(savedInstanceState: Bundle?) {
        logi("viewModel1: $viewModel1")
        logi("viewModel2: $viewModel2")
        logi("viewModel3: $viewModel3")
        logi("getViewModel1: ${getStateViewModel<ViewModel1>()}")
        logi("getViewModel2: ${getStateViewModel<ViewModel2>()}")
        logi("getViewModel3: ${getStateViewModel<ViewModel3>()}")

//        setContent {
//            AppCompatTheme {
//                HostNavScreen()
//            }
//        }

        //viewModel2.resultData.observer {}

        val adapter = DoctorListAdapter()
        mDataBinding.recyclerView.adapter = adapter.getBaseFooterAdapter()
        (mDataBinding.recyclerView.layoutManager as GridLayoutManager).spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) = if (adapter.itemCount - 1 < position) 2 else 1
            }
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel1.getPagerData().collectLatest(adapter::submitData)
        }
        //buildNavGraph(mDataBinding.containerView.id)
    }
}

class DoctorListAdapter :
    PagingDataAdapter<DoctorList.Doctor, DoctorListAdapter.MyViewHolder>(object :
        DiffUtil.ItemCallback<DoctorList.Doctor>() {
        override fun areItemsTheSame(oldItem: DoctorList.Doctor, newItem: DoctorList.Doctor) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: DoctorList.Doctor, newItem: DoctorList.Doctor) =
            oldItem.userId == newItem.userId
    }) {

    fun getBaseFooterAdapter() = withLoadStateFooter(FooterLoadStateAdapter(::retry))

    inner class MyViewHolder(binding: ItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imageView = binding.imageView
        private val textView = binding.textView
        fun convert(doctor: DoctorList.Doctor?) {
            Glide.with(imageView).load(doctor?.avatar).into(imageView)
            textView.text = doctor?.name
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.convert(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemBinding.inflate(LayoutInflater.from(parent.context)))
    }
}

class FooterLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<FooterLoadStateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        ViewHolder(ItemFooterBinding.inflate(LayoutInflater.from(parent.context)), retry)

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class ViewHolder(binding: ItemFooterBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        private val loadingText = binding.loadingText
        private val errorText = binding.errorText.apply { setOnClickListener { retry() } }

        fun bind(loadState: LoadState) {
            when (loadState) {
                is LoadState.NotLoading -> {
                    loadingText.gone()
                    errorText.gone()
                }
                LoadState.Loading -> {
                    loadingText.show()
                    errorText.gone()
                }
                is LoadState.Error -> {
                    loadingText.gone()
                    errorText.show()
                    errorText.text = if (loadState.error is NoDataException) "加载完成" else "加载失败"
                }
            }
        }
    }
}

@Destination("main/test")
class TestActivity : ComponentActivity()

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