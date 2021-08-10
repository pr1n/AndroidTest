package com.pr1n.androidtest

import android.content.Context
import com.apesmedical.commonsdk.app.SDKAppLifecycles
import com.pr1n.repository.local.LocalService
import com.pr1n.repository.remote.base.RxHttpRemoteService
import com.pr1n.repository.remote.RemoteService
import com.pr1n.repository.local.db.MainDB
import com.apesmedical.commonsdk.delegate.AppLifecycles
import com.apesmedical.commonsdk.delegate.ConfigModule
import com.apesmedical.commonsdk.delegate.GlobalConfigModule
import com.pr1n.repository.repo.MainRepository
import com.pr1n.repository.repo.impl.MainRepositoryImpl
import com.pr1n.androidtest.viewmodel.ViewModel1
import com.pr1n.androidtest.viewmodel.ViewModel2
import com.pr1n.androidtest.viewmodel.ViewModel3
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class AppConfiguration : ConfigModule {
    override fun applyOptions(context: Context, builder: GlobalConfigModule) {
//        builder.addModule(
//            AppInitializer.getInstance(context).initializeComponent(MyInitializer::class.java)
//        )
        builder.addModule(module {
            single<RemoteService> { RxHttpRemoteService() }
            single<LocalService> { LocalService }
            single { MainDB.getDatabase(context) }
            factory<MainRepository> { MainRepositoryImpl(get(), get()) }
            viewModel { params -> ViewModel1(params.get(), get()) }
            viewModel { params -> ViewModel2(params.get(), get()) }
            viewModel { params -> ViewModel3(params.get(), get()) }
        })
    }

    override fun injectAppLifecycle(context: Context, lifecycles: MutableList<AppLifecycles>) {
        lifecycles.add(SDKAppLifecycles())
    }
}