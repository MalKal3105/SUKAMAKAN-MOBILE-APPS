package com.example.sukamakan

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class CartAdapter(
    val cartItemList: MutableList<CartItem>,
    private val context: Context,
    private val totalView: TextView,
    private val username: String,
    private val database: FirebaseDatabase
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var totalPrice: Double = 0.0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view, totalView)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItemList[position]

        holder.bindCartItemList(cartItemList)
        // Bind data to views
        holder.prodName?.text = cartItem.productName
//        holder.prodName2?.text = cartItem.productName2
        holder.quantity?.text = cartItem.quantity.toString()
//        holder.quantity2?.text = cartItem.quantity.toString()

        // Load image manually
        val resourceId = context.resources.getIdentifier(
            cartItem.productImage.toString(),
            "drawable",
            context.packageName
        )
        holder.imageView.setImageResource(resourceId)
    }

    fun updateCartItemList(newCartItemList: List<CartItem>) {
        cartItemList.clear()
        cartItemList.addAll(newCartItemList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return cartItemList.size
    }

    inner class CartViewHolder(itemView: View, private val totalView: TextView) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.food_Image)
        var minusButton: ImageView = itemView.findViewById(R.id.minus_button)
        var plusButton: ImageView = itemView.findViewById(R.id.plus_button)
        var delButton: ImageView = itemView.findViewById(R.id.del_button)
        var prodName: TextView = itemView.findViewById(R.id.prod_name)
        var quantity: TextView = itemView.findViewById(R.id.quantity)

        init {
            // Set click listeners for plusButton and minusButton
            plusButton.setOnClickListener {

                updateQuantity(1, totalView)
            }

            minusButton.setOnClickListener {

                updateQuantity(-1, totalView)
            }

            delButton.setOnClickListener {
                val currentPosition = adapterPosition
                removeItem(currentPosition)
            }
        }

        fun bindCartItemList(cartItemList: List<CartItem>) {
            if (cartItemList.isNotEmpty()) {
                // If there's at least one item in the cart list
                prodName.text = cartItemList[0].productName
                quantity.text = cartItemList[0].quantity.toString()

                if (cartItemList.size > 1) {
                    // If there are more than one items, dynamically add prod_name2 TextView
                    val prodName2 = TextView(context)
                    val quant2 = TextView(context)
                    prodName2.text = cartItemList[1].productName
                    quant2.text = cartItemList[0].quantity.toString()

                }

                if (cartItemList.size > 2) {
                    val prodName3 = TextView(context)
                    val quant3 = TextView(context)
                    prodName3.text = cartItemList[2].productName
                    quant3.text = cartItemList[2].quantity.toString()
                }
            } else {
                // Clear the text views if the cart is empty
                prodName.text = ""
                quantity.text = ""
            }
        }


        private fun updateQuantity(change: Int, totalView: TextView) {
            val newPosition = adapterPosition
            if (newPosition != RecyclerView.NO_POSITION) {
                val cartItem = cartItemList[newPosition]
                val newQuantity = cartItem.quantity + change

                // Ensure the quantity is not negative
                if (newQuantity >= 0) {
                    cartItem.quantity = newQuantity
                    if (newPosition == 0) {
                        quantity.text = newQuantity.toString()
                    } else if (newPosition == 1) {
                        quantity.text = newQuantity.toString()
                    } else if (newPosition == 2) {
                        quantity.text = newQuantity.toString()
                    }
                    // Update the total price
                    updateTotalPrice(cartItem, change, totalView)
                }
            }
        }

        private fun updateTotalPrice(cartItem: CartItem, change: Int, totalView: TextView) {
            // Update the total price based on the change in quantity
            totalPrice += cartItem.productPrice * change
            totalView.text = String.format("%.2f", totalPrice.toDouble())
        }

        private fun updateTotalPrice(removedItem: CartItem) {
            // Update the total price by subtracting the price of the removed item
            totalPrice -= removedItem.productPrice * removedItem.quantity
            totalView.text = String.format("%.2f", totalPrice)
        }

        private fun removeItem(position: Int) {
            if (position in 0 until cartItemList.size) {
                val removedItem = cartItemList[position]

                // Log the cart ID value
                Log.d("CartItem", "Cart ID: ${removedItem.cartID}")

                // Get a reference to the cart item in the database
                val userCartRef =
                    removedItem.cartID?.let {
                        database.getReference("user").child(username).child("cart").child(
                            it
                        )
                    }

                // Remove the item from the database
                if (userCartRef != null) {
                    userCartRef.removeValue()
                }

                // Remove the item from the RecyclerView
                cartItemList.removeAt(position)
                notifyItemRemoved(position)

                // Optionally, you can notify the range changed to update other items' positions
                notifyItemRangeChanged(position, cartItemList.size - position)

                // Update the total price by subtracting the price of the removed item
                updateTotalPrice(removedItem)
            }
        }
    }
}


