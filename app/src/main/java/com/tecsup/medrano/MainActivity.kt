package com.tecsup.medrano

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.tecsup.medrano.navigation.AppNavGraph
import com.tecsup.medrano.ui.theme.Medrano_ProductosAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Medrano_ProductosAppTheme {
                AppNavGraph()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
}