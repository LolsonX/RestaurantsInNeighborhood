package com.example.restaurantinneighborhood.ui.main

import android.content.ClipDescription
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.core.view.isVisible

import com.example.restaurantinneighborhood.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import com.example.restaurantinneighborhood.data.models.Restaurant
import com.example.restaurantinneighborhood.data.models.RestaurantModel
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import org.osmdroid.views.CustomZoomButtonsController
import java.util.*
import kotlin.collections.ArrayList

private const val isFavourite = "isFavourite"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FavMapFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FavMapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavMapFragment : Fragment(), Observer {
    private var collection = RestaurantModel
    private var restaurantList: ArrayList<Restaurant>? = ArrayList()
    private var isFavourite: String? = null
    var ratedOnly: Boolean = false
    private var listener: OnFragmentInteractionListener? = null
    private var isMapView = true
    private lateinit var adapter: RestaurantAdapter

    fun observe(o: Observable){
        o.addObserver(this)
    }
    override fun update(o: Observable?, arg: Any?) {
        restaurantList!!.clear()
        adapter.notifyDataSetInvalidated()
        restaurantList = ((o as RestaurantModel).getData())
        addPoints()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observe(collection)
        arguments?.let {
            isFavourite = it.getString(isFavourite)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav_map, container, false)
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param isFavourite
         * @return A new instance of fragment FavMapFragment.
         */
        @JvmStatic
        fun newInstance(isFavourite: Boolean) =
            FavMapFragment().apply {
                arguments = Bundle().apply {
                    putBoolean("isFavourite", true)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fab: FloatingActionButton = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            isMapView = !isMapView
            showVisible(view)
        }
        initMap(view)
    }


    private fun initMap(view: View){
        val listView = view.findViewById<ListView>(R.id.restaurants_list_view)
        adapter = RestaurantAdapter(context!!, restaurantList!!)
        listView.adapter = adapter
        val map = view.findViewById<MapView>(R.id.map_view)
        print("Initializing map")
        Configuration.getInstance().userAgentValue = "restaurant-in-neighbourhood"
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE )
        map.setMultiTouchControls(true)
        map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        val mapController = map.controller
        mapController.setZoom(15.0)
        val startPoint = GeoPoint(54.2, 16.183333333 )
        mapController.setCenter(startPoint)

    }

    private fun showVisible(view: View) = if (isMapView){
        view.findViewById<MapView>(R.id.map_view).isVisible = true
        view.findViewById<ListView>(R.id.restaurants_list_view).isVisible = false
        view.findViewById<FloatingActionButton>(R.id.fab).setImageResource(R.drawable.ic_menu_white)
    }
    else{
        view.findViewById<MapView>(R.id.map_view).isVisible = false
        view.findViewById<ListView>(R.id.restaurants_list_view).isVisible = true
        view.findViewById<FloatingActionButton>(R.id.fab).setImageResource(R.drawable.ic_map_white)


    }

    private fun addPoints(){
        val map = view!!.findViewById<MapView>(R.id.map_view)
        map.overlays.clear()
        for(restaurant in this.restaurantList!!){
            if(ratedOnly && restaurant.rating.toFloat() >= 4.0F)
            {
                map.overlays.add(generateMarker(restaurant.location, restaurant.name, map, restaurant.imageUrl, restaurant.description))
                adapter.add(restaurant)
                adapter.notifyDataSetChanged()
            }
            else if (!ratedOnly){
                map.overlays.add(generateMarker(restaurant.location, restaurant.name, map, restaurant.imageUrl, restaurant.description))
                adapter.add(restaurant)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun generateMarker(position: GeoPoint, name: String, map: MapView, url:String, description: String): Marker {
        val marker = Marker(map)
        marker.position = position
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_place_red, null)
        marker.title = name
        val click = Marker.OnMarkerClickListener(
            fun(marker: Marker, mapView: MapView): Boolean{
                val iv = view!!.findViewById<ImageView>(R.id.restaurant_image)
                Picasso.get().load(url).into(iv)
                view!!.findViewById<TextView>(R.id.restaurant_description).text = description
                val detailView = view!!.findViewById<CollapsingToolbarLayout>(R.id.detail_view)

                if(!detailView.isVisible){
                    detailView.visibility = View.VISIBLE
                    view!!.findViewById<FloatingActionButton>(R.id.fab).translationY = -200.0F
                    return true
                }
                else{
                    detailView.visibility = View.INVISIBLE
                    view!!.findViewById<FloatingActionButton>(R.id.fab).translationY = 0.0F
                    return true
                }
            }
        )
        marker.setOnMarkerClickListener(click)


        return marker
    }



}


