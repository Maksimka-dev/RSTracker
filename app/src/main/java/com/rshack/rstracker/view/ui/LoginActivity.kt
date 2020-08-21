package com.rshack.rstracker.view.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.rshack.rstracker.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()

        btn_login.setOnClickListener {
            val email: String = tv_email.text.toString()
            val password: String = tv_password.text.toString()
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(
                    applicationContext,
                    "Please fill in the required fields",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "firebase auth success")
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()
                    } else {
                        Log.d(TAG, "firebase auth failed")
                        Toast.makeText(
                            applicationContext,
                            "Wrong email or password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        btn_register.setOnClickListener {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
            finish()
        }
    }
}