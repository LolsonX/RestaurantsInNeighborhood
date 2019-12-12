package com.example.restaurantinneighborhood

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.restaurantinneighborhood.ui.main.FavMapFragment
import com.example.restaurantinneighborhood.ui.main.MapViewPager
import com.example.restaurantinneighborhood.ui.main.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity(), FavMapFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: MapViewPager = findViewById(R.id.view_pager)
        viewPager.setPagingEnabled(false)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

    }

}