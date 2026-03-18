package io.hextree.initial

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.hextree.initial.ui.theme.InitialTheme
//import androidx.core.net.toUri

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //val link = Intent(Intent.ACTION_VIEW, "https://hextree.io".toUri())
        val servico = Intent().apply{
            setClassName("io.hextree.attacksurface", "io.hextree.attacksurface.services.Flag24Service")
            action = "io.hextree.services.START_FLAG24_SERVICE"
        }


        setContent {
            var value by remember { mutableIntStateOf(0) }
            InitialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Hextree.io",
                        modifier = Modifier.padding(innerPadding)
                    )
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        Button(onClick = {value += 1}) {
                            Text(text = "valor: $value")
                            Log.i("HEXTREE", "run")
                            if(value >= 1){
                                this@MainActivity.startService(servico)

                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InitialTheme {
        Greeting("Hello.Hextree.io")
    }
}