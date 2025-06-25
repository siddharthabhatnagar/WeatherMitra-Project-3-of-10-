package com.project.weathermitra.screens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.project.weathermitra.R
import com.project.weathermitra.models.WeatherResponse
import com.project.weathermitra.repository.WeatherRepository
import com.project.weathermitra.viewmodel.WeatherState
import com.project.weathermitra.viewmodel.WeatherViewModel


@Composable
fun MainScreen() {
    val vm:WeatherViewModel= hiltViewModel()
    val apiKey=""
    val state by vm.state.collectAsState()
    val location = remember { mutableStateOf("") }
    var temp=remember { mutableStateOf("") }
    var condition=remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var tempLocation by remember { mutableStateOf(location.value) }
    var city by remember { mutableStateOf("Paris") }
    LaunchedEffect(city) { vm.loadWeather(city, apiKey) }
    when (state) {
        is WeatherState.Loading -> FullScreenIndicator()
        is WeatherState.Error -> FullScreenError((state as WeatherState.Error).message)
        is WeatherState.Success -> {
            val w = (state as WeatherState.Success).data
            location.value=w.location.name
            temp.value=w.current.temp_c.toString()
            condition.value=w.current.condition.text
            Log.d("TAG", "MainScreen: ${w.location.name} ${w.current.temp_c.toString()} ${w.current.condition.text}")

        }
    }
    val theme = "Dark"
    var drawableId = R.drawable.light_bg
    if (theme == "Light") {
        drawableId = R.drawable.light_bg
    } else {
        drawableId = R.drawable.dark_bg
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = drawableId),
            contentDescription = "Clean Sky Background",
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = location.value,
                modifier = Modifier.padding(top = 100.dp),
                color = Color.White,
                fontSize = 50.sp
            )
            Text(
                text =temp.value,
                modifier = Modifier.padding(top = 10.dp),
                color = Color.White,
                fontSize = 100.sp
            )
            Text(
                text = condition.value,
                modifier = Modifier.padding(top = 10.dp),
                color = Color.White,
                fontSize = 25.sp
            )
            Image(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .width(250.dp)
                    .height(250.dp),
                painter = painterResource(id = R.drawable.house_element),
                contentDescription = "House Element",
                contentScale = ContentScale.Crop
            )
            Row (modifier = Modifier.padding(top=20.dp)){
                Text(
                    text = "Change Location", fontSize = 25.sp, modifier = Modifier.clickable {
                        tempLocation = location.value
                        showDialog = true
                    },
                    color = Color.White
                )
                IconButton(
                    onClick = {
                        tempLocation = location.value
                        showDialog = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Change Location"
                    )
                }
            }
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text(text = "Change Location") },
                    text = {
                        OutlinedTextField(
                            value = tempLocation,
                            onValueChange = { tempLocation = it },
                            label = { Text("Enter new location") }
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                city = tempLocation
                                showDialog = false
                            }
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDialog = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}
@Composable
fun FullScreenIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


@Composable
fun FullScreenError(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Error: $message",
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.body1
        )
    }
}

@Preview
@Composable
fun PreviewMS(modifier: Modifier = Modifier) {
    MainScreen()
}