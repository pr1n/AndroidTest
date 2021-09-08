package com.pr1n.androidtest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.apes.annotation.nav.Destination
import com.apesmedical.commonsdk.base.newbase.BaseVMTripleView
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.library.sdk.ext.logi
import com.pr1n.androidtest.databinding.ActivityMainBinding
import com.pr1n.androidtest.dialog.showNormalDialogFragment
import com.pr1n.androidtest.screen.HostNavScreen
import com.pr1n.androidtest.viewmodel.ViewModel1
import com.pr1n.androidtest.viewmodel.ViewModel2
import com.pr1n.androidtest.viewmodel.ViewModel3
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.getStateViewModel


//@UIConfig(isAction = false, title = "Main")
@ExperimentalAnimationApi
@ExperimentalMaterialNavigationApi
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

//        val biometricPrompt = BiometricPrompt.Builder(this)
//            .setTitle("Title")
//            .setDescription("Description...")
//            .setNegativeButton("Cancel", Executors.newSingleThreadExecutor()) { dialog, which ->
//                logi("Cancel*-*-*-*-*-*-*-*-*-*-which: $which")
//                dialog?.dismiss()
//            }
//            .setConfirmationRequired(true)
//            .build()
//        biometricPrompt.authenticate(
//            CancellationSignal(),
//            Executors.newSingleThreadExecutor(),
//            object : BiometricPrompt.AuthenticationCallback() {
//                override fun onAuthenticationFailed() {
//                    super.onAuthenticationFailed()
//                    logi("onAuthenticationFailed*-*-*-*-*-*-*-*-*-*-")
//                }
//
//                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
//                    super.onAuthenticationSucceeded(result)
//                    logi("onAuthenticationSucceeded*-*-*-*-*-*-*-*-*-*-result: $result")
//                }
//
//                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
//                    super.onAuthenticationError(errorCode, errString)
//                    logi("onAuthenticationError*-*-*-*-*-*-*-*-*-*-helpCode: $errorCode, helpString: $errString")
//                }
//
//                override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
//                    super.onAuthenticationHelp(helpCode, helpString)
//                    logi("onAuthenticationHelp*-*-*-*-*-*-*-*-*-*-helpCode: $helpCode, helpString: $helpString")
//                }
//            })

//        mDataBinding.showDialog.setOnClickListener {
//            lifecycleScope.launch {
//                toast("dialog result -> ${showNormalDialogFragment()}")
//            }
//        }
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