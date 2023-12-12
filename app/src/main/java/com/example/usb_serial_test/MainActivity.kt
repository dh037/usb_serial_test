package com.example.usb_serial_test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.usb_serial_test.ui.theme.Usb_serial_testTheme


class MainActivity : ComponentActivity() {
    private val sc = SerialCommunication(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Usb_serial_testTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = {
                            sc.handleDoor(doorState = DoorState.OPEN)
                        }) {
                            Text("OPEN DOOR")
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(onClick = {
                            sc.handleDoor(doorState = DoorState.CLOSE)
                        }) {
                            Text("CLOSE DOOR")
                        }

                    }
                }
            }
        }
    }

    override fun onDestroy() {
        sc.closeConnection()
        super.onDestroy()
    }
}
