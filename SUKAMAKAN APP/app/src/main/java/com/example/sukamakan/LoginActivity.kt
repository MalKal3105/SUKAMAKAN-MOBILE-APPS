package com.example.sukamakan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var Name: EditText
    private lateinit var Password: EditText
    private lateinit var loginbutton: Button
    private lateinit var goSignUp: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Name = findViewById(R.id.log_name)
        Password = findViewById(R.id.log_password)
        loginbutton = findViewById(R.id.log_button)
        goSignUp = findViewById(R.id.log_login)

        loginbutton.setOnClickListener() {
            checkUser()
        }
        goSignUp.setOnClickListener() {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkUser() {
        val usernametxt = Name.text.toString().trim()
        val passwordtxt = Password.text.toString()

        dbRef = FirebaseDatabase.getInstance().getReference("user")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(usernametxt)) {
                    val userSnapshot = snapshot.child(usernametxt)

                    // Add null checks
                    val name = userSnapshot.child("name").getValue(String::class.java)
                    val getpassword = userSnapshot.child("password").getValue(String::class.java)
                    val userId = userSnapshot.child("userId").getValue(String::class.java)
                    val email = userSnapshot.child("email").getValue(String::class.java)

                    // Add null checks
                    if (getpassword != null && getpassword == passwordtxt) {
                        Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_LONG).show()

                        //Intent to a different activity
                        val intent = Intent(this@LoginActivity, FoodListActivity::class.java)
                        intent.putExtra("senduser", userId)
                        intent.putExtra("sendname", name)
                        intent.putExtra("sendemail", email)
                        intent.putExtra("sendusername", usernametxt)
                        intent.putExtra("sendpassword", getpassword)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid Login", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Wrong Login", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database errors if needed
                Toast.makeText(this@LoginActivity, "Error Occurred", Toast.LENGTH_LONG).show()
            }
        })
    }

}
