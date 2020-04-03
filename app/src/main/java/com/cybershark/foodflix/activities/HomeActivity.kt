package com.cybershark.foodflix.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.cybershark.foodflix.R
import com.cybershark.foodflix.fragments.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val spFileName="foodFlixFile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        sharedPreferences=getSharedPreferences(spFileName, Context.MODE_PRIVATE)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        val tvProfileName = headerView.findViewById<TextView>(R.id.tvProfileName)
        val tvPhone=headerView.findViewById<TextView>(R.id.tvPhone)
        tvProfileName.text=sharedPreferences.getString("name","")
        tvPhone.text=sharedPreferences.getString("phone","")

        //sets nav bar icon
        val toggle= ActionBarDrawerToggle(this,drawer_layout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        //opens default fragment
        openRestaurantsFragment()
        toolbar.title = "All Restaurants"

        //sets nav bar listener
        nav_view.setNavigationItemSelectedListener{
            when(it.itemId){
                R.id.home_item ->{
                    openRestaurantsFragment()
                }
                R.id.fav_res_item ->{
                    toolbar.title = "Favorites"
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, FavouritesFragment()).commit()
                }
                R.id.profile_item ->{
                    toolbar.title = "Profile"
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, ProfileFragment()).commit()
                }
                R.id.faq_item ->{
                    toolbar.title = "FAQ"
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, FaqFragment()).commit()
                }
                R.id.order_history_item ->{
                    toolbar.title = "Order History"
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, OrderHistoryFragment()).commit()
                }
                R.id.logout_item -> {
                    Log.e("foodflix","log out pressed")
                    AlertDialog
                        .Builder(this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure You want to log out?")
                        .setPositiveButton("Yes") { dialog, whichButton ->
                            //log out
                            sharedPreferences.edit().putBoolean("isLoggedIn",false).apply()
                            startActivity(Intent(this,
                                LoginActivity::class.java))
                            finishAffinity()
                            dialog.dismiss()
                        }
                        .setNegativeButton("No"){ dialog, whichButton ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
            drawer_layout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }
    }

    private fun openRestaurantsFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, RestaurantsFragment()).commit()
        toolbar.title = "All Restaurants"
    }

    override fun onBackPressed() {
        when {
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) !is RestaurantsFragment -> {
                openRestaurantsFragment()
            }
            drawer_layout.isDrawerOpen(GravityCompat.START) -> {
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            else -> super.onBackPressed()
        }
    }
}