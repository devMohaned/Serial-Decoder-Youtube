package com.serial.decoder.core.nav

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDeepLink
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.serial.decoder.core.ui.MainViewModel
import com.serial.decoder.feature_decoding.ui.home.HomeDeepLinking
import com.serial.decoder.feature_decoding.ui.home.HomeScreen
import com.serial.decoder.feature_faq.ui.FAQScreen
import com.serial.decoder.feature_onboarding.ui.OnBoardingScreen


@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    windowSize: WindowWidthSizeClass,
    viewModel: MainViewModel = hiltViewModel<MainViewModel>()
) {
    val navController: NavHostController = rememberNavController()

    val startDestination = if (viewModel.isFirstTime) NavigationGraph.OnBoarding.name
    else NavigationGraph.Home.name

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    )
    {
        composable(route = NavigationGraph.OnBoarding.name)
        {
            OnBoardingScreen(windowSize = windowSize, onHomeNavigation = {
                navController.navigate(NavigationGraph.Home.name) {
                    popUpTo(NavigationGraph.OnBoarding.name) {
                        inclusive = true
                    }
                }
            })
        }

        composable(
            route = NavigationGraph.Home.name, deepLinks = listOf(
                NavDeepLink(
                    uri = HomeDeepLinking.URI
                )
            ), arguments = listOf(
                navArgument(
                    name = HomeDeepLinking.ARG_1_SERIAL,
                    builder = {
                        type = NavType.StringType
                        nullable = true
                    },
                ), navArgument(name = HomeDeepLinking.ARG_2_BRAND, builder = {
                    type = NavType.StringType
                    nullable = true
                })
            )
        )
        {
            val serial = it.arguments?.getString(HomeDeepLinking.ARG_1_SERIAL)
            val brand = it.arguments?.getString(HomeDeepLinking.ARG_2_BRAND)

            HomeScreen(serial = serial, brand = brand, onFAQClicked = {
                navController.navigate(NavigationGraph.FAQ.name)
            })
        }

        composable(route = NavigationGraph.FAQ.name)
        {
            FAQScreen() {
                navController.popBackStack()
            }
        }
    }
}