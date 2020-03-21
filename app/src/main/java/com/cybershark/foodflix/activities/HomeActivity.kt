package com.cybershark.foodflix.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import com.cybershark.foodflix.R
import com.cybershark.foodflix.fragments.*
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val spFileName="foodFlixFile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        sharedPreferences=getSharedPreferences(spFileName, Context.MODE_PRIVATE)
        val bundle=intent.extras

        //sets nav bar icon
        val toggle= ActionBarDrawerToggle(this,drawer_layout,toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        supportFragmentManager.beginTransaction().add(
            R.id.fragmentContainer,
            RestaurantsFragment()
        ).addToBackStack("restaurants").commit()
        toolbar.title = "All Restaurants"
        //sets nav bar listener
        nav_view.setNavigationItemSelectedListener{
            when(it.itemId){
                R.id.home_item ->{
                    toolbar.title="All Restaurants"
                    if (supportFragmentManager.backStackEntryCount>1)
                        supportFragmentManager.popBackStack()
                }
                R.id.fav_res_item ->{
                    if (supportFragmentManager.backStackEntryCount>1)
                        supportFragmentManager.popBackStack()
                    supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, FavouritesFragment()).addToBackStack("favourites").commit()
                }
                R.id.profile_item ->{
                    if (supportFragmentManager.backStackEntryCount>1)
                        supportFragmentManager.popBackStack()
                    supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, ProfileFragment()).addToBackStack("profile").commit()
                }
                R.id.faq_item ->{
                    if (supportFragmentManager.backStackEntryCount>1)
                        supportFragmentManager.popBackStack()
                    supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, FaqFragment()).addToBackStack("faq").commit()
                }
                R.id.order_history_item ->{
                    if (supportFragmentManager.backStackEntryCount>1)
                        supportFragmentManager.popBackStack()
                    supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, OrderHistoryFragment()).addToBackStack("history").commit()
                }
                R.id.logout_item -> {
                    AlertDialog
                        .Builder(this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure You want to log out?")
                        .setPositiveButton("Yes") { dialog, whichButton ->
                            //log out
                            startActivity(Intent(this,
                                LoginActivity::class.java))
                            finishAffinity()
                            dialog.dismiss()
                        }
                        .setNegativeButton("No"){ dialog, whichButton ->
                            dialog.dismiss()
                        }
                }
                else->{}
            }
            drawer_layout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }
    }

    override fun onBackPressed() {
        when {
            drawer_layout.isDrawerOpen(GravityCompat.START) -> drawer_layout.closeDrawer(GravityCompat.START)
        }
    }
}
