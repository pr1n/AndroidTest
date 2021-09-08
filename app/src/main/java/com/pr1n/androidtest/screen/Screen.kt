package com.pr1n.androidtest.screen

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.plusAssign
import com.apesmedical.commonsdk.http.Complete
import com.apesmedical.commonsdk.http.Loading
import com.apesmedical.commonsdk.http.Success
import com.blankj.utilcode.util.ToastUtils
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.pr1n.androidtest.TestActivity
import com.pr1n.androidtest.dialog.DefaultDialog
import com.pr1n.androidtest.viewmodel.ViewModel1
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity
import org.koin.androidx.compose.getStateViewModel
import kotlin.math.roundToInt

@JvmInline
value class Route private constructor(val routePath: String) {
    companion object {
        val FirstScreen = Route("/first")
        val SecondScreen = Route("/second")
        val SheetScreen = Route("/sheet")
    }
}

@ExperimentalMaterialNavigationApi
@ExperimentalAnimationApi
@Composable
fun HostNavScreen() {
    val animatedNavController = rememberAnimatedNavController()
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    animatedNavController.navigatorProvider += bottomSheetNavigator
    ModalBottomSheetLayout(bottomSheetNavigator = bottomSheetNavigator) {
        AnimatedNavHost(
            navController = animatedNavController,
            startDestination = Route.FirstScreen.routePath
        ) {
            composable(route = Route.FirstScreen.routePath,
                enterTransition = { initial, target -> slideInHorizontally(initialOffsetX = { 1000 }) },
                exitTransition = { initial, target -> slideOutHorizontally(targetOffsetX = { -1000 }) },
                popEnterTransition = { initial, target -> slideInHorizontally(initialOffsetX = { -1000 }) },
                popExitTransition = { initial, target -> slideOutHorizontally(targetOffsetX = { 1000 }) })
            { FirstScreen(animatedNavController) }

            composable(route = Route.SecondScreen.routePath,
                enterTransition = { initial, target -> slideInHorizontally(initialOffsetX = { 1000 }) },
                exitTransition = { initial, target -> slideOutHorizontally(targetOffsetX = { -1000 }) },
                popEnterTransition = { initial, target -> slideInHorizontally(initialOffsetX = { -1000 }) },
                popExitTransition = { initial, target -> slideOutHorizontally(targetOffsetX = { 1000 }) })
            { SecondScreen(animatedNavController) }
            bottomSheet(Route.SheetScreen.routePath) {
                Text(text = "This is a cool bottom sheet!", modifier = Modifier.padding(10.dp))
            }
        }
    }
//    val navController = rememberNavController()
//    NavHost(navController = navController, startDestination = Route.FirstScreen.routePath) {
//        composable(Route.FirstScreen.routePath) { FirstScreen(navController) }
//        composable(Route.SecondScreen.routePath) { SecondScreen(navController) }
//    }
}

@Composable
private fun FirstScreen(navController: NavHostController) {
    val viewModel = getStateViewModel<ViewModel1>()

    val resultState by viewModel.resultLiveData.observeAsState(Complete())
    Log.i("TAG", "FirstScreen: $resultState")
    val isShowLoading = resultState !is Complete<*>
    var resultText by remember { mutableStateOf("") }
    if (resultState is Loading) resultText = ""
    if (resultState is Error) resultText = "Error: ${resultState.message}"
    if (resultState is Success) resultText = resultState.data.toString()

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "First") })
    }) {
        Column {
            Button(
                onClick = { navController.navigate(Route.SecondScreen.routePath) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .background(Color.LightGray, RoundedCornerShape(5.dp))
            ) { Text(text = "toSecond", color = MaterialTheme.colors.error) }

            Button(
                onClick = { navController.navigate(Route.SheetScreen.routePath) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .background(Color.LightGray, RoundedCornerShape(5.dp))
            ) { Text(text = "showSheet", color = MaterialTheme.colors.primaryVariant) }

            Button(
                onClick = { viewModel.getDataLiveData() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .placeholder(
                        visible = isShowLoading,
                        color = Color.Gray,
                        highlight = PlaceholderHighlight.shimmer(Color.LightGray)
                    )
                    .background(Color.LightGray, RoundedCornerShape(5.dp))
            ) { Text(text = "getData") }

            Text(
                text = resultText,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(20.dp)
                    .verticalScroll(ScrollState(0))
            )
        }
    }
}

@Composable
private fun SecondScreen(navController: NavHostController) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Second") }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
            }
        })
    }) {
        val modifier = Modifier
            .background(Color.Red)
            .layout { measurable, constraints ->
                val padding = 8.dp
                    .toPx()
                    .roundToInt()
                val paddedConstraints = constraints
                    .copy()
                    .also {
                        it.constrainWidth(it.maxWidth - padding * 2)
                        it.constrainHeight(it.maxHeight - padding * 2)
                    }
                val placeable = measurable.measure(paddedConstraints)
                layout(placeable.width + padding * 2, placeable.height + padding * 2) {
                    placeable.placeRelative(padding, padding)
                }
            }
            .background(Color.Green)


        val scope = rememberCoroutineScope()

        val dialog = DefaultDialog(
            LocalContext.current,
            LocalLifecycleOwner.current,
            LocalSavedStateRegistryOwner.current
        )
        val context = LocalContext.current
        CustomColumn {
            Text(text = "layout1", modifier = modifier.clickable {
                scope.launch {
                    ToastUtils.showShort(dialog.show().toString())
                }
            })
            Text(text = "layout2", modifier = modifier.clickable {
                context.startActivity<TestActivity>()
            })
            Text(text = "layout3", modifier = modifier)
        }
    }
}

@Composable
private fun CustomColumn(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(content = content, modifier = modifier) { measurables, constrints ->
        val width: Int
        val height: Int
        val placeables = measurables.map { it.measure(constrints) }
        width = placeables.maxOf { it.width }
        height = placeables.sumOf { it.height }
        layout(width, height) {
            var sumHeight = 0
            placeables.forEach { placeable ->
                placeable.placeRelative(0, sumHeight)
                sumHeight += placeable.height
            }
        }
    }
}