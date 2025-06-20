package com.example.sukamakan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrderHistoryActivity : AppCompatActivity() {

    private lateinit var userId: String
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var username: String
    private lateinit var password: String
    private lateinit var orderRecyclerView: RecyclerView
    private lateinit var orderHistoryAdapter: OrderHistoryAdapter
    private val orderList = mutableListOf<Order>()  // Maintain a list of orders


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        // Retrieve user data from intent
        userId = intent.getStringExtra("senduser") ?: ""
        name = intent.getStringExtra("sendname") ?: ""
        email = intent.getStringExtra("sendemail") ?: ""
        username = intent.getStringExtra("sendusername") ?: ""
        password = intent.getStringExtra("sendpassword") ?: ""

        fetchOrderHistoryFromDatabase()

        // Initialize RecyclerView and Adapter
        orderRecyclerView = findViewById(R.id.order_history_rec)  // Replace with your actual RecyclerView ID
        orderHistoryAdapter = OrderHistoryAdapter(orderList)

        // Set LayoutManager for RecyclerView
        val layoutManager = LinearLayoutManager(this)
        orderRecyclerView.layoutManager = layoutManager

        // Set the adapter to the RecyclerView
        orderRecyclerView.adapter = orderHistoryAdapter

        // Set click listener for the back button
        val backButton = findViewById<ImageView>(R.id.back_icon)  // Replace with your actual back button ID
        backButton.setOnClickListener {

            // Retrieve user data from intent
            userId = intent.getStringExtra("senduser") ?: ""
            name = intent.getStringExtra("sendname") ?: ""
            email = intent.getStringExtra("sendemail") ?: ""
            username = intent.getStringExtra("sendusername") ?: ""
            password = intent.getStringExtra("sendpassword") ?: ""


            val intent = Intent(this, FoodListActivity::class.java).apply {
                putExtra("senduser", userId)
                putExtra("sendname", name)
                putExtra("sendemail", email)
                putExtra("sendusername", username)
                putExtra("sendpassword", password)
            }
            startActivity(intent)
            finish() // Optional: finish the current activity to remove it from the back stack
        }
    }

    private fun fetchOrderHistoryFromDatabase() {
        val database = FirebaseDatabase.getInstance()
        val ordersRef = database.getReference("user").child(username).child("orders")

        ordersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Clear the existing order list
                orderList.clear()

                for (orderSnapshot in dataSnapshot.children) {
                    val orderData = orderSnapshot.value as? Map<*, *>  // Get raw data

                    // Skip null entries
                    if (orderData == null) {
                        Log.d("OrderHistoryActivity", "Raw order data is null. Skipping.")
                        continue
                    }

                    Log.d("OrderHistoryActivity", "Raw order data: $orderData")

                    val orderId = orderData["orderId"]?.toString() ?: ""
                    val totalQuantity = (orderData["totalQuantity"] as? Long)?.toInt() ?: 0
                    val totalPriceRM = orderData["totalPriceRM"]
                    val totalPrice = when (totalPriceRM) {
                        is Double -> totalPriceRM
                        is Long -> totalPriceRM.toDouble()
                        is String -> totalPriceRM.toDoubleOrNull() ?: 0.0
                        else -> 0.0
                    }
                    val foodName = orderData["foodName"]?.toString() ?: ""
                    val foodName2 = orderData["foodName2"]?.toString() ?: ""
                    val foodName3 = orderData["foodName3"]?.toString() ?: ""
                    val quantity = orderData["quantity"]?.toString() ?: ""
                    val quantity2 = orderData["quantity2"]?.toString() ?: ""
                    val quantity3 = orderData["quantity3"]?.toString() ?: ""
                    val username = orderData["username"]?.toString() ?: ""
                    val time = orderData["time"]?.toString() ?: ""
                    val tablesz = orderData["table"]?.toString() ?: ""

                    Log.d("OrderHistoryActivity", "Order ID: $orderId")
                    Log.d("OrderHistoryActivity", "Total Quantity: $totalQuantity")
                    Log.d("OrderHistoryActivity", "Total Price: $totalPrice")
                    Log.d("OrderHistoryActivity", "Food Name: $foodName")
                    Log.d("OrderHistoryActivity", "Food Name2: $foodName2")
                    Log.d("OrderHistoryActivity", "Food Name3: $foodName3")
                    Log.d("OrderHistoryActivity", "quantity: $quantity")
                    Log.d("OrderHistoryActivity", "quantity2: $quantity2")
                    Log.d("OrderHistoryActivity", "quantity3: $quantity3")
                    Log.d("OrderHistoryActivity", "time: $time")
                    Log.d("OrderHistoryActivity", "table order: $tablesz")

                    val order = Order(orderId, totalQuantity, totalPrice, foodName, foodName2, foodName3,quantity, quantity2,quantity3,username, time,tablesz)
                    orderList.add(order)
                }
                // Notify the adapter that the data set has changed
                orderHistoryAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("OrderHistoryActivity", "Error fetching order history: ${databaseError.message}")
            }
        })
    }
}
