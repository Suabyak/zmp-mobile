package com.example.mobilesocialapp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.example.mobilesocialapp.databinding.ActivityDashboardBinding
import com.example.mobilesocialapp.fragments.CreateFragment
import com.example.mobilesocialapp.fragments.HomeFragment

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(LayoutInflater.from(this))
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("jwt", Context.MODE_PRIVATE)
        setContentView(binding.root)

        val token = sharedPreferences.getString("jwt", null)

        val bundle = Bundle()
        bundle.putString("token", token)

        val homeFragment = HomeFragment()
        val createFragment = CreateFragment()

        homeFragment.arguments = bundle
        setFragment(homeFragment)

        binding.navigationId.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    homeFragment.arguments = bundle
                    setFragment(homeFragment)
                }

//                R.id.search -> {
//                    val intent = Intent(this, CreateActivity::class.java)
//                    startActivity(intent)
//                }

                R.id.add -> {
                    setFragment(createFragment)
                }

//                R.id.profile -> {
//                    val intent = Intent(this, CreateActivity::class.java)
//                    startActivity(intent)
//                }
            }

            true
        }
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
    }

}