package com.example.sukamakan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UpdateActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var regName : EditText
    private lateinit var regUsername : EditText
    private lateinit var regEmail : EditText
    private lateinit var regPassword : EditText
    private lateinit var deletebutton : Button
    private lateinit var updatebutton : Button
    private lateinit var backButton : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        regName = findViewById(R.id.reg_name)
        regEmail = findViewById(R.id.reg_email)
        regUsername = findViewById(R.id.reg_username)
        regPassword = findViewById(R.id.reg_password)
        updatebutton = findViewById(R.id.update_button)
        deletebutton = findViewById(R.id.delete_button)
        backButton = findViewById(R.id.back_button)

        val userId = intent.getStringExtra("senduser")
        val name = intent.getStringExtra("sendname")
        val email = intent.getStringExtra("sendemail")
        val username = intent.getStringExtra("sendusername")
        val password = intent.getStringExtra("sendpassword")

        regEmail.setText(email)
        regUsername.setText(username)
        regName.setText(name)
        regPassword.setText(password)

        updatebutton.setOnClickListener {
            updateData(
                regEmail.text.toString(),
                regName.text.toString(),
                regUsername.text.toString(),
                regPassword.text.toString()
            )
        }

        backButton.setOnClickListener() {
            Log.d("UpdateActivity", "Button clicked") // Add this line

            val userId = intent.getStringExtra("senduser")
            val name = intent.getStringExtra("sendname")
            val email = intent.getStringExtra("sendemail")
            val username = intent.getStringExtra("sendusername")
            val password = intent.getStringExtra("sendpassword")

            Log.d("UpdateActivity", "userId: $userId, name: $name, email: $email, username: $username, password: $password") // Add this line

            // Proceed with the intent if needed
            val intent = Intent(this@UpdateActivity, FoodListActivity::class.java).apply {
                putExtra("senduser", userId)
                putExtra("sendname", name)
                putExtra("sendemail", email)
                putExtra("sendusername", username)
                putExtra("sendpassword", password)
            }

            startActivity(intent)
        }

        deletebutton.setOnClickListener() {
                // Assuming you have the username stored in a variable called 'usernameToDelete'
            val usernameToDelete = regUsername.text.toString()

            deleteData(usernameToDelete)

        }
    }

    private fun updateData(e: String, n: String, u: String, p: String) {
        dbRef = FirebaseDatabase.getInstance().getReference("user")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(u)) {

                    val userRef = dbRef.child(u)
                    val userId = snapshot.child(u).child("userId").getValue(String::class.java)

                    // Set the new user data with the updated username
                    val updatedUserInfo = UserData(userId, e, n, u, p)
                    userRef.setValue(updatedUserInfo)

                    // Show the Toast message after successful update
                    Toast.makeText(this@UpdateActivity, "Data Updated Successfully", Toast.LENGTH_LONG).show()

                    // Proceed with the intent if needed
                    val intent = Intent(this@UpdateActivity, FoodListActivity::class.java)
                    intent.putExtra("senduser", userId)
                    intent.putExtra("sendname", n)
                    intent.putExtra("sendemail", e)
                    intent.putExtra("sendusername", u)
                    intent.putExtra("sendpassword", p)

                    finish()
                    startActivity(intent)
                } else {
                    // Show the Toast message after successful update
                    Toast.makeText(this@UpdateActivity, "Error Occurred", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database errors if needed
                Toast.makeText(this@UpdateActivity, "Error Occurred", Toast.LENGTH_LONG).show()
            }
        })
    }


    private fun deleteData(ud: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("user").child(ud)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "User data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, LoginActivity::class.java)
            finish()
            startActivity(intent)

        }.addOnFailureListener { error ->
            Toast.makeText(this, "Deleting Error ${error.message}", Toast.LENGTH_LONG).show()
        }
    }
}