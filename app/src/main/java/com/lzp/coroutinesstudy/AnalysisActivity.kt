package com.lzp.coroutinesstudy

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_analysis.*
import kotlinx.coroutines.*

class AnalysisActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analysis)

        start.setOnClickListener {
            CoroutineScope(Dispatchers.IO + CoroutineName("test")).launch {
                delay(100)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AnalysisActivity, "task complete", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}