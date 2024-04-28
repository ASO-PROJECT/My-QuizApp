package com.example.myquiizapp

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.health.connect.datatypes.ExerciseRoute
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class SignIn : AppCompatActivity() {
    private lateinit var editTextEmail: EditText
    private lateinit var textViewSignUp:TextView
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSignIn: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonSignIn = findViewById(R.id.buttonSignIn)
        textViewSignUp = findViewById(R.id.textViewSignUp)

        textViewSignUp.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
            finish()

        }

        buttonSignIn.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signInUser(email, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    Toast.makeText(this, "Sign in successful", Toast.LENGTH_SHORT).show()


                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    val errorMessage = (task.exception as? FirebaseAuthException)?.message
                        ?: "Sign in failed. Please try again later."
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }
}