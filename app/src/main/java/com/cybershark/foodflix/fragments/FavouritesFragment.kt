package com.cybershark.foodflix.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cybershark.foodflix.R


class FavouritesFragment : Fragment() {

    private lateinit var inflatedView:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        inflatedView= inflater.inflate(R.layout.fragment_favourites, container, false)
        return inflatedView
    }

}
