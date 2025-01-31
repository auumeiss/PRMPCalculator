package com.example.calculator111


import net.objecthunter.exp4j.ExpressionBuilder

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    //@RequiresApi(Build.VERSION_CODES.S)
    //val blurEffect = RenderEffect.createBlurEffect(10f, 10f, Shader.TileMode.MIRROR)



    //@RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var result = ""

        val calcResult: TextView = findViewById(R.id.calcresult)
        val inputForm: TextView = findViewById(R.id.inputform)

        val parentLayout: ViewGroup = findViewById(R.id.layoutmain)
        


    }
}