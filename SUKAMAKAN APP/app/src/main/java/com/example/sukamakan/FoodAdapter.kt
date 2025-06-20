package com.example.sukamakan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoodAdapter(private var originalList: MutableList<Food>) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    private var horFoodList: List<Food> = originalList.toList() // Copy of the original list
    private var onItemClickListener: ((Int) -> Unit)? = null

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodImageView: ImageView = itemView.findViewById(R.id.hor_img)
        val foodNameTv: TextView = itemView.findViewById(R.id.hor_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.food_list_horizontal, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = horFoodList[position]
        holder.foodImageView.setImageResource(food.foodImage)
        holder.foodNameTv.text = food.foodName

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return horFoodList.size
    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    // Inside your FoodAdapter
    fun filter(query: String) {
        val filteredList = mutableListOf<Food>()
        for (food in originalList) {
            if (food.foodName.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(food)
            }
        }
        setList(filteredList)
    }

    // Inside your FoodAdapter
    fun setList(newList: List<Food>) {
        horFoodList = newList
        notifyDataSetChanged()
    }

    // Inside your FoodAdapter
    fun resetList() {
        setList(originalList)
    }
}

