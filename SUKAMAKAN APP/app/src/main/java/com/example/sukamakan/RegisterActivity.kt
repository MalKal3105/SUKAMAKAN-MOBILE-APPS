package com.example.sukamakan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var regName : EditText
    private lateinit var regUsername : EditText
    private lateinit var regEmail : EditText
    private lateinit var regPassword : EditText
    private lateinit var signupbutton : Button
    private lateinit var loginbutton : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        regName = findViewById(R.id.reg_name)
        regEmail = findViewById(R.id.reg_email)
        regUsername = findViewById(R.id.reg_username)
        regPassword = findViewById(R.id.reg_password)
        signupbutton = findViewById(R.id.reg_button)
        loginbutton = findViewById(R.id.reg_login)

        signupbutton.setOnClickListener() {
            saveUserData(
                regEmail.text.toString(),
                regName.text.toString() ,
                regUsername.text.toString(),
                regPassword.text.toString()
            )
        }

        loginbutton.setOnClickListener() {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveUserData(e:String, n:String, u:String, p:String){
        //   write data to database
        dbRef = FirebaseDatabase.getInstance().getReference("user")

        val userId = dbRef.push().key!!     //variable for autogenerate ID
        val ud = UserData(userId,e,n,u,p)

        dbRef.child(u).setValue(ud)  //each new id .. have collection of child
            .addOnCompleteListener {
                Toast.makeText(this, "User Successfully Registered", Toast.LENGTH_LONG).show()
            }.addOnFailureListener{
                Toast.makeText(this, "User Fail to be Register", Toast.LENGTH_LONG).show()
            }
    }
}