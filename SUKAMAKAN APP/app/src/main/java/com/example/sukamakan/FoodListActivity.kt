package com.example.sukamakan

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class FoodListActivity : AppCompatActivity() {

    private lateinit var horRecyclerView: RecyclerView
    private lateinit var verRecyclerView: RecyclerView
    private lateinit var horFoodList: ArrayList<Food>
    private lateinit var verFoodList: ArrayList<FoodVer>
    private lateinit var horAdapter: FoodAdapter
    private lateinit var verAdapter: FoodVerAdapter
    private lateinit var editText: EditText
    private lateinit var menuCart: ImageView
//    private lateinit var foodName: String
//    private var foodPrice: Double = 0.0
//    private var foodImage: Int = R.drawable.pizza1
    private lateinit var history: MenuItem

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)

        init()

        // Assuming you have received the data in this activity
        val userId = intent.getStringExtra("senduser")
        val name = intent.getStringExtra("sendname")
        val email = intent.getStringExtra("sendemail")
        val username = intent.getStringExtra("sendusername")
        val password = intent.getStringExtra("sendpassword")



        // Set the data in the NavigationView header
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0) // 0 represents the first header (if multiple)

        // Find TextViews in the header layout
        val userNameHeader: TextView = headerView.findViewById(R.id.prof_name)
        val emailHeader: TextView = headerView.findViewById(R.id.prof_email)

        val prof: MenuItem = navigationView.menu.findItem(R.id.nav_prof)
        val cart: MenuItem = navigationView.menu.findItem(R.id.nav_cart)
        val logout: MenuItem = navigationView.menu.findItem(R.id.nav_logout)

        // Set the click listener for "Add to Cart" button
        verAdapter = FoodVerAdapter(verFoodList, this@FoodListActivity) // Pass the listener to the adapter
        verRecyclerView.adapter = verAdapter

        history = navigationView.menu.findItem(R.id.nav_order_history)
        history.setOnMenuItemClickListener {
            sendToOrderHistory()
            true
        }

        menuCart = findViewById(R.id.cart_button)
        menuCart.setOnClickListener() {
            openCart()
            true
        }

        cart.setOnMenuItemClickListener {
            // Assuming you have received the necessary data
            val userId = intent.getStringExtra("senduser")
            val name = intent.getStringExtra("sendname")
            val email = intent.getStringExtra("sendemail")
            val username = intent.getStringExtra("sendusername")
            val password = intent.getStringExtra("sendpassword")

            // Create an intent to start the CartActivity
            val intent = Intent(this, CartActivity::class.java).apply {
                putExtra("senduser", userId)
                putExtra("sendname", name)
                putExtra("sendemail", email)
                putExtra("sendusername", username)
                putExtra("sendpassword", password)
            }

            // Add more data as needed

            // Start the CartActivity
            startActivity(intent)
            true
        }

        prof.setOnMenuItemClickListener {
            // Handle the click event
            // Assuming you have received the necessary data
            val userId = intent.getStringExtra("senduser")
            val name = intent.getStringExtra("sendname")
            val email = intent.getStringExtra("sendemail")
            val username = intent.getStringExtra("sendusername")
            val password = intent.getStringExtra("sendpassword")

            val intent = Intent(this@FoodListActivity, UpdateActivity::class.java).apply {
                putExtra("senduser", userId)
                putExtra("sendname", name)
                putExtra("sendemail", email)
                putExtra("sendusername", username)
                putExtra("sendpassword", password)
            }
            startActivity(intent)
            // Return true to consume the menu item click event
            true
        }

        logout.setOnMenuItemClickListener {
            val intent = Intent(this@FoodListActivity, LoginActivity::class.java)
            startActivity(intent)

            true
        }

        // Set the data to the TextViews
        userNameHeader.text = username
        emailHeader.text = email

        // Log the retrieved values
        Log.d("FoodListActivity", "User ID: $userId")
        Log.d("FoodListActivity", "Name: $name")
        Log.d("FoodListActivity", "Email: $email")
        Log.d("FoodListActivity", "Username: $username")
        Log.d("FoodListActivity", "Password: $password")
    }

    private fun openCart() {
        // Assuming you have received the necessary data
        val userId = intent.getStringExtra("senduser")
        val name = intent.getStringExtra("sendname")
        val email = intent.getStringExtra("sendemail")
        val username = intent.getStringExtra("sendusername")
        val password = intent.getStringExtra("sendpassword")

        // Create an intent to start the CartActivity
        val intent = Intent(this, CartActivity::class.java).apply {
            putExtra("senduser", userId)
            putExtra("sendname", name)
            putExtra("sendemail", email)
            putExtra("sendusername", username)
            putExtra("sendpassword", password)
        }

        // Add more data as needed

        // Start the CartActivity
        startActivity(intent)
        finish()
    }

    // Function to send data to OrderHistoryActivity
    private fun sendToOrderHistory() {
        // Retrieve the data from intent
        val userId = intent.getStringExtra("senduser") ?: ""
        val name = intent.getStringExtra("sendname") ?: ""
        val email = intent.getStringExtra("sendemail") ?: ""
        val username = intent.getStringExtra("sendusername") ?: ""
        val password = intent.getStringExtra("sendpassword") ?: ""
        val totalQuantity = intent.getIntExtra("totalQuantity", 0)
        val totalPrice = intent.getDoubleExtra("totalPrice_RM", 0.0)
        val foodName = intent.getStringExtra("foodName") ?: ""
        val orderId = intent.getStringExtra("OrderID") ?: ""

        // Create an Intent to start OrderHistoryActivity
        val intent = Intent(this, OrderHistoryActivity::class.java).apply {
            putExtra("senduser", userId)
            putExtra("sendname", name)
            putExtra("sendemail", email)
            putExtra("sendusername", username)
            putExtra("sendpassword", password)
            putExtra("totalQuantity", totalQuantity)
            putExtra("totalPrice_RM", totalPrice)
            putExtra("foodName", foodName)
            putExtra("OrderID", orderId)
            // Add more extras if needed
        }

        // Start the OrderHistoryActivity
        startActivity(intent)
    }

    // Implement the interface method
    fun onAddToCartClick(position: Int) {
        // Ensure that the position is valid
        if (position >= 0 && position < verFoodList.size) {
            // Get the selected food item from verFoodList using the position
            val selectedFood = verFoodList[position]

            // Create an intent to send back the selected food item to CartActivity
            val resultIntent = Intent()
            resultIntent.putExtra("newCartItem", selectedFood)
            setResult(Activity.RESULT_OK, resultIntent)

            // Assuming you have received the necessary data
            val userId = intent.getStringExtra("senduser")
            val name = intent.getStringExtra("sendname")
            val email = intent.getStringExtra("sendemail")
            val username = intent.getStringExtra("sendusername")
            val password = intent.getStringExtra("sendpassword")

            // Get a reference to the database
            val database = FirebaseDatabase.getInstance()

            val userCartRef = username?.let { database.getReference("user").child(it).child("cart") }

            // Generate your own unique ID for the new cart item
            val cartItemId = generateUniqueID() // Implement this function to generate your own ID

            // Ensure cartItemId is not null
            if (cartItemId != null) {
                // Add cartItemId to the selected food item
                selectedFood.cartItemId = cartItemId

                // Add the selected food item to the database under the generated ID
                userCartRef?.child(cartItemId)?.setValue(selectedFood)
                    ?.addOnSuccessListener {
                        Log.d("FoodListActivity", "Cart item added successfully")
                    }?.addOnFailureListener { e ->
                        Log.e("FoodListActivity", "Error adding cart item: ${e.message}")
                    }
            } else {
                Log.e("FoodListActivity", "Failed to generate cart item ID")
            }

            // Create an intent to start the CartActivity
            val intent = Intent(this, CartActivity::class.java).apply {

                putExtra("sendcartID", cartItemId)
                putExtra("senduser", userId)
                putExtra("sendname", name)
                putExtra("sendemail", email)
                putExtra("sendusername", username)
                putExtra("sendpassword", password)
            }

            // Pass necessary data to the CartActivity using intent extras
            intent.putExtra("foodImage", selectedFood.foodImage)
            intent.putExtra("foodName", selectedFood.foodName)
            intent.putExtra("foodPrice", selectedFood.foodPrice)

            // Add more data as needed

            // Start the CartActivity
            startActivity(intent)
            finish()
        } else {
            // Handle an invalid position (optional)
            // You may want to log an error, show a message, or take appropriate action
            Log.e("FoodListActivity", "Invalid position: $position")
        }
    }

    // Function to generate a unique order ID
    private fun generateUniqueID(): String {
        val dateFormat = SimpleDateFormat("mmMMssSSS", Locale.getDefault())
        return "CART-${dateFormat.format(Date())}"
    }

    private fun init(){

        val profileButton: ImageView = findViewById(R.id.profile_button)

        profileButton.setOnClickListener {
            val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
            drawerLayout.openDrawer(GravityCompat.START)
        }

        horRecyclerView = findViewById(R.id.food_hor_rec)
        horRecyclerView.setHasFixedSize(true)
        horRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL , false)
        val snapHelper : SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(horRecyclerView)


        verRecyclerView = findViewById(R.id.food_ver_rec)
        verRecyclerView.setHasFixedSize(true)
        verRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL , false)
        val snapHelper2 : SnapHelper = LinearSnapHelper()
        snapHelper2.attachToRecyclerView(verRecyclerView)

        horFoodList = ArrayList()
        verFoodList = ArrayList()

        addDataToHorList()
        addDataToVerList()

        horAdapter = FoodAdapter(horFoodList)
        horRecyclerView.adapter = horAdapter

        verAdapter = FoodVerAdapter(verFoodList, this@FoodListActivity)
        verRecyclerView.adapter = verAdapter

        horAdapter.setOnItemClickListener { position ->
            val selectedFood = horFoodList[position]

            if (selectedFood.foodName.equals("Pizza", ignoreCase = true)) {
                // Clear existing items in verFoodList
                verFoodList.clear()

                // Call the function to add drink data to verFoodList
                pizzaDataToVerList()

                // Notify the adapter that the data set has changed
                verAdapter.setList(verFoodList)
            }

            if (selectedFood.foodName.equals("Desert", ignoreCase = true)) {
                // Clear existing items in verFoodList
                verFoodList.clear()

                // Call the function to add drink data to verFoodList
                desertDataToVerList()

                // Notify the adapter that the data set has changed
                verAdapter.setList(verFoodList)
            }

            if (selectedFood.foodName.equals("Drink", ignoreCase = true)) {

                // Clear existing items in verFoodList
                verFoodList.clear()

                // Call the function to add drink data to verFoodList
                drinkDataToVerList()

                // Notify the adapter that the data set has changed
                verAdapter.setList(verFoodList)
            }

            if (selectedFood.foodName.equals("Burger", ignoreCase = true)) {
                // Clear existing items in verFoodList
                verFoodList.clear()

                // Call the function to add drink data to verFoodList
                burgerDataToVerList()

                // Notify the adapter that the data set has changed
                verAdapter.setList(verFoodList)
            }

            if (selectedFood.foodName.equals("Ice Cream", ignoreCase = true)) {
                // Clear existing items in verFoodList
                verFoodList.clear()

                // Call the function to add drink data to verFoodList
                icecreamDataToVerList()

                // Notify the adapter that the data set has changed
                verAdapter.setList(verFoodList)
            }
        }

        // Inside the init() method
        editText = findViewById(R.id.editText)
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed for this example
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                val searchQuery = charSequence.toString()

//                // Always reset the list
//                verAdapter.resetList()

                // If the search query is not empty, filter the data
                if (searchQuery.isNotEmpty()) {
                    verAdapter.resetList()
                    verAdapter.filter(searchQuery)
                } else {
                    // If the search query is empty, add default data
                    addDataToVerList()
                }
            }


            override fun afterTextChanged(editable: Editable?) {
                // Not needed for this example
            }
        })
    }

    private fun addDataToHorList() {
        horFoodList.add(Food(R.drawable.pizza, "Pizza"))
        horFoodList.add(Food(R.drawable.cakeicon, "Desert"))
        horFoodList.add(Food(R.drawable.drinkicon, "Drink"))
        horFoodList.add(Food(R.drawable.burgericon, "Burger"))
        horFoodList.add(Food(R.drawable.icecreamicon, "Ice Cream"))
    }

    private fun addDataToVerList() {
        verFoodList.add(FoodVer(R.drawable.pizza1, "Spicy Pizza Kaw", "10", R.drawable.rating3, "3.0", "30Min"))
        verFoodList.add(FoodVer(R.drawable.pizza2, "Meletop Pizza", "15", R.drawable.rating4, "4.0", "30Min"))
        verFoodList.add(FoodVer(R.drawable.pizza3, "Pizza Ranggi", "20", R.drawable.rating5, "5.0", "30Min"))

        verFoodList.add(FoodVer(R.drawable.drink1, "Ice Lemon Drink", "5", R.drawable.rating3, "2.0", "5Min"))
        verFoodList.add(FoodVer(R.drawable.drink2, "Green Tea Drink", "7", R.drawable.rating4, "5.0", "5Min"))
        verFoodList.add(FoodVer(R.drawable.drink3, "Mango Drink Kaw", "9", R.drawable.rating5, "3.0", "5Min"))

        verFoodList.add(FoodVer(R.drawable.burger1, "Burger Kebabom", "12", R.drawable.rating3, "3.0", "15Min"))
        verFoodList.add(FoodVer(R.drawable.burger2, "Double Onion Burger", "14", R.drawable.rating4, "4.0", "15Min"))
        verFoodList.add(FoodVer(R.drawable.burger3, "Burger Kembor", "16", R.drawable.rating5, "5.0", "15Min"))

        verFoodList.add(FoodVer(R.drawable.icecream1, "Chocolate Ice Cream", "2", R.drawable.rating5, "5.0", "2Min"))
        verFoodList.add(FoodVer(R.drawable.icecream2, "Vanilla Ice Cream", "4", R.drawable.rating4, "4.0", "2Min"))
        verFoodList.add(FoodVer(R.drawable.icecream3, "Strawberry Ice Cream", "66", R.drawable.rating3, "3.0", "2Min"))

        verFoodList.add(FoodVer(R.drawable.desert1, "Red Velvet Desert", "7", R.drawable.rating5, "5.0", "10Min"))
        verFoodList.add(FoodVer(R.drawable.desert2, "Rahsia SUKAMAKAN Desert", "9", R.drawable.rating4, "4.0", "10Min"))
        verFoodList.add(FoodVer(R.drawable.desert3, "Desert Sempoi", "12", R.drawable.rating4, "4.0", "10Min"))
    }

    private fun drinkDataToVerList() {
        verFoodList.add(FoodVer(R.drawable.drink1, "Ice Lemon Drink", "5", R.drawable.rating3, "2.0", "5Min"))
        verFoodList.add(FoodVer(R.drawable.drink2, "Green Tea Drink", "7", R.drawable.rating4, "5.0", "5Min"))
        verFoodList.add(FoodVer(R.drawable.drink3, "Mango Drink Kaw", "9", R.drawable.rating5, "3.0", "5Min"))
    }

    private fun burgerDataToVerList() {
        verFoodList.add(FoodVer(R.drawable.burger1, "Burger Kebabom", "12", R.drawable.rating3, "3.0", "15Min"))
        verFoodList.add(FoodVer(R.drawable.burger2, "Double Onion Burger", "14", R.drawable.rating4, "4.0", "15Min"))
        verFoodList.add(FoodVer(R.drawable.burger3, "Burger Kembor", "16", R.drawable.rating5, "5.0", "15Min"))
    }

    private fun desertDataToVerList() {
        verFoodList.add(FoodVer(R.drawable.desert1, "Red Velvet Desert", "7", R.drawable.rating5, "5.0", "10Min"))
        verFoodList.add(FoodVer(R.drawable.desert2, "Rahsia SUKAMAKAN Desert", "9", R.drawable.rating4, "4.0", "10Min"))
        verFoodList.add(FoodVer(R.drawable.desert3, "Desert Sempoi", "12", R.drawable.rating4, "4.0", "10Min"))
    }

    private fun pizzaDataToVerList() {
        verFoodList.add(FoodVer(R.drawable.pizza1, "Spicy Pizza Kaw", "10", R.drawable.rating3, "3.0", "30Min"))
        verFoodList.add(FoodVer(R.drawable.pizza2, "Meletop Pizza", "15", R.drawable.rating4, "4.0", "30Min"))
        verFoodList.add(FoodVer(R.drawable.pizza3, "Pizza Ranggi", "20", R.drawable.rating5, "5.0", "30Min"))
    }

    private fun icecreamDataToVerList() {
        verFoodList.add(FoodVer(R.drawable.icecream1, "Chocolate Ice Cream", "2", R.drawable.rating5, "5.0", "2Min"))
        verFoodList.add(FoodVer(R.drawable.icecream2, "Vanilla Ice Cream", "4", R.drawable.rating4, "4.0", "2Min"))
        verFoodList.add(FoodVer(R.drawable.icecream3, "Strawberry Ice Cream", "66", R.drawable.rating3, "3.0", "2Min"))
    }
}

