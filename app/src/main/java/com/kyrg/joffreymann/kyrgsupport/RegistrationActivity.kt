package com.kyrg.joffreymann.kyrgsupport

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.view.MenuItem
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_registration.*
import android.view.KeyEvent.KEYCODE_BACK
import android.view.KeyEvent
import com.google.firebase.database.DatabaseReference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.android.gms.tasks.Task
import android.view.View
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener





class RegistrationActivity: AppCompatActivity() {
    private var mDatabase: DatabaseReference? = null
    private var mMessageReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputManager.hideSoftInputFromWindow(
//                this.currentFocus!!.windowToken,
//                InputMethodManager.HIDE_NOT_ALWAYS)
        setContentView(R.layout.activity_registration)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        // get reference to button
        val registerButton = findViewById<Button>(R.id.register)
        // set on-click listener
        registerButton.setOnClickListener {
            val firstName = findViewById<EditText>(R.id.firstNameEditText).text.toString()
            val lastName = findViewById<EditText>(R.id.lastNameEditText).text.toString()
            val email = findViewById<EditText>(R.id.emailEditText).text.toString()
            val password = findViewById<EditText>(R.id.passwordEditText).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.confirmPasswordEditText).text.toString()
            val phone = findViewById<EditText>(R.id.phoneEditText).text.toString()
            writeNewUser(email, password, phone, firstName, lastName)
            hideKeyboard()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // TODO Auto-generated method stub
        val id = item.getItemId()
        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() === 0) {
            // do something on back.
            onBackPressed()
            true
        } else super.onKeyDown(keyCode, event)

    }

    private fun writeNewUser(email: String, password: String, phone: String, firstName: String, lastName: String) {
        val admin = LoginActivity.KYRGAdmin(email, password, phone, firstName, lastName)

        if(this.formCompletelyFilledOut()){
            var auth = FirebaseAuth.getInstance()
            auth.createUserWithEmailAndPassword(admin.email!!, admin.password!!)
                    .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.getCurrentUser()
                            FirebaseDatabase.getInstance().reference.child("Administrators").child(task.result.user.uid).child("e-mail").setValue(admin.email)
                            FirebaseDatabase.getInstance().reference.child("Administrators").child(task.result.user.uid).child("phone").setValue(admin.phone)
                            FirebaseDatabase.getInstance().reference.child("Administrators").child(task.result.user.uid).child("firstName").setValue(admin.firstName)
                            FirebaseDatabase.getInstance().reference.child("Administrators").child(task.result.user.uid).child("lastName").setValue(admin.lastName)
                            Toast.makeText(this, "You have successfully created an account.",
                                    Toast.LENGTH_SHORT).show()
                            val firstNameField = findViewById<EditText>(R.id.firstNameEditText)
                            val lastNameField = findViewById<EditText>(R.id.lastNameEditText)
                            val emailField = findViewById<EditText>(R.id.emailEditText)
                            val passwordField = findViewById<EditText>(R.id.passwordEditText)
                            val confirmPasswordField = findViewById<EditText>(R.id.confirmPasswordEditText)
                            val phoneField = findViewById<EditText>(R.id.phoneEditText)
                            firstNameField.text = null
                            lastNameField.text = null
                            emailField.text = null
                            passwordField.text = null
                            confirmPasswordField.text = null
                            phoneField.text = null
                            onBackPressed()
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                        }

                        // ...
                    })
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

    private fun formCompletelyFilledOut(): Boolean {
        val firstNameField = findViewById<EditText>(R.id.firstNameEditText)
        val lastNameField = findViewById<EditText>(R.id.lastNameEditText)
        val emailField = findViewById<EditText>(R.id.emailEditText)
        val passwordField = findViewById<EditText>(R.id.passwordEditText)
        val confirmPasswordField = findViewById<EditText>(R.id.confirmPasswordEditText)
        val phoneField = findViewById<EditText>(R.id.phoneEditText)
        return (emailField.text.length > 0 && passwordField.text.length > 6 && phoneField.text.length > 0 && firstNameField.text.length > 0 && lastNameField.text.length > 0)
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
}