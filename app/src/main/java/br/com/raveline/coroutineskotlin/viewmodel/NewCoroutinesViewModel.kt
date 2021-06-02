package br.com.raveline.coroutineskotlin.viewmodel

import androidx.lifecycle.*
import br.com.raveline.coroutineskotlin.model.UserModel
import br.com.raveline.coroutineskotlin.repository.UserRepository
import kotlinx.coroutines.*

class NewCoroutinesViewModel : ViewModel() {
    private var myText = MutableLiveData<Int>()
    val getMyText: LiveData<Int>
        get() = myText

    var asyncVariable1 = MutableLiveData<Int>()
    var asyncVariable2 = MutableLiveData<Int>()
    var total = MutableLiveData<Int>()

    private val myJob = Job()
    private val myScope = CoroutineScope(Dispatchers.IO + myJob)

    private var userRepository = UserRepository()
   // var users: MutableLiveData<List<UserModel>> = MutableLiveData()

    var users = liveData(Dispatchers.IO) {
        val result = userRepository.getUsers()
        emit(result)
    }

    init {
        myText.value = 0
        asyncVariable1.value = 51000
        asyncVariable2.value = 12000
        total.value = 0
    }

    fun getValueUpdated(): Int {
        myText.value = myText.value?.plus(1)
        return getMyText.value!!
    }

    fun getSumOfAsync(): Int {
        total.value = asyncVariable1.value?.plus(asyncVariable2.value!!)
        return total.value!!
    }

    suspend fun getTotalCount(): Int {
        var count = 0
        val deferredInt = CoroutineScope(Dispatchers.IO).async {
            delay(1000L)
            return@async 150
        }

        return count + deferredInt.await()
    }


    fun getUserData() {
        myScope.launch {

        }
    }

    fun getUserDataUpdated() {
        viewModelScope.launch {
            var result: List<UserModel>? = null
            withContext(Dispatchers.IO){
                result = userRepository.getUsers()
            }

          //  users.value = result
        }
    }

    override fun onCleared() {
        super.onCleared()
        myJob.cancel()
    }


}