package com.thelazybattley.joserizalquizadmin.presentation.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var selectedRoute by rememberSaveable {
        mutableStateOf(
            value = AppDestinations.BottomNavDestinations.Home.route
        )
    }

    NavigationBar(
        windowInsets = NavigationBarDefaults.windowInsets,
        modifier = modifier,
        containerColor = colors.ivoryMist
    ) {
        AppDestinations.BottomNavDestinations.routes().forEach { route ->
            val isSelected = selectedRoute == route.route
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    selectedRoute = route.route
                    navController.navigate(route = route.route)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = route.drawable),
                        contentDescription = null,
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = route.textRes),
                        style = typography.semiBold10
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = colors.antiqueGold,
                    selectedTextColor = colors.espresso,
                    unselectedIconColor = colors.taupe,
                    unselectedTextColor = colors.taupe
                )
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewBottomNavBar() {
    AppTheme {
        BottomNavBar(
            modifier = Modifier
                .fillMaxWidth(),
            navController = rememberNavController()
        )
    }
}
