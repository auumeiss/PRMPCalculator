package com.example.calculator111

import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    //calcbutton

    private var firstnumber = ""
    private var currentnumber = ""
    private var currentoperator = ""

    val calcResult: TextView = findViewById(R.id.calcresult)
    val inputForm: TextView = findViewById(R.id.inputform)

    private var result = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        val parentLayout: ViewGroup = findViewById(R.id.layoutmain)

        for (i in 0 until parentLayout.childCount) {
            val view = parentLayout.getChildAt(i)
            if (view is Button) {
                view.setOnClickListener {
                    val buttonText=view.text.toString()
                    when{
                        buttonText.matches(Regex("[0-9]"))->{
                            if (currentoperator.isEmpty())
                            {
                                firstnumber+=buttonText
                                calcResult.text=firstnumber
                            }else
                            {
                                currentnumber+=buttonText
                                calcResult.text=currentnumber
                            }
                        }

                        buttonText.matches(Regex("[+\\-*/]"))->{
                            currentnumber=""
                            if (calcResult.text.toString().isNotEmpty())
                            {
                                currentoperator=buttonText
                                calcResult.text="0"
                            }
                        }
                        buttonText == "=" ->{
                            if (currentnumber.isNotEmpty()&& currentoperator.isNotEmpty())
                            {
                                inputForm.text = "$firstnumber$currentoperator$currentnumber"
                                result = evaluateExpr(firstnumber, currentnumber, currentoperator)
                                firstnumber=result
                                calcResult.text=result
                            }
                        }

                        buttonText == "." -> {
                            if (currentoperator.isEmpty())
                            {
                                if (firstnumber.contains("."))
                                {
                                    if (firstnumber.isEmpty())firstnumber+="0$buttonText"
                                    else firstnumber +=buttonText
                                    calcResult.text=firstnumber
                                }
                            }
                            else
                            {
                                if (currentnumber.contains("."))
                                {
                                    if(currentnumber.isEmpty()) currentnumber+="0$buttonText"
                                    else currentnumber += buttonText
                                    calcResult.text = currentnumber
                                }
                            }
                        }
                        buttonText=="C"->{
                            currentnumber=""
                            firstnumber=""
                            currentoperator=""
                            calcResult.text=""
                            inputForm.text=""
                        }
                    }
                }
            }
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun evaluateExpr (firstNumber:String, secondNumber:String, operator:String):String
    {
        val num1=firstNumber.toDouble()
        val num2=secondNumber.toDouble()
        return when(operator)
        {
            "+"->(num1+num2).toString()
            "-"->(num1-num2).toString()
            "*"->(num1*num2).toString()
            "/"->(num1/num2).toString()
            else ->""
        }
    }

}