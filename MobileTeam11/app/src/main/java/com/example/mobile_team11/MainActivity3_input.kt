package com.example.mobile_team11

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobile_team11.databinding.ActivityMainActivity3InputBinding

class MainActivity3_input : AppCompatActivity() {
    lateinit var binding: ActivityMainActivity3InputBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainActivity3InputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "운동 루틴 입력"

        binding.RoutineInputBtn.setOnClickListener {
            val intent = Intent()
            intent.putExtra("name",binding.name.text.toString())
            intent.putExtra("time", "${binding.time.text} 분")
            intent.putExtra("set", "${binding.set.text} 세트")
            intent.putExtra("count", "${binding.count.text} 회")
            intent.putExtra("relax", "${binding.relax.text} 초")
            setResult(RESULT_OK,intent)
            finish()
        }

    }
}