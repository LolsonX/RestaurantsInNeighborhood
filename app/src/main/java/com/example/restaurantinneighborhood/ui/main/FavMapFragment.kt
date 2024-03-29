package com.example.restaurantinneighborhood.ui.main

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
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
import com.example.restaurantinneighborhood.ui.helpers.RoundedTransformation
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
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
        adapter.clear()
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

    override fun onResume() {
        super.onResume()
        view!!.invalidate()
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
        view.findViewById<MapView>(R.id.map_view).alpha = 0.0F
        view.findViewById<MapView>(R.id.map_view).isVisible = true
        view.findViewById<MapView>(R.id.map_view).animate().alpha(1.0F)
        view.findViewById<ListView>(R.id.restaurants_list_view).isVisible = false
        view.findViewById<FloatingActionButton>(R.id.fab).setImageResource(R.drawable.ic_menu_white)
    }
    else{
        view.findViewById<MapView>(R.id.map_view).isVisible = false
        view.findViewById<View>(R.id.detail_view).isVisible = false
        view.findViewById<FloatingActionButton>(R.id.fab).animate().translationY(0.0F)
        view.findViewById<ListView>(R.id.restaurants_list_view).alpha = 0.0F
        view.findViewById<ListView>(R.id.restaurants_list_view).isVisible = true
        view.findViewById<ListView>(R.id.restaurants_list_view).animate().alpha(1.0F)
        view.findViewById<FloatingActionButton>(R.id.fab).setImageResource(R.drawable.ic_map_white)


    }

    private fun addPoints(){
        val map = view!!.findViewById<MapView>(R.id.map_view)
        map.overlays.clear()
        restaurantList = ArrayList()
        restaurantList = collection.getData()
        for(restaurant in restaurantList!!){
            Log.println(Log.ERROR, "points", (ratedOnly && restaurant.rating.toFloat() >= 4.0F).toString())
            if(ratedOnly && restaurant.rating.toFloat() >= 4.0F)
            {
                map.overlays.add(generateMarker(restaurant, map))
                adapter.add(restaurant)
                adapter.notifyDataSetChanged()
            }
            else if (!ratedOnly){
                map.overlays.add(generateMarker(restaurant, map))
                adapter.add(restaurant)
                adapter.notifyDataSetChanged()
            }
        }
        map.invalidate()
    }

    private fun generateMarker(restaurant: Restaurant, map: MapView): Marker {
        val marker = Marker(map)
        marker.position = restaurant.location
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_place_red, null)
        marker.title = restaurant.name
        val click = Marker.OnMarkerClickListener(
            fun(marker: Marker, mapView: MapView): Boolean{
                val iv = view!!.findViewById<ImageView>(R.id.restaurant_image)
                Picasso.get().load(restaurant.imageUrl).transform(RoundedTransformation()).into(iv)
                view!!.findViewById<TextView>(R.id.restaurant_name).text = restaurant.name
                view!!.findViewById<TextView>(R.id.restaurant_description).text = restaurant.description
                val ratingBar = view!!.findViewById<RatingBar>(R.id.restaurant_rating)
                ratingBar.rating = restaurant.rating.toFloat()
                ratingBar.stepSize = 0.5F
                ratingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->
                    restaurantList!!.clear()
                    ratingBar.rating = fl
                    collection.updateRating(fl, restaurant)
                }
                val detailView = view!!.findViewById<CollapsingToolbarLayout>(R.id.detail_view)

                if(detailView.alpha != 1.0F){
                    detailView.translationY = detailView.height.toFloat()
                    detailView.visibility = View.VISIBLE
                    detailView.animate().translationY(0.0F)
                    detailView.animate().alpha(1.0F)
                    view!!.findViewById<FloatingActionButton>(R.id.fab).animate().translationY(-330.0F)
                    return true
                }
                else{
                    detailView.animate().translationY(detailView.height.toFloat())
                    view!!.findViewById<FloatingActionButton>(R.id.fab).animate().translationY(0F)
                    detailView.animate().alpha(0.0F)
                    return true
                }
            }
        )
        marker.setOnMarkerClickListener(click)


        return marker
    }



}


