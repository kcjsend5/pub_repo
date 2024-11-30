package com.example.mobile_team11

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentTransaction
import com.example.mobile_team11.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Main"

        toggle = ActionBarDrawerToggle(this,binding.drawer,R.string.draweropen,R.string.drawerclosed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        binding.Navigation.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu1 -> startActivity(Intent(this, MainActivity2::class.java))
                R.id.menu2 -> startActivity(Intent(this, MainActivity3::class.java))
            }
            true
        }

        binding.tab.addTab(binding.tab.newTab().apply { text = "오늘의 식단" })
        binding.tab.addTab(binding.tab.newTab().apply { text = "오늘의 운동" })
        val transaction:FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.mainactivity,Fragment1())
        transaction.commit()
        binding.tab.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(p0: TabLayout.Tab?) {
                when(p0!!.text){
                    "오늘의 식단"->{
                        val transaction:FragmentTransaction = supportFragmentManager.beginTransaction()
                        binding.today.text = "오늘의 식단"
                        transaction.replace(R.id.mainactivity,Fragment1())
                        transaction.commit()
                    }

                    "오늘의 운동"-> {
                        val transaction:FragmentTransaction = supportFragmentManager.beginTransaction()
                        binding.today.text = "오늘의 운동"
                        transaction.replace(R.id.mainactivity, Fragment2())
                        transaction.commit()
                    }
                }
            }
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabReselected(p0: TabLayout.Tab?) {}
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}