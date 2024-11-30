package com.example.mobile_team11

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_team11.databinding.ActivityMain3Binding
import com.example.mobile_team11.databinding.ItemMain2Binding
import com.example.mobile_team11.databinding.ItemMain3Binding

class MainActivity3 : AppCompatActivity() {
    lateinit var binding: ActivityMain3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "운동 루틴 관리"

        val data = mutableListOf<MutableList<String>>()

        binding.recyclerViewExercise.layoutManager = LinearLayoutManager(this)
        val adapter = MyAdapter3(data)
        binding.recyclerViewExercise.adapter = adapter

        val requestLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            var name = it.data?.getStringExtra("name")
            var time = it.data?.getStringExtra("time")
            var set = it.data?.getStringExtra("set")
            var count = it.data?.getStringExtra("count")
            var relax = it.data?.getStringExtra("relax")
            val resultdata = mutableListOf<String>()
            if (name != null&&time != null&&set != null&&count != null&&relax != null) {
                resultdata.add(name)
                resultdata.add(time)
                resultdata.add(set)
                resultdata.add(count)
                resultdata.add(relax)
                data.add(resultdata)
                adapter.notifyDataSetChanged()
                MyApplication.prefs.setdata("data3[${data.size}][0]",name)
                MyApplication.prefs.setdata("data3[${data.size}][1]",time)
                MyApplication.prefs.setdata("data3[${data.size}][2]",set)
                MyApplication.prefs.setdata("data3[${data.size}][3]",count)
                MyApplication.prefs.setdata("data3[${data.size}][4]",relax)
                MyApplication.prefs.setint("data3size",data.size)
            }
        }
        for( i in 1..MyApplication.prefs.getint("data3size",0)){
            val result = mutableListOf<String>()
            result.add(MyApplication.prefs.getdata("data3[$i][0]",""))
            result.add(MyApplication.prefs.getdata("data3[$i][1]",""))
            result.add(MyApplication.prefs.getdata("data3[$i][2]",""))
            result.add(MyApplication.prefs.getdata("data3[$i][3]",""))
            result.add(MyApplication.prefs.getdata("data3[$i][4]",""))
            data.add(result)
            adapter.notifyDataSetChanged()
        }


        binding.addBtn3.setOnClickListener {
            val intent = Intent(this, MainActivity3_input::class.java)
            requestLauncher.launch(intent)
        }
    }
}

class MyViewHolder3(val binding: ItemMain3Binding): RecyclerView.ViewHolder(binding.root)

class MyAdapter3(val data:MutableList<MutableList<String>>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder3(ItemMain3Binding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var binding = (holder as MyViewHolder3).binding
        binding.ExerciseName.text = data[position][0]
        binding.ExerciseTime.text = data[position][1]
        binding.Set.text = data[position][2]
        binding.NumofSet.text = data[position][3]
        binding.RestTime.text = data[position][4]
    }
}