package com.example.restaurantinneighborhood.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.restaurantinneighborhood.R

private val TAB_TITLES = arrayOf(
    R.string.All,
    R.string.Favourites
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a FavouriteMapFragment (defined as a static inner class below).
        if (position == 0) {
            val elem = FavMapFragment.newInstance(false)
            elem.ratedOnly = false
            return elem
        } else {
            val elem = FavMapFragment.newInstance(true)
            elem.ratedOnly = true
            return elem
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
}