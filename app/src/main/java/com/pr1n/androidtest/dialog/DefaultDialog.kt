package com.pr1n.androidtest.dialog

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.ViewTreeSavedStateRegistryOwner
import com.apesmedical.commonsdk.widget.CustomDialog
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class DefaultDialog(
    private val context: Context,
    private val owner: LifecycleOwner,
    private val registryOwner: SavedStateRegistryOwner
) {

    private lateinit var cancellClick: () -> Unit
    private lateinit var defineClick: () -> Unit

    private val view = ComposeView(context).also {
        ViewTreeLifecycleOwner.set(it, owner)
        //ViewTreeViewModelStoreOwner.set(it, owner)
        ViewTreeSavedStateRegistryOwner.set(it, registryOwner)
        it.setContent {
            DialogScreen(cancellClick, defineClick)
        }
    }

    @Composable
    private fun DialogScreen(cancellClick: () -> Unit, defineClick: () -> Unit) {
        Column(
            modifier = Modifier.background(
                color = Color.White,
                shape = RoundedCornerShape(5.dp)
            )
        ) {
            Text(
                text = "Title",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.h5
            )
            Text(
                text = "Content",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.body1
            )
            Row {
                Button(onClick = cancellClick, modifier = Modifier.weight(1F)) {
                    Text(text = "cancell")
                }
                Button(onClick = defineClick, modifier = Modifier.weight(1F)) {
                    Text(text = "define")
                }
            }
        }
    }

    private val dialog by lazy {
        CustomDialog(
            context = context,
            contentView = view,
            isCanceledOnTouchOutside = true
        )
    }

    suspend fun show() = suspendCancellableCoroutine<Boolean> { cancellableContinuation ->
        cancellableContinuation.invokeOnCancellation { dialog.cancel() }
        cancellClick = {
            dialog.dismiss()
            cancellableContinuation.resume(false)
        }
        defineClick = {
            dialog.dismiss()
            cancellableContinuation.resume(true)
        }
        dialog.show()
    }
}