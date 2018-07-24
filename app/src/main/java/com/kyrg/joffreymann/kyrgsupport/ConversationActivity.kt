package com.kyrg.joffreymann.kyrgsupport

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class ConversationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //val img = findViewById<View>(R.id.image) as ImageView
        //img.setImageResource(R.drawable.white_logo_transparent_background)
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