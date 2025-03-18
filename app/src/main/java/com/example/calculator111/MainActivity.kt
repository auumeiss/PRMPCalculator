package com.example.calculator111


import Calculator.Calculator
import Calculator.FirebaseDb
import Calculator.MyGestureListener
import Calculator.ThemeSaving
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.BatteryManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private lateinit var gestureDetector: GestureDetectorCompat
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var result: TextView
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }
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

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM Token", token)
            } else {
                Log.e("FCM Token", "Failed to get token", task.exception)
            }
        }
        val db = Firebase.firestore
        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val themeSaving=ThemeSaving()
        var newThemeState:Boolean = false
        ThemeSaving().loadDeviceData(db, deviceId) { themeBoolean ->
            // themeBoolean содержит значение темы
            if (themeBoolean) {
                // Применить темную тему
                newThemeState=true
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                // Применить светлую тему
                newThemeState=false
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        var history: MutableList<String> = mutableListOf()
        FirebaseApp.initializeApp(this)
        history.add("")
        result = findViewById(R.id.result)
        gestureDetector = GestureDetectorCompat(this, MyGestureListener(result))
        val firebasedb = FirebaseDb()
        val calc: Calculator = Calculator()
        val formula: TextView = findViewById(R.id.formula)
        result.text=""
        formula.text=""
        //батарея 50%
        val batteryReceiver: BroadcastReceiver
        batteryReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
                if (level < 50) {
                    result.setTextColor(resources.getColor(android.R.color.white))
                } else {
                    result.setTextColor(resources.getColor(android.R.color.darker_gray))
                }
            }
        }
        registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        val historybutton:Button = findViewById(R.id.history)
        historybutton.setOnClickListener() {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
        val cos:Button = findViewById(R.id.cos)
        val tg:Button = findViewById(R.id.tg)
        val ctg:Button = findViewById(R.id.ctg)
        val sin:Button = findViewById(R.id.sin)
        val nine:Button=findViewById(R.id.nine)
        val eight:Button=findViewById(R.id.eight)
        val seven:Button=findViewById(R.id.seven)
        val six:Button=findViewById(R.id.six)
        val five:Button=findViewById(R.id.five)
        val four:Button=findViewById(R.id.four)
        val three:Button=findViewById(R.id.three)
        val two:Button=findViewById(R.id.two)
        val one:Button=findViewById(R.id.one)
        val zero:Button=findViewById(R.id.zero)
        val zakr:Button=findViewById(R.id.zakr)
        val otkr:Button=findViewById(R.id.otkr)
        val ravno:Button=findViewById(R.id.ravno)
        val clear:Button=findViewById(R.id.clear_text)
        val sqrt:Button=findViewById(R.id.sqrt)
        val minus:Button=findViewById(R.id.minus)
        val plus:Button=findViewById(R.id.plus)
        val devide:Button=findViewById(R.id.devide)
        val multiply:Button=findViewById(R.id.multiply)
        val dot:Button=findViewById(R.id.dot)
        val theme:Button=findViewById(R.id.theme)
        val buttonsTrig:Array<Button> = arrayOf(tg,ctg,sin,cos)
        val buttonOperations:Array<Button> = arrayOf(devide,multiply, minus,plus)
        val buttonsNumber: Array<Button> = arrayOf(nine,eight,seven,six,five,four,three,two,one,zero)
        formula.setOnClickListener{calc.Formula(formula,result)}
        dot.setOnClickListener { calc.Dot(dot, result) }
        sqrt.setOnClickListener{calc.Sqrt(sqrt,result)}
        buttonsNumber.forEach { button->button.setOnClickListener { calc.Number(result, button)} }
        buttonOperations.forEach { button ->
            button.setOnClickListener { calc.Operation(button, result) }
        }
        theme.setOnClickListener {
            themeSaving.saveDeviceData(db,!newThemeState,deviceId)
            newThemeState=!newThemeState
            setAppTheme(newThemeState)
        }
        clear.setOnClickListener{ result.text=""
            formula.text=""
        }
        buttonsTrig.forEach { button->button.setOnClickListener{
            calc.Trig(button,result)
            }
        }
        otkr.setOnClickListener {
            calc.Otkr(otkr, result)
        }
        zakr.setOnClickListener{
            if (result.text.last().isDigit()) result.append(zakr.text)
        }
        ravno.setOnClickListener {
            formula.text = result.text
            result.text=calc.Result(result, history)
            firebasedb.saveDeviceData(db,history,deviceId)
        }
    }
    private fun setAppTheme(isDarkTheme: Boolean) {
        if (isDarkTheme == true) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.Theme_Calculator111)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            setTheme(R.style.Theme_Calculator111)
        }
    }


    override fun onDestroy() {
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("isAuth", "false")
        editor.apply()
        super.onDestroy()
    }

}