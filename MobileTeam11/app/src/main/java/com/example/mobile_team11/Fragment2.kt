package com.example.mobile_team11

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_team11.MyApplication.Companion.prefs
import com.example.mobile_team11.databinding.Fragment1Binding
import com.example.mobile_team11.databinding.Fragment2Binding
import com.example.mobile_team11.databinding.ItemMain3Binding

class Fragment2 : Fragment() {
    lateinit var binding: Fragment2Binding
    var data = mutableListOf<MutableList<String>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = Fragment2Binding.inflate(inflater,container,false)

        binding.itemMain2.layoutManager = LinearLayoutManager(requireContext())
        val adapter = MyAdapter1(data) { position ->
            AlertDialog.Builder(requireContext()).run {
                setTitle("${data[position][0]}")
                setIcon(android.R.drawable.ic_dialog_info)
                setMessage("운동시간: ${data[position][1]}\n휴식시간: ${data[position][4]}\n" +
                        "세트: ${data[position][2]}\n" + "세트당 횟수: ${data[position][3]}")
                setPositiveButton("OK") { dialog, which -> Log.d("kkang", "OK!") }
                show()
            }
        }
        binding.itemMain2.adapter = adapter
        binding.itemMain2.addItemDecoration(
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        )

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

        return binding.root
    }
}

class MyViewHolderfregment2(val binding: ItemMain3Binding): RecyclerView.ViewHolder(binding.root)

class MyAdapter1(val data:MutableList<MutableList<String>>, private val onItemClick: (Int) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
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

        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }
}