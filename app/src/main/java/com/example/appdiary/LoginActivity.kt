package com.example.appdiary

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var sharedPreferences: SharedPreferences = this.getSharedPreferences("SharePreferences", Context.MODE_PRIVATE)
        if (!sharedPreferences.getBoolean("havePassword", false)) {
            login()
        }
        btnLogin.setOnClickListener {

            var spPassword = sharedPreferences.getString("spPassword", "123456")
            var pass = login_etPassword.text.toString()
            if (pass == spPassword) {
                login()
            } else {
                Toast.makeText(this, "Incorrect password", Toast.LENGTH_LONG).show()
            }

        }
        login_forgotPass.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putString("spPassword","123456")
            editor.apply()
            Toast.makeText(this, "New password is '123456'", Toast.LENGTH_LONG).show()
        }
    }

    fun login() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}