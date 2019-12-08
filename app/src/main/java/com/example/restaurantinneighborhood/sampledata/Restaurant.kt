package com.example.restaurantinneighborhood.sampledata

import org.osmdroid.util.GeoPoint

class Restaurant {
    var name: String = ""
    var description: String = ""
    var imageURL: String = ""
    var location: GeoPoint = GeoPoint(0.0, 0.0)
    var rating: Int = 1
    var websiteURL: String = ""

    constructor(
        name: String,
        description: String,
        imageURL: String,
        location: GeoPoint,
        rating: Int,
        websiteURL: String
    ) {
        this.name = name
        this.description = description
        this.imageURL = imageURL
        this.location = location
        this.rating = rating
        this.websiteURL = websiteURL
    }
}