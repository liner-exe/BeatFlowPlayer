package com.example.beatflowplayer

import android.Manifest
import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.beatflowplayer.ui.navigation.AppNavigation
import com.example.beatflowplayer.ui.theme.BeatFlowPlayerTheme
import com.example.beatflowplayer.viewmodel.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint

fun requestPermissions(activity: Activity) {
    val permissionRequestCode = 123
    val permissionsToRequest = mutableListOf<String>()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissionsToRequest.add(Manifest.permission.READ_MEDIA_AUDIO)
    } else {
        permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    ActivityCompat.requestPermissions(
        activity,
        permissionsToRequest.toTypedArray(),
        permissionRequestCode
    )
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BeatFlowPlayerTheme {
                Main(viewModel)
            }
        }

        requestPermissions(this)
    }
}

@Composable
fun Main(
    playerViewModel: PlayerViewModel
) {
    BeatFlowPlayerTheme {
        AppNavigation(playerViewModel)
    }
}