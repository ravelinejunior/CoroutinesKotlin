package br.com.raveline.coroutineskotlin.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import br.com.raveline.coroutineskotlin.R
import br.com.raveline.coroutineskotlin.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var dataBinding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        GlobalScope.launch {
            asyncFunctions()
        }

        button_id.setOnClickListener {
            //lifecycleScope funciona de acordo com a activity lifecycle, se for destruida a coroutine finaliza
            lifecycleScope.launch {
                while (true) {
                    delay(1000L)
                    Log.d(TAG, "Button clicked: Still running.... ")
                }
            }

            GlobalScope.launch {
                delay(5000L)
                val intent = Intent(this@MainActivity, OtherActivity::class.java)
                intent.putExtra(TAG, doNetworkCall())
                startActivity(intent)
                finish()
            }
        }

        dataBinding.buttonNewCoroutinesId.setOnClickListener {
            startActivity(Intent(this,NewCouroutineActivity::class.java))
        }

    }


    private suspend fun asyncFunctions() {
        GlobalScope.launch(Dispatchers.IO) {
            val time = measureTimeMillis {

                val a = async { doNetworkCall() }
                val b = async { doNetworkCall2() }

                Log.d(TAG, "A is ${a.await()}")
                Log.d(TAG, "B is ${b.await()}")

                /*
               //// BAD PRACTICE
               val job1 = launch {
                    a = doNetworkCall()
                }

                val job2 = launch {
                    b = doNetworkCall2()
                }

                job1.join()
                job2.join()*/
            }

            Log.d(TAG, "asyncFunctions: Request took $time ms.")
        }
    }

    private suspend fun usingJobAndRunBlocking() {
        val job = GlobalScope.launch(Dispatchers.Default) {
            Log.d(TAG, "Coroutines is still working ...")
            withTimeout(3000L) {
                for (i in 0 until 1000000 step 2) {
                    if (isActive) {
                        Log.d(TAG, "Coroutines Counting on $i")
                    }

                }
            }
            Log.d(TAG, "Coroutines end counting ")
        }

        runBlocking {
            delay(2000L)
            job.cancel()
            Log.d(TAG, "usingJobAndRunBlocking: Main Thread is continuing ...")

            callGlobalLaunch()
        }
    }

    suspend fun callGlobalLaunch() {
        GlobalScope.launch(newSingleThreadContext("Ex´s")) {
            Log.d(TAG, "Coroutine hello from thread ${Thread.currentThread().name}")

            for (i in 1 until 3) {
                delay(1000L)
                Log.d(
                    TAG,
                    "Coroutine hello from thread ${Thread.currentThread().name} and the index is $i"
                )
            }

            val net1 = doNetworkCall()
            val net2 = doNetworkCall2()

            Log.d(TAG, net1)
            Log.d(TAG, net2)

            withContext(Dispatchers.Main) {
                Log.d(TAG, "Coroutine after text from thread ${Thread.currentThread().name}")
                textView.text = doNetworkCall()
            }
        }
    }

    suspend fun callRunBlocking() {

        runBlocking {
            delay(2000)
            Log.d(TAG, "Coroutine Run Blocking from thread ${Thread.currentThread().name}")

            launch(Dispatchers.IO) {
                delay(2000)
                Log.d(TAG, "Finished IO Coroutines 1")
            }


            launch(Dispatchers.IO) {
                delay(2000)
                Log.d(TAG, "Finished IO Coroutines 2")
                Log.d(TAG, "Coroutine after text from thread ${Thread.currentThread().name}")
                textView.text = doNetworkCall()
            }

            Log.d(TAG, "Coroutine ENDS Run Blocking from thread ${Thread.currentThread().name}")

        }
    }

    private suspend fun doNetworkCall(): String {
        delay(2500)
        return "I want to study coroutines "
    }


    private suspend fun doNetworkCall2(): String {
        delay(2000)
        return "I really need to study more flutter and serveless"
    }

    companion object {
        const val TAG = "main_activity"
    }
}