package com.example.catering_boys.main

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catering_boys.R
import com.example.catering_boys.address.AlamatActivity // Import Activity Alamat
import com.example.catering_boys.databinding.ActivityMainBinding
import com.example.catering_boys.history.HistoryOrderActivity
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var modelCategoriesList: MutableList<ModelCategories> = ArrayList()
    private var modelTrendingList: MutableList<ModelTrending> = ArrayList()
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var trendingAdapter: TrendingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusbar()
        setInitLayout()
        setCategories()
        setTrending()
        setupBottomNavigation()
    }

    private fun setInitLayout() {
        // Tombol Riwayat Pesanan
        binding.cvHistory.setOnClickListener {
            val intent = Intent(this@MainActivity, HistoryOrderActivity::class.java)
            startActivity(intent)
        }

        // BARU: Klik tombol "Bantul, DIY" untuk membuka Google Maps
        binding.btnLocationHome.setOnClickListener {
            val intent = Intent(this@MainActivity, AlamatActivity::class.java)
            startActivity(intent)
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                filterData(s.toString())
            }
            override fun afterTextChanged(s: Editable) {}
        })

        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 3)
            setHasFixedSize(true)
        }

        binding.rvTrending.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.nav_home

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_dashboard -> {
                    startActivity(Intent(this, SellerDashboardActivity::class.java))
                    true
                }
                R.id.nav_account -> {
                    startActivity(Intent(this, AccountActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun filterData(text: String) {
        val filteredList = modelTrendingList.filter {
            it.tvPlaceName.lowercase().contains(text.lowercase())
        }

        if (::trendingAdapter.isInitialized) {
            trendingAdapter.setFilter(filteredList)
        }
    }

    private fun setCategories() {
        modelCategoriesList.clear()
        modelCategoriesList.add(ModelCategories(R.drawable.ic_complete, "Complete Package"))
        modelCategoriesList.add(ModelCategories(R.drawable.ic_saving, "Saving Package"))
        modelCategoriesList.add(ModelCategories(R.drawable.ic_healthy, "Healthy Package"))
        modelCategoriesList.add(ModelCategories(R.drawable.ic_fast, "FastFood"))
        modelCategoriesList.add(ModelCategories(R.drawable.ic_event, "Event Packages"))
        modelCategoriesList.add(ModelCategories(R.drawable.ic_more_food, "Others"))

        categoriesAdapter = CategoriesAdapter(this, modelCategoriesList)
        binding.rvCategories.adapter = categoriesAdapter
    }

    private fun setTrending() {
        modelTrendingList.clear()
        modelTrendingList.add(ModelTrending(R.drawable.complete_1, "Menu 1", "2.200 disukai"))
        modelTrendingList.add(ModelTrending(R.drawable.complete_2, "Menu 2", "1.220 disukai"))
        modelTrendingList.add(ModelTrending(R.drawable.complete_3, "Menu 3", "345 disukai"))
        modelTrendingList.add(ModelTrending(R.drawable.complete_4, "Menu 4", "590 disukai"))

        trendingAdapter = TrendingAdapter(this, modelTrendingList)
        binding.rvTrending.adapter = trendingAdapter
    }

    private fun setStatusbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }
}