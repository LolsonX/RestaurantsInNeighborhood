package com.example.restaurantinneighborhood.ui.main

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible

import com.example.restaurantinneighborhood.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val isFavourite = "isFavourite"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FavMapFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FavMapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavMapFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var isFavourite: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var isMapView = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    // TODO: Rename method, update argument and hook method into UI event
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
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param isFavourite Parameter 1.
         * @return A new instance of fragment FavMapFragment.
         */
        // TODO: Rename and change types and number of parameters
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

    private fun showVisible(view: View){
        if (isMapView){
            view.findViewById<MapView>(R.id.map_view).isVisible = true
            view.findViewById<MapView>(R.id.restaurants_list_view).isVisible = false
            view.findViewById<FloatingActionButton>(R.id.fab).setImageResource(R.drawable.ic_menu_white)
        }
        else{
            view.findViewById<MapView>(R.id.map_view).isVisible = false
            view.findViewById<MapView>(R.id.restaurants_list_view).isVisible = true
            view.findViewById<FloatingActionButton>(R.id.fab).setImageResource(R.drawable.ic_map_white)

        }
    }
}
