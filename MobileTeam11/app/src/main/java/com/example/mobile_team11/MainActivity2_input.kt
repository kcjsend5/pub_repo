package com.example.mobile_team11

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobile_team11.databinding.ActivityMainActivity2InputBinding

class MainActivity2_input : AppCompatActivity() {
    lateinit var binding: ActivityMainActivity2InputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainActivity2InputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "식단 입력"

        binding.FoodInputBtn.setOnClickListener {
            val food = binding.food.text.toString()
            val calorie = binding.calorie.text.toString()
            val nutrient = binding.nutrient.text.toString()

            // Append "kcal" unit to the calorie input
            val calorieWithUnit = addUnitToCalories(calorie, "kcal")

            val intent = Intent()
            intent.putExtra("food", food)
            intent.putExtra("calorie", calorieWithUnit)
            intent.putExtra("nutrient", nutrient)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    // Function to append unit to the calorie input
    private fun addUnitToCalories(calorie: String, unit: String): String {
        return "$calorie $unit"
    }
}