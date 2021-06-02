package br.com.raveline.coroutineskotlin.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.raveline.coroutineskotlin.R
import kotlinx.android.synthetic.main.activity_other.*

class OtherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other)

        if (intent.extras != null) {
            textView_other_id.text = intent.getStringExtra(TAG_BUNDLE)
        }
    }

    companion object {
        const val TAG_BUNDLE = "main_activity"
        const val TAG = "other_activity"
    }
}