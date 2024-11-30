package com.example.mobile_team11

import android.content.Intent
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
import com.example.mobile_team11.databinding.Fragment1Binding
import com.example.mobile_team11.databinding.ItemMain2Binding


class Fragment1 : Fragment() {
    lateinit var binding:Fragment1Binding
    var data = mutableListOf<MutableList<String>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = Fragment1Binding.inflate(inflater,container,false)

        binding.itemMain1.layoutManager = LinearLayoutManager(requireContext())
        val adapter = MyAdapter(data) { position ->
            AlertDialog.Builder(requireContext()).run {
                setTitle("${data[position][0]}")
                setIcon(android.R.drawable.ic_dialog_info)
                setMessage("칼로리: ${data[position][1]}\n영양소: ${data[position][2]}")
                setPositiveButton("OK") { dialog, which -> Log.d("kkang", "OK!") }
                show()

            }
        }
        binding.itemMain1.adapter = adapter
        binding.itemMain1.addItemDecoration(
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        )

        for( i in 1..MyApplication.prefs.getint("data2size",0)){
            val result = mutableListOf<String>()
            result.add(MyApplication.prefs.getdata("data2[$i][0]",""))
            result.add(MyApplication.prefs.getdata("data2[$i][1]",""))
            result.add(MyApplication.prefs.getdata("data2[$i][2]",""))
            data.add(result)

            adapter.notifyDataSetChanged()
        }

        return binding.root
    }
}
class MyViewHolderfregment(val binding: ItemMain2Binding): RecyclerView.ViewHolder(binding.root)

class MyAdapter(val data: MutableList<MutableList<String>>, private val onItemClick: (Int) -> Unit): RecyclerView.Adapter<MyViewHolderfregment>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderfregment {
        return MyViewHolderfregment(ItemMain2Binding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolderfregment, position: Int) {
        val binding = holder.binding
        binding.FoodName.text = data[position][0]
        binding.calorie.text = data[position][1]
        binding.nutrient.text = data[position][2]

        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }
}