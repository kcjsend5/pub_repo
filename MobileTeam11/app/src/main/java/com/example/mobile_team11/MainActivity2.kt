package com.example.mobile_team11

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_team11.databinding.ActivityMain2Binding
import com.example.mobile_team11.databinding.ItemMain2Binding

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    lateinit var data:MutableList<MutableList<String>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "식단 관리"

        data = mutableListOf<MutableList<String>>()

        binding.recyclerViewFood.layoutManager = LinearLayoutManager(this)
        val adapter = MyAdapter2(data)
        binding.recyclerViewFood.adapter = adapter
        binding.recyclerViewFood.addItemDecoration(DividerItemDecoration(this,LinearLayoutManager.VERTICAL))

        val requestLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            var food = it.data?.getStringExtra("food")
            var calorie = it.data?.getStringExtra("calorie")
            var nutrient = it.data?.getStringExtra("nutrient")

            val resultdata = mutableListOf<String>()
            resultdata.add(food!!)
            resultdata.add(calorie!!)
            resultdata.add(nutrient!!)
            data.add(resultdata)
            adapter.notifyDataSetChanged()

            MyApplication.prefs.setdata("data2[${data.size}][0]",food)
            MyApplication.prefs.setdata("data2[${data.size}][1]",calorie)
            MyApplication.prefs.setdata("data2[${data.size}][2]",nutrient)
            MyApplication.prefs.setint("data2size",data.size)
        }

        for( i in 1..MyApplication.prefs.getint("data2size",0)){
            val result = mutableListOf<String>()
            result.add(MyApplication.prefs.getdata("data2[$i][0]",""))
            result.add(MyApplication.prefs.getdata("data2[$i][1]",""))
            result.add(MyApplication.prefs.getdata("data2[$i][2]",""))
            data.add(result)
            adapter.notifyDataSetChanged()
        }

        binding.addBtn2.setOnClickListener {
            val intent = Intent(this, MainActivity2_input::class.java)
            requestLauncher.launch(intent)
        }
    }
}

class MyViewHolder2(val binding:ItemMain2Binding):RecyclerView.ViewHolder(binding.root)

class MyAdapter2(val data:MutableList<MutableList<String>>):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder2(ItemMain2Binding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var binding = (holder as MyViewHolder2).binding
        binding.FoodName.text = data[position][0]
        binding.calorie.text = data[position][1]
        binding.nutrient.text = data[position][2]
    }
}