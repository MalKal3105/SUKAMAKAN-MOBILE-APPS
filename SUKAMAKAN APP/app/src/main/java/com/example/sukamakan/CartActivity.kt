package com.example.sukamakan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class CartActivity : AppCompatActivity() {

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var totalView: TextView
    private lateinit var orderButton: Button
    private lateinit var cartid: String
    private lateinit var userId: String
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var username: String
    private lateinit var password: String
    private lateinit var foodName: String
    private var foodPrice: Double = 0.0
    private var foodImage: Int = R.drawable.pizza1
    private lateinit var backButton: ImageView
    private lateinit var tableNum: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        Log.d("OrderPlacedActivity", "onCreate called")

        // Retrieve user data from intent
        cartid = intent.getStringExtra("sendcartID") ?: ""
        userId = intent.getStringExtra("senduser") ?: ""
        name = intent.getStringExtra("sendname") ?: ""
        email = intent.getStringExtra("sendemail") ?: ""
        username = intent.getStringExtra("sendusername") ?: ""
        password = intent.getStringExtra("sendpassword") ?: ""

        // Retrieve food data from intent
        foodName = intent.getStringExtra("foodName") ?: ""
        foodPrice = intent.getDoubleExtra("foodPrice", 0.0)
        foodImage = intent.getIntExtra("foodImage", R.drawable.pizza1)

        Log.d("CartActivity", "cartId: $cartid")
        Log.d("CartActivity", "userId: $userId")
        Log.d("CartActivity", "name: $name")
        Log.d("CartActivity", "email: $email")
        Log.d("CartActivity", "username: $username")
        Log.d("CartActivity", "password: $password")
        Log.d("CartActivity", "Food Name: $foodName")
        Log.d("CartActivity", "Food Price: $foodPrice")
        Log.d("CartActivity", "Food Image: $foodImage")

        totalView = findViewById(R.id.total_view)

        // Initialize RecyclerView and Adapter
        val database = FirebaseDatabase.getInstance()
        cartRecyclerView = findViewById(R.id.cart_rec)
        cartAdapter = CartAdapter(mutableListOf(), this, totalView, username, database)
        getCartItemList()

        // Set LayoutManager for RecyclerView (e.g., LinearLayoutManager or GridLayoutManager)
        val layoutManager = LinearLayoutManager(this)
        cartRecyclerView.layoutManager = layoutManager

        // Set the adapter to the RecyclerView
        cartRecyclerView.adapter = cartAdapter

        orderButton = findViewById(R.id.order_button)
        orderButton.setOnClickListener {
            makeOrder()
        }

        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            // Create an intent to send data back to the FoodListActivity
            val returnIntent = Intent(this, FoodListActivity::class.java).apply {
                putExtra("foodImage", foodImage)
                putExtra("foodName", foodName)
                putExtra("foodPrice", foodPrice)
                putExtra("senduser", userId)
                putExtra("sendname", name)
                putExtra("sendemail", email)
                putExtra("sendusername", username)
                putExtra("sendpassword", password)
                // Add any other necessary data
            }
            startActivity(returnIntent)
            finish() // Return to FoodListActivity
        }

    }

    private fun makeOrder() {
        Log.d("CartActivity", "makeOrder function called")

        // Prepare data to send to OrderPlacedActivity
        val totalQuantity = calculateTotalQuantity()
        val totalPrice = calculateTotalPrice()

        // You can get foodName from the first item in the cartItemList if available
        val foodName = cartAdapter.cartItemList.firstOrNull()?.productName ?: ""
        val quantity = cartAdapter.cartItemList.firstOrNull()?.quantity?.toString() ?:""
        // Get foodName2 from the second item in the cartItemList if available
        val foodName2 = cartAdapter.cartItemList.getOrNull(1)?.productName ?: ""
        val quantity2 = cartAdapter.cartItemList.getOrNull(1)?.quantity?.toString() ?:""

        val foodName3 = cartAdapter.cartItemList.getOrNull(2)?.productName ?: ""
        val quantity3 = cartAdapter.cartItemList.getOrNull(2)?.quantity?.toString() ?:""

        val currentTimeInUTCString = getCurrentTimeInTimeZone("UTC", "yyyy-MM-dd")

        tableNum = findViewById(R.id.table_num)
        val tablenumber = tableNum.text.toString()

        // Assuming you have received the necessary user data
        val userId = intent.getStringExtra("senduser")
        val name = intent.getStringExtra("sendname")
        val email = intent.getStringExtra("sendemail")
        val username = intent.getStringExtra("sendusername")
        val password = intent.getStringExtra("sendpassword")

        Log.d("SendOrder", "foodName 2: $foodName2")
        // Create an Order object
        val order = Order(
            orderId = generateOrderId(),
            totalQuantity = totalQuantity,
            totalPriceRM = totalPrice,
            foodName = foodName,
            foodName2 = foodName2,
            foodName3 = foodName3,
            quantity = quantity,
            quantity2 = quantity2,
            quantity3 = quantity3,
            username = username,
            time = currentTimeInUTCString,
            table = tablenumber
        )

        // Create an Intent to start OrderPlacedActivity
        val intent = Intent(this, OrderPlacedActivity::class.java).apply {
            Log.d("CartActivity", "userId: $userId")
            Log.d("CartActivity", "name: $name")
            Log.d("CartActivity", "email: $email")
            Log.d("CartActivity", "username: $username")
            Log.d("CartActivity", "password: $password")
            Log.d("CartActivity", "totalQuantity: $totalQuantity")
            Log.d("CartActivity", "totalPrice: $totalPrice")
            Log.d("CartActivity", "foodName: $foodName")
            Log.d("CartActivity", "foodName2: $foodName2")
            Log.d("CartActivity", "foodName3: $foodName3")
            Log.d("CartActivity", "quantity: $quantity")
            Log.d("CartActivity", "quantity2: $quantity2")
            Log.d("CartActivity", "quantity3: $quantity3")
            Log.d("CartActivity", "Order_id: $order.orderId")
            Log.d("CartActivity", "Time: $currentTimeInUTCString")
            Log.d("CartActivity", "Time: $tablenumber")
            putExtra("senduser", userId)
            putExtra("sendname", name)
            putExtra("sendemail", email)
            putExtra("sendusername", username)
            putExtra("sendpassword", password)
            putExtra("totalQuantity", totalQuantity)
            putExtra("totalPrice_RM", totalPrice)
            putExtra("foodName", foodName)
            putExtra("foodName2", foodName2)
            putExtra("foodName3", foodName3)
            putExtra("quantity", quantity)
            putExtra("quantity2", quantity2)
            putExtra("quantity3", quantity3)
            putExtra("OrderID", order.orderId)
            putExtra("Time", currentTimeInUTCString)
            putExtra("Table", tablenumber)
            // Add more extras if needed
        }

        // Start the OrderPlacedActivity
        startActivity(intent)

        storeOrderDetails(order)
    }

    fun getCurrentTimeInTimeZone(timeZoneId: String, format: String): String {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZoneId))
        val sdf = SimpleDateFormat(format)
        sdf.timeZone = TimeZone.getTimeZone(timeZoneId)
        return sdf.format(calendar.time)
    }

    // Function to store order details in Firebase Realtime Database under user.child.username.child("orders")
    private fun storeOrderDetails(order: Order) {
        val database = FirebaseDatabase.getInstance()
        val ordersRef = database.getReference("user")

        // Assuming the user ID is non-null
        order.username?.let { username ->
            val userOrdersRef = ordersRef.child(username).child("orders")
            order.orderId?.let {
                userOrdersRef.child(it).setValue(order)
                    .addOnSuccessListener {
                        Log.d("CartActivity", "Order details stored successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.e("CartActivity", "Error storing order details: ${e.message}")
                    }
            }
        }
    }


    // Function to generate a unique order ID
    private fun generateOrderId(): String {
        val dateFormat = SimpleDateFormat("ssSSS", Locale.getDefault())
        return "ORDER-${dateFormat.format(Date())}"
    }

    private fun calculateTotalQuantity(): Int {
        // Calculate total quantity from your cartItemList
        return cartAdapter.cartItemList.sumBy { it.quantity }
    }

    private fun calculateTotalPrice(): Double {
        // Calculate total price from your cartItemList
        return cartAdapter.cartItemList.sumByDouble { it.productPrice * it.quantity }
    }

    private fun getCartItemList() {
        val database = FirebaseDatabase.getInstance()
         username = intent.getStringExtra("sendusername") ?: ""

        // Reference to the user's cart in the database
        val userCartRef = database.getReference("user").child(username).child("cart")

        userCartRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val cartItemList = mutableListOf<CartItem>()

                for (cartSnapshot in dataSnapshot.children) {
                    val cartId = cartSnapshot.child("cartItemId").getValue(String::class.java) ?: ""
                    val foodImage = cartSnapshot.child("foodImage").getValue(Int::class.java) ?: R.drawable.drink2
                    val foodName = cartSnapshot.child("foodName").getValue(String::class.java) ?: ""
                    val foodName2 = cartSnapshot.child("foodName2").getValue(String::class.java) ?: ""
                    val foodName3 = cartSnapshot.child("foodName3").getValue(String::class.java) ?: ""
                    val quantity = cartSnapshot.child("quantity").getValue(String::class.java)?.toInt() ?:0
                    val quantity2 = cartSnapshot.child("quantity2").getValue(String::class.java)?.toInt() ?:0
                    val quantity3 = cartSnapshot.child("quantity3").getValue(String::class.java)?.toInt() ?:0
                    val foodPrice = cartSnapshot.child("foodPrice").getValue(String::class.java)?.toDouble() ?: 0.0

                    Log.d("CartActivity", "Food Image: $foodImage")
                    Log.d("CartActivity", "CartID: $cartId")

                    val currentTimeInUTCString = getCurrentTimeInTimeZone("UTC", "yyyy-MM-dd")

                    val cartItem = CartItem(cartId, foodImage, foodName, foodName2, foodName3, quantity,quantity2,quantity3, foodPrice, currentTimeInUTCString) // Assuming default quantity is 1
                    cartItemList.add(cartItem)
                }
                // Update RecyclerView with the fetched cart items
                updateRecyclerView(cartItemList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Log.e("CartActivity", "Failed to read cart items: ${databaseError.message}")
            }
        })
    }

    private fun updateRecyclerView(cartItemList: List<CartItem>) {
        // Update RecyclerView with the fetched cart items
        cartAdapter.updateCartItemList(cartItemList)
    }

}
