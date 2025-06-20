package com.example.sukamakan

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

class OrderPlacedActivity : AppCompatActivity() {

    private lateinit var btnOkay: Button
    private lateinit var orderID: TextView
    private lateinit var QR : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_placed)

        orderID = findViewById(R.id.order_id)
        val orderId = intent.getStringExtra("OrderID") ?: ""
        orderID.text = orderId

        QR = findViewById(R.id.QRCode)
        val data = orderId
        Log.d("OrderPlacedActivity", "Data: $data")

        // Check if data is null before proceeding
        if (data != null) {
            if (data.isEmpty()) {
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
            } else {
                val writer = QRCodeWriter()
                try {
                    val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
                    val width = bitMatrix.width
                    val height = bitMatrix.height
                    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                    for (x in 0 until width) {
                        for (y in 0 until height) {
                            bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                        }
                    }
                    QR.setImageBitmap(bmp)
                } catch (e: WriterException) {
                    e.printStackTrace()
                }
            }
        } else {
            Toast.makeText(this, "ERROR: Data is null", Toast.LENGTH_SHORT).show()
        }

        btnOkay = findViewById(R.id.btnOk)
        orderID = findViewById(R.id.order_id)

        // Set click listener for the button
        btnOkay.setOnClickListener {
            sendToOrderHistory()
        }
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
        val foodName2 = intent.getStringExtra("foodName2") ?: ""
        val foodName3 = intent.getStringExtra("foodName3") ?: ""
        val quantity = intent.getStringExtra("quantity") ?: ""
        val quantity2 = intent.getStringExtra("quantity2") ?: ""
        val quantity3 = intent.getStringExtra("quantity3") ?: ""
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
            putExtra("foodName2", foodName2)
            putExtra("foodName3", foodName3)
            putExtra("quantity", quantity)
            putExtra("quantity2", quantity2)
            putExtra("quantity3", quantity3)
            putExtra("OrderID", orderId)
            // Add more extras if needed
        }

        // Start the OrderHistoryActivity
        startActivity(intent)
    }
}
