package com.kyrg.joffreymann.kyrgsupport

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Button
import android.content.Intent
import kotlinx.android.synthetic.main.activity_login.*
import android.content.SharedPreferences
import android.content.Context
import com.google.firebase.database.DatabaseReference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import android.support.annotation.NonNull
import android.widget.EditText
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import android.app.Activity
import android.view.inputmethod.InputMethodManager

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view: View = if (currentFocus == null) View(this) else currentFocus
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        val editor = this.getSharedPreferences("", 0).edit()
        setContentView(R.layout.activity_login)
        // get reference to button
        val registerButton = findViewById(R.id.register) as Button
        // set on-click listener
        registerButton.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
        val loginButton = findViewById(R.id.login) as Button
        loginButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.emailEditText).text.toString()
            val password = findViewById<EditText>(R.id.passwordEditText).text.toString()
            loginWithEmailAndPassword(email, password)
            hideKeyboard()
        }
    }

    fun hideKeyboard() {
        try {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
            // TODO: handle exception
        }

    }

    public fun currentUser():KYRGAdmin {
        val prefs = getSharedPreferences("adminInfo", Context.MODE_PRIVATE)
        val restoredText = prefs.getString("text", null)
        if (restoredText != null) {
            val localEmail = prefs.getString("email", "")
            val localPassword = prefs.getString("password", "")
            val localPhone = prefs.getString("phone", "")
            val localFirstName = prefs.getString("firstName", "")
            val localLastName = prefs.getString("lastName", "")
            return KYRGAdmin(localEmail, localPassword, localPhone, localFirstName, localLastName)
        }
        else {
            return KYRGAdmin("", "", "", "", "")
        }
    }

    private fun loginWithEmailAndPassword(email: String, password: String) {
        var auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,  OnCompleteListener<AuthResult> {
                    if (it.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.getCurrentUser()
                        Toast.makeText(this, "You have successfully logged in.",
                                Toast.LENGTH_SHORT).show()
                        val emailField = findViewById<EditText>(R.id.emailEditText)
                        val passwordField = findViewById<EditText>(R.id.passwordEditText)
                        emailField.text = null
                        passwordField.text = null
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    }
                })
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }

    class KYRGAdmin {
        var email: String? = null
        var password: String? = null
        var phone: String? = null
        var firstName: String? = null
        var lastName: String? = null

        constructor() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        constructor(email: String?, password: String?, phone: String?, firstName: String?, lastName: String?) {
            this.email = email
            this.password = password
            this.phone = phone
            this.firstName = firstName
            this.lastName = lastName
        }
    }
}
