package com.thelazybattley.joserizalquizadmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.thelazybattley.joserizalquizadmin.domain.usecase.IsSignedInUseCase
import com.thelazybattley.joserizalquizadmin.presentation.navigation.AppNavigation
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Firestore rules only allow the admin account to write, so require sign-in first.
    @Inject
    lateinit var isSignedInUseCase: IsSignedInUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val isSignedIn = isSignedInUseCase()
        setContent {
            AppNavigation(isSignedIn = isSignedIn)
        }
    }
}
