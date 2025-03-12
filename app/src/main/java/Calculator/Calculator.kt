package Calculator

import android.widget.Button
import android.widget.TextView
import net.objecthunter.exp4j.ExpressionBuilder

class Calculator {
    private fun limitInput(result: TextView) {
        if (result.text.length >= 28) {
            result.text = result.text.substring(0, 28)
        }
    }

    fun Number(result:TextView, num:Button)
    {
        result.append(num.text)
        limitInput(result)
    }

    fun Result(result: TextView, history: MutableList<String>): String {
        val optext = result.text.toString()
        if (optext.isNotEmpty()) {
            try {
                val expr = ExpressionBuilder(result.text.toString()).build()
                val res = expr.evaluate()
                val longres = res.toLong()
                val tohistory = "$optext=${
                    if (longres.toDouble() == res) longres else res
                }"
                history.add(tohistory)
                return if (longres.toDouble() == res) longres.toString() else res.toString()
            } catch (e: Exception) {
                return "Error"
            }
        }
        return ""
    }

    fun Sqrt(sqrt: Button, result: TextView) {
        if (result.text.isEmpty() || result.text.last() in "+-*/") {
            result.append("sqrt(")
            limitInput(result)
        }
    }

    fun Formula(formula: TextView, result: TextView) {
        val temp = result.text
        result.text = formula.text
        formula.text = temp
    }

    fun Dot(dot: Button, result: TextView) {
        val currentText = result.text.toString()

        if (currentText.isEmpty() || currentText == "0") {
            result.text = "0."
            limitInput(result)
            return
        }

        val lastNumber = currentText.split(Regex("[+\\-*/]")).last()
        if (!lastNumber.contains(".") && currentText.last().isDigit()) {
            result.append(".")
            limitInput(result)
            return
        }
    }

    fun Operation(button: Button, result: TextView) {
        if (result.text.isNotEmpty() && (result.text.last().isDigit() || result.text.last() == ')')) {
            result.append(button.text)
            limitInput(result)
        }
    }

    fun Trig(button: Button, result: TextView) {
        if (result.text.isEmpty() || result.text.last() in "+-*/") {
            result.append(button.text)
            limitInput(result)
        }
    }

    fun Otkr(otkr: Button, result: TextView) {
        if (result.text.isEmpty() || result.text.last() in "+-*/") {
            result.append(otkr.text)
            limitInput(result)
        }
    }
}