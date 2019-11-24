package com.example.restaurantinneighborhood.data.models

import com.google.firebase.database.DataSnapshot
import org.osmdroid.util.GeoPoint
import java.lang.Exception

class Restaurant (snapshot: DataSnapshot) {
    lateinit var id: String
    lateinit var description: String
    lateinit var imageUrl: String
    lateinit var location: GeoPoint
    lateinit var name: String
    lateinit var rating: Number
    lateinit var websiteUrl: String

    init {
        try {
            val data: HashMap<String, Any> = snapshot.value as HashMap<String, Any>
            id = snapshot.key ?: ""
            description = data["description"] as String
            imageUrl = data["imageUrl"] as String
            location = data["location"] as GeoPoint
            name = data["name"] as String
            rating = data["rating"] as Number
            websiteUrl = data["websiteUrl"] as String
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}