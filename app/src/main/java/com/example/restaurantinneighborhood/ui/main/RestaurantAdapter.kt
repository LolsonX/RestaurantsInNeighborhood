package com.example.restaurantinneighborhood.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.restaurantinneighborhood.data.models.Restaurant
import com.example.restaurantinneighborhood.R
import com.squareup.picasso.Picasso
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.RatingBar
import com.example.restaurantinneighborhood.data.models.RestaurantModel
import com.example.restaurantinneighborhood.ui.helpers.RoundedTransformation


class RestaurantAdapter(private val context: Context,
                        private var dataSource: ArrayList<Restaurant>): BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun getItem(p0: Int): Any {
       return dataSource[p0]
    }

    override fun getItemId(p0: Int): Long {
       return p0.toLong()
    }

    override fun getCount(): Int {
        return dataSource.size
    }

    fun add(restaurant: Restaurant){
        val res = dataSource.find { r -> r.id == restaurant.id }
        if( res ==  null)
        {
            dataSource.add(restaurant)
        }
        else{
            if(res.rating != restaurant.rating){
                dataSource.remove(res)
                notifyDataSetChanged()
            }
        }
    }

    fun clear(){
        dataSource.clear()
    }
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.list_view, p2, false)
        val titleTextView = rowView.findViewById(R.id.restaurant_list_name) as TextView

// Get subtitle element
        val subtitleTextView = rowView.findViewById(R.id.restaurant_list_description) as TextView

// Get detail element
        val detailTextView = rowView.findViewById(R.id.restaurant_list_webUrl) as TextView

// Get thumbnail element
        val thumbnailImageView = rowView.findViewById(R.id.restaurant_list_image) as ImageView

        val ratingView = rowView.findViewById(R.id.restaurant_list_rating) as RatingBar

        val restaurant = getItem(p0) as Restaurant

// 2
        titleTextView.text = restaurant.name
        subtitleTextView.text = restaurant.description
        detailTextView.text = restaurant.websiteUrl
        ratingView.rating = restaurant.rating as Float
        ratingView.setOnRatingBarChangeListener { ratingBar, fl, b ->
            ratingBar.rating = fl
            RestaurantModel.updateRating(fl, restaurant)
        }
        detailTextView.setOnClickListener {
            val webpage = Uri.parse(restaurant.websiteUrl)
            val intent = Intent(Intent.ACTION_VIEW, webpage)
            startActivity(context, intent, Bundle())
        }

// 3
        Picasso.get().load(restaurant.imageUrl).placeholder(R.mipmap.ic_launcher).transform(RoundedTransformation()).into(thumbnailImageView)
        return rowView
    }

}