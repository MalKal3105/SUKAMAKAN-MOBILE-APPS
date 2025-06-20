package com.example.sukamakan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderHistoryAdapter(private val orderList: List<Order>) :
    RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val orderIdTextView: TextView = itemView.findViewById(R.id.orderIdTextView)
        val totalQuantityTextView: TextView = itemView.findViewById(R.id.quantity)
        val totalPriceTextView: TextView = itemView.findViewById(R.id.price_total)
        val foodNameTextView: TextView = itemView.findViewById(R.id.Order1)
        val foodName2TextView: TextView = itemView.findViewById(R.id.Order2)
        val foodName3TextView: TextView = itemView.findViewById(R.id.Order3)
        val quantityTextView: TextView = itemView.findViewById(R.id.quantity)
        val quantity2TextView: TextView = itemView.findViewById(R.id.quantity2)
        val quantity3TextView: TextView = itemView.findViewById(R.id.quantity3)
        val orderIDTextView: TextView = itemView.findViewById(R.id.order_ID)
        val timesz: TextView = itemView.findViewById(R.id.time_view)
        val table: TextView = itemView.findViewById(R.id.table_num)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_history, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        // Bind data to views
        // Add logging statements
        Log.d("OrderHistoryAdapter", "Order ID: ${order.orderId}")
        Log.d("OrderHistoryAdapter", "Total Quantity: ${order.totalQuantity}")
        Log.d("OrderHistoryAdapter", "Total Price: ${order.totalPriceRM}")
        Log.d("OrderHistoryAdapter", "Food Name: ${order.foodName}")
        Log.d("OrderHistoryAdapter", "Food Name2: ${order.foodName2}")
        Log.d("OrderHistoryAdapter", "Food Name3: ${order.foodName3}")
        Log.d("OrderHistoryAdapter", "Food Name: ${order.quantity}")
        Log.d("OrderHistoryAdapter", "Food Name2: ${order.quantity2}")
        Log.d("OrderHistoryAdapter", "Food Name3: ${order.quantity3}")
        Log.d("OrderHistoryAdapter", "Time: ${order.time}")
        Log.d("OrderHistoryAdapter", "Table: ${order.table}")
//        holder.orderIdTextView.text = "Order ID: ${order.orderId}"
        holder.totalQuantityTextView.text = "${order.totalQuantity}"
        holder.totalPriceTextView.text = "RM${order.totalPriceRM}"
        holder.foodNameTextView.text = "${order.foodName}"
        holder.foodName2TextView.text = "${order.foodName2}"
        holder.foodName3TextView.text = "${order.foodName3}"
        holder.quantityTextView.text = "${order.quantity}"
        holder.quantity2TextView.text = "${order.quantity2}"
        holder.quantity3TextView.text = "${order.quantity3}"
        holder.orderIDTextView.text = "${order.orderId}"
        holder.timesz.text = "${order.time}"
        holder.table.text = "${order.table}"
    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}
