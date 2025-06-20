package com.example.sukamakan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoodVerAdapter(
    private var verFoodList: List<FoodVer>,
    private var addToCartClickListener: FoodListActivity
) : RecyclerView.Adapter<FoodVerAdapter.FoodViewHolder>() {

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodImageView: ImageView = itemView.findViewById(R.id.ver_img)
        val foodNameTv: TextView = itemView.findViewById(R.id.name)
        val foodPriceTv: TextView = itemView.findViewById(R.id.price)
        val foodImageRatingView: ImageView = itemView.findViewById(R.id.imageView)
        val foodRatingTv: TextView = itemView.findViewById(R.id.textView4)
        val foodTimeTv: TextView = itemView.findViewById(R.id.time)
        val cartButton: Button = itemView.findViewById(R.id.cart_button)
    }

    // Interface to handle item click events
    interface OnAddToCartClickListener {
        fun onAddToCartClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.food_list_vertical, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        with(holder) {
            val food = verFoodList[position]
            foodImageView.setImageResource(food.foodImage)
            foodNameTv.text = food.foodName
            foodPriceTv.text = food.foodPrice
            foodImageRatingView.setImageResource(food.foodRating)
            foodRatingTv.text = food.foodRate
            foodTimeTv.text = food.foodTime

            cartButton.setOnClickListener {
                addToCartClickListener.onAddToCartClick(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return verFoodList.size
    }

    // Inside your FoodVerAdapter
    fun filter(query: String) {
        val filteredList = mutableListOf<FoodVer>()
        for (food in verFoodList) {
            if (food.foodName.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(food)
            }
        }
        setList(filteredList)
    }

    // Inside your FoodVerAdapter
    fun setList(newList: List<FoodVer>) {
        verFoodList = newList
        notifyDataSetChanged()
    }

    // Inside your FoodVerAdapter
    fun resetList() {
        setList(verFoodList)
    }
}
