package com.lzp.coroutinesstudy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lzp.coroutinesstudy.retofit.StringConvertFactory
import com.lzp.coroutinesstudy.service.TestService
import kotlinx.android.synthetic.main.activity_net_work.*
import retrofit2.Retrofit

class NetWorkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_net_work)

        network.setOnClickListener {
            val retrofit = Retrofit.Builder().baseUrl("https://www.baidu.com")
                .addConverterFactory(StringConvertFactory())
                .build()

            lifecycleScope.launchWhenCreated {
                val content =
                    retrofit.create(TestService::class.java).getContent("https://www.baidu.com")
                text.text = content
            }


        }
    }
}