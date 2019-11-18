package com.example.restaurantinneighborhood.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.restaurantinneighborhood.R
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import java.io.Console

class MapFragment : Fragment() {

    companion object {
        fun newInstance() = MapFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap(view)

    }

    private fun initMap(view: View){
        val map = view.findViewById<MapView>(R.id.map_view)
        print("Initializing map")
        Configuration.getInstance().userAgentValue = "restaurant-in-neighbourhood"
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE )
        map.setMultiTouchControls(true)
        val mapController = map.controller
        mapController.setZoom(15.0)
        val startPoint = GeoPoint(54.2, 16.183333333 )
        mapController.setCenter(startPoint)

    }
}
