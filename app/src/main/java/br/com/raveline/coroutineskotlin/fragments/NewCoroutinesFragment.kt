package br.com.raveline.coroutineskotlin.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import br.com.raveline.coroutineskotlin.R
import br.com.raveline.coroutineskotlin.databinding.FragmentNewCoroutinesBinding
import br.com.raveline.coroutineskotlin.manager.UserManager
import br.com.raveline.coroutineskotlin.viewmodel.NewCoroutinesViewModel
import kotlinx.android.synthetic.main.fragment_new_coroutines.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main


class NewCoroutinesFragment : Fragment() {

    private lateinit var dataBinding: FragmentNewCoroutinesBinding
    private lateinit var viewModel: NewCoroutinesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_new_coroutines, container, false)

        dataBinding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(NewCoroutinesViewModel::class.java)

       // viewModel.getUserDataUpdated()

        viewModel.users.observe(viewLifecycleOwner, Observer { users ->
            users.forEach { userModel ->
                Log.i("ViewModelScope", "User name is ${userModel.name} ")
            }
        })

        lifecycleScope.launchWhenCreated {
            dataBinding.progressBar.visibility = View.VISIBLE
            Log.i("LifecycleScope", "Launch When Created")
            delay(4000)
        }

        lifecycleScope.launchWhenStarted {
            dataBinding.progressBar.visibility = View.GONE
            Log.i("LifecycleScope", "Launch When Started")
            delay(4000L)
        }

        lifecycleScope.launchWhenResumed {
            dataBinding.progressBar.visibility = View.GONE
            Log.i("LifecycleScope", "Launch When Resumed")
        }

        dataBinding.buttonDownloadNewCoroutines.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {
                showText()
            }
        }

        dataBinding.buttonStartNewCoroutines.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                showClicks()
            }
        }

        dataBinding.buttonAsyncNewCoroutines.setOnClickListener {
            /*     CoroutineScope(Main).launch {
                     Log.i("CoroutineScope", "Calculation started... ")

                     viewModel.asyncVariable1.value = async { getAsyncValue1() }.await()
                     viewModel.asyncVariable2.value = async { getAsyncValue2() }.await()
                     viewModel.total.value = async{viewModel.getSumOfAsync()}.await()

                     Log.i("CoroutineScope", "Calculation finished... ")

                     dataBinding.apply {
                         tvAsyncFunctionId.text = "Total sum is ${viewModel.total.value}"
                     }

                 }*/

            CoroutineScope(Main).launch {
                // dataBinding.tvAsyncFunctionId.text = viewModel.getTotalCount().toString()
                dataBinding.tvAsyncFunctionId.text = UserManager().getTotalUserCount().toString()

            }
        }


        return dataBinding.root
    }

    private fun downloadUserData() {
        for (i in 1 until 100) {
            Log.i("NewCoroutinesFragment", "downloadUserData in ${Thread.currentThread().name} $i")
        }
    }

    private suspend fun showText() {
        withContext(Dispatchers.Main) {

            for (i in 1 until 100) {
                delay(50L)
                dataBinding.tvDataUserNewCoroutines.text =
                    "Downloading user $i in ${Thread.currentThread().name} "
            }
        }
    }

    private suspend fun showClicks() {
        withContext(Dispatchers.Main) {
            dataBinding.tvClickedTimesId.text = "Clicked ${viewModel.getValueUpdated()}"
        }
    }

    private suspend fun getAsyncValue1(): Int {
        delay(5000L)
        Log.i("CoroutineScope", "getAsyncValue1: returned")
        return viewModel.asyncVariable1.value!! * 3
    }


    private suspend fun getAsyncValue2(): Int {
        delay(3000)
        Log.i("CoroutineScope", "getAsyncValue2: returned")
        return viewModel.asyncVariable2.value!! * 2
    }

}