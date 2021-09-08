package com.pr1n.androidtest

import android.graphics.Outline
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.apesmedical.commonsdk.base.newbase.exception.NoDataException
import com.apesmedical.commonsdk.base.newbase.ext.gone
import com.apesmedical.commonsdk.base.newbase.ext.show
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.pr1n.androidtest.databinding.ItemBannerBinding
import com.pr1n.androidtest.databinding.ItemDoctorBinding
import com.pr1n.androidtest.databinding.ItemFooterBinding
import com.pr1n.repository.entity.Doctor
import com.pr1n.repository.entity.DoctorList

class DoctorAdapter :
    PagingDataAdapter<Doctor, DoctorAdapter.ViewHolder>(object :
        DiffUtil.ItemCallback<Doctor>() {
        override fun areItemsTheSame(oldItem: Doctor, newItem: Doctor) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Doctor, newItem: Doctor) =
            oldItem.userId == newItem.userId
    }) {

    fun getBaseFooterAdapter() = withLoadStateFooter(FooterLoadStateAdapter(::retry))

    class ViewHolder(binding: ItemDoctorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imageView = binding.imageView
        private val textView = binding.textView
        fun convert(doctor: Doctor?) {
            imageView.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setOval(0, 0, view.width, view.width)
                }
            }
            imageView.clipToOutline = true
            Glide.with(imageView).load(doctor?.avatar).into(imageView)
            textView.text = doctor?.name
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.convert(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemDoctorBinding.inflate(LayoutInflater.from(parent.context)))
    }
}

class BannerAdapter :
    BaseQuickAdapter<DoctorList.Banner, BaseViewHolder>(R.layout.item_banner) {

    override fun convert(holder: BaseViewHolder, item: DoctorList.Banner) {
        val binding = ItemBannerBinding.bind(holder.itemView)
        Glide.with(binding.bannerImg).load(item.image).into(binding.bannerImg)
        binding.bannerTitle.text = item.title
    }
}

class DoctorListAdapter :
    PagingDataAdapter<DoctorList.Doctor, DoctorListAdapter.ViewHolder>(object :
        DiffUtil.ItemCallback<DoctorList.Doctor>() {
        override fun areItemsTheSame(oldItem: DoctorList.Doctor, newItem: DoctorList.Doctor) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: DoctorList.Doctor, newItem: DoctorList.Doctor) =
            oldItem.userId == newItem.userId
    }) {

    fun getBaseFooterAdapter() = withLoadStateFooter(FooterLoadStateAdapter(::retry))

    class ViewHolder(binding: ItemDoctorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imageView = binding.imageView
        private val textView = binding.textView
        fun convert(doctor: DoctorList.Doctor?) {
            imageView.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setOval(0, 0, view.width, view.width)
                }
            }
            imageView.clipToOutline = true
            Glide.with(imageView).load(doctor?.avatar).into(imageView)
            textView.text = doctor?.name
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.convert(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemDoctorBinding.inflate(LayoutInflater.from(parent.context)))
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