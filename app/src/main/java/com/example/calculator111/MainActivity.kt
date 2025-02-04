package com.example.calculator111


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import net.objecthunter.exp4j.ExpressionBuilder

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.min

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


        val result: TextView = findViewById(R.id.result)
        val formula: TextView = findViewById(R.id.formula)
        result.text=""
        formula.text=""

        val parentLayout: ViewGroup = findViewById(R.id.layoutmain)

        var batteryReceiver: BroadcastReceiver

        batteryReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                // Получаем текущий уровень батареи
                val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1

                // Обновляем TextView

                // Если уровень меньше 20%, меняем цвет текста на красный
                if (level < 50) {
                    result.setTextColor(resources.getColor(android.R.color.holo_red_dark))
                } else {
                    // Иначе возвращаем стандартный цвет
                    result.setTextColor(resources.getColor(android.R.color.black))
                }
            }
        }
        registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

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

        sqrt.setOnClickListener{if (result.text.isEmpty() || result.text.last().equals('+')|| result.text.last().equals('-')|| result.text.last().equals('*')|| result.text.last().equals('/')) {
            result.append("sqrt(")
        }
        }

        val minus:Button=findViewById(R.id.minus)
        val plus:Button=findViewById(R.id.plus)
        val devide:Button=findViewById(R.id.devide)
        val multiply:Button=findViewById(R.id.multiply)
        formula.setOnClickListener{
            val temp = result.text
            result.text=formula.text
            formula.text=temp
        }
        val dot:Button=findViewById(R.id.dot)
        dot.setOnClickListener {
            val currentText = result.text.toString()

            // Проверяем, пуст ли экран -> ставим "0."
            if (currentText.isEmpty() || currentText == "0") {
                result.text = "0."
                return@setOnClickListener
            }

            // Проверяем последнее число (после оператора) на наличие точки
            val lastNumber = currentText.split(Regex("[+\\-*/]")).last()
            if (!lastNumber.contains(".")&&currentText.last().isDigit()) {
                result.append(".")
                return@setOnClickListener
            }
        }

        val buttonsNumber: Array<Button> = arrayOf(nine,eight,seven,six,five,four,three,two,one,zero)

        buttonsNumber.forEach { button->button.setOnClickListener { result.append(button.text)} }

        val buttonOperations:Array<Button> = arrayOf(devide,multiply, minus,plus)

        buttonOperations.forEach { button->
            button.setOnClickListener { if (result.text.isNotEmpty() && result.text.last().isDigit()||result.text.last().equals(')'))
                result.append(button.text)}}
        clear.setOnClickListener{
            result.text=""
            formula.text=""
        }


        val buttonsTrig:Array<Button> = arrayOf(tg,ctg,sin,cos)
        buttonsTrig.forEach { button->button.setOnClickListener{
            if (result.text.isEmpty() || result.text.last().equals('+')|| result.text.last().equals('-')|| result.text.last().equals('*')|| result.text.last().equals('/')){
                result.append(button.text)
            }
        } }

        otkr.setOnClickListener {
            if (result.text.isEmpty() || result.text.last().equals('+') || result.text.last()
                    .equals('-') || result.text.last().equals('*') || result.text.last().equals('/')
            ) {
                result.append(otkr.text)
            }
        }
        zakr.setOnClickListener{
            if (result.text.last().isDigit()) result.append(zakr.text)
        }

        ravno.setOnClickListener {
            val optext = result.text.toString()
            if (optext.isNotEmpty()) {
                try {
                    val expr = ExpressionBuilder(result.text.toString()).build()
                    val res = expr.evaluate()
                    val longres = res.toLong()
                    if (longres.toDouble() == res) {
                        result.text = longres.toString()
                    } else {
                        result.text = res.toString()
                    }
                } catch (e: Exception) {
                    result.text = "Error"
                }
            }
            formula.text = optext





        }




    }
}