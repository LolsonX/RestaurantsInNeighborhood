package com.example.restaurantinneighborhood.data.models

import android.util.Log
import com.google.firebase.database.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

object RestaurantModel: Observable() {
    private var valueDataListener: ValueEventListener? = null
    private var restaurantList: ArrayList<Restaurant>? = ArrayList()

    private fun getDatabaseRef(): DatabaseReference? {
        return FirebaseDatabase.getInstance().reference.child("restaurants")
    }

    init {
        if (valueDataListener != null) {
            getDatabaseRef()?.removeEventListener(valueDataListener as ValueEventListener)
        }
        valueDataListener = null
        valueDataListener = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    val data: ArrayList<Restaurant> = ArrayList()
                    for (snapshot: DataSnapshot in dataSnapshot.children) {
                        try {
                            data.add(Restaurant(snapshot))
                        }
                        catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    restaurantList = data
                    Log.i("RestaurantModel", "data updated, there are "
                            + restaurantList!!.size + " restaurants in the cache")
                    setChanged()
                    notifyObservers()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        getDatabaseRef()?.addValueEventListener(valueDataListener as ValueEventListener)
    }

    fun getData(): ArrayList<Restaurant>? {
        return restaurantList
    }
}