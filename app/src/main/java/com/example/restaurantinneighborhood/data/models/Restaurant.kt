package com.example.restaurantinneighborhood.data.models

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import org.osmdroid.util.GeoPoint
import java.lang.Exception

class Restaurant (snapshot: DataSnapshot) {
    lateinit var id: String
    lateinit var ref: DatabaseReference
    lateinit var description: String
    lateinit var imageUrl: String
    lateinit var location: GeoPoint
    lateinit var name: String
    lateinit var rating: Number
    lateinit var websiteUrl: String

    init {
        try {
            val data: HashMap<String, Any> = snapshot.value as HashMap<String, Any>
            val cords = data["location"] as HashMap<String, Any>
            val point = GeoPoint((cords["Longitude"] as Double), (cords["Latitude"] as Double))
            ref = snapshot.ref
            id = snapshot.key ?: ""
            description = data["description"] as String
            imageUrl = data["imageURL"] as String
            location = point
            name = data["name"] as String
            rating = data["rating"].toString().toFloat()
            websiteUrl = data["websiteUrl"] as String
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}