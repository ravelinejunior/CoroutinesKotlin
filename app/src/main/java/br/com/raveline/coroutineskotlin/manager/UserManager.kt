package br.com.raveline.coroutineskotlin.manager

import kotlinx.coroutines.*

class UserManager {

    var count = 0
    private lateinit var deferredInt:Deferred<Int>

    suspend fun getTotalUserCount(): Int {

        coroutineScope {
            launch(Dispatchers.IO) {
                delay(2000)
                count = 150
            }

             deferredInt = async(Dispatchers.IO) {
                delay(1000L)
                return@async 150
            }

        }

        return count + deferredInt.await()

    }
}