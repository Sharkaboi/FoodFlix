package com.cybershark.foodflix.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cybershark.foodflix.R

class ProfileFragment : Fragment() {

    private lateinit var inflatedView: View
    private lateinit var sharedPreferences: SharedPreferences
    private val spFileName="foodFlixFile"

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
      inflatedView= inflater.inflate(R.layout.fragment_profile, container, false)
      val tvProfileName=inflatedView.findViewById<TextView>(R.id.tvProfileName)
      val tvProfilePhone=inflatedView.findViewById<TextView>(R.id.tvProfilePhone)
      val tvProfileEmail=inflatedView.findViewById<TextView>(R.id.tvProfileEmail)
      val tvProfileLocation=inflatedView.findViewById<TextView>(R.id.tvProfileLocation)
      sharedPreferences=activity!!.getSharedPreferences(spFileName, Context.MODE_PRIVATE)
      tvProfileName.text=sharedPreferences.getString("name","Error")
      tvProfilePhone.text=sharedPreferences.getString("phone","Error")
      tvProfileEmail.text=sharedPreferences.getString("email","Error")
      tvProfileLocation.text=sharedPreferences.getString("address","Error")

      return inflatedView
  }
}
