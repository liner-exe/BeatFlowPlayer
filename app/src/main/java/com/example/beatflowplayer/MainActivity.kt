package com.example.beatflowplayer

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.palette.graphics.Palette
import com.example.beatflowplayer.ui.theme.BeatFlowPlayerTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Main()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Main() {
    val navController = rememberNavController()

    Scaffold {
        NavHost(
            navController = navController,
            startDestination = Screen.PlayerScreen.route
        ) {
            composable(Screen.PlayerScreen.route) { AudioPlayerUI(navController) }
            composable(Screen.TracksScreen.route) { TracksUI(navController) }
        }
    }
}

@Composable
fun AudioPlayerUI(navController: NavController) {
    val context = LocalContext.current

    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var isPlaying by remember { mutableStateOf(false) }
    val mediaplayer = remember { MediaPlayer.create(context, R.raw.nominalo) }
    var accentColor by remember { mutableStateOf<Color?>(null) }

    val imageResId = R.drawable.nominalo

    LaunchedEffect(imageResId) {
        val bitmap = withContext(Dispatchers.IO) {
            BitmapFactory.decodeResource(context.resources, imageResId)
        }

        val palette = Palette.from(bitmap).generate()
        accentColor = palette.vibrantSwatch?.rgb?.let { Color(it) }
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            accentColor ?: Color.White,
            accentColor?.copy(alpha = 0.7f) ?: Color.White,
            Color.Black.copy(alpha = 0.9f)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    )
    {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )
        {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            )
            {
                IconButton(onClick = { navController.navigate(Screen.TracksScreen.route) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_down),
                        contentDescription = "Collapse",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Now playing",
                    fontSize = 16.sp,
                    style = TextStyle(
                        fontWeight = FontWeight.W500
                    ),
                    color = Color.LightGray
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.nominalo),
                    contentDescription = "Album Cover",
                    modifier = Modifier
                        .size(300.dp)
                        .clip(shape = RoundedCornerShape(12.dp))
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "NOMINALO",
                    color = Color.White,
                    fontSize = 40.sp,
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "MORGENSHTERN",
                    color = Color.LightGray,
                    fontSize = 18.sp,
                    style = TextStyle(
                        fontWeight = FontWeight.W500
                    )
                )

                Slider(
                    value = sliderPosition,
                    onValueChange = { newValue ->
                        sliderPosition = newValue
                    },
                    valueRange = 0f..100f,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    Text(
                        text = "0:00", color = Color.LightGray
                    )

                    Text(
                        text = "2:11", color = Color.LightGray
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {

                        },
                        Modifier
                            .clip(shape = CircleShape)
                            .size(36.dp)
                            .background(color = Color.Transparent)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.shuffle),
                            contentDescription = "Shuffle",
                            modifier = Modifier.size(36.dp),
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = {

                        },
                        Modifier
                            .clip(shape = CircleShape)
                            .size(48.dp)
                            .background(color = Color.LightGray)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.previous),
                            contentDescription = "Previous",
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            if (isPlaying) {
                                mediaplayer.pause()
                            } else {
                                mediaplayer.start()
                            }
                            isPlaying = !isPlaying
                        },
                        modifier = Modifier
                            .clip(shape = CircleShape)
                            .size(64.dp)
                            .background(color = Color.LightGray)
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (isPlaying) R.drawable.pause
                                else R.drawable.play_button
                            ),
                            contentDescription = "Pause",
                            modifier = Modifier.size(50.dp)
                        )
                    }

                    IconButton(
                        onClick = {

                        },
                        Modifier
                            .clip(shape = CircleShape)
                            .size(48.dp)
                            .background(color = Color.LightGray)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.next),
                            contentDescription = "Next",
                            modifier = Modifier
                                .size(36.dp)
                                .background(color = Color.LightGray)
                        )
                    }

                    IconButton(
                        onClick = {

                        },
                        Modifier
                            .clip(shape = CircleShape)
                            .size(48.dp)
                            .background(color = Color.Transparent)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.repeat),
                            contentDescription = "Previous",
                            modifier = Modifier.size(36.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TracksUI(navController: NavController) {
    val tracks = listOf(
        Track(1, "OLALA", "MORGENSTERN"),
        Track(2, "ARISTOCRAT", "MORGENSTERN"),
        Track(3, "HUBLOT", "MORGENSTERN"),
        Track(4, "NOMINALO", "MORGENSTERN"),
        Track(5, "GTA", "MORGENSTERN"),
        Track(6, "ПАПИН ТАНК", "MORGENSTERN"),
        Track(7, "DINERO", "MORGENSTERN"),
        Track(8, "МАНИЯ", "MORGENSTERN"),
        Track(9, "Я НА ТАБЛАХ", "MORGENSTERN"),
        Track(10, "КОГДА НАС ОТПУСТИТ", "MORGENSTERN"),
        Track(11, "PULL UP", "MORGENSTERN"),
        Track(12, "Я КОГДА-НИБУДЬ УЙДУ", "MORGENSTERN"),
    ).also {
        TrackList(tracks = it)
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewAudioPlayerUI() {
    BeatFlowPlayerTheme {
        AudioPlayerUI(navController = rememberNavController())
    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun PreviewTracksUI() {
//    BeatFlowPlayerTheme {
//        TracksUI(navController = rememberNavController())
//    }
//}