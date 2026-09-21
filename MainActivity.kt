package com.example.calculator

import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.LinearLayout
import android.app.Activity
import java.util.Locale

class MainActivity : Activity() {
    private lateinit var display: TextView
    private var current = "0"
    private var stored: Double? = null
    private var op: Char? = null
    private var fresh = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildUi()
    }

    private fun buildUi() {
        val bg = Color.rgb(16,16,16)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(18, 28, 18, 18)
            setBackgroundColor(bg)
        }
        display = TextView(this).apply {
            text = "0"
            textSize = 52f
            setTextColor(Color.WHITE)
            gravity = Gravity.END or Gravity.CENTER_VERTICAL
            setPadding(8, 10, 8, 18)
        }
        root.addView(display, LinearLayout.LayoutParams(-1, 0, 1.1f))

        val grid = GridLayout(this).apply {
            columnCount = 4
            rowCount = 5
            useDefaultMargins = true
        }
        val keys = arrayOf(
            "C","±","%","÷",
            "7","8","9","×",
            "4","5","6","−",
            "1","2","3","+",
            "0",".","⌫","="
        )
        keys.forEach { key ->
            val b = Button(this).apply {
                text = key
                textSize = if (key == "=") 25f else 21f
                isAllCaps = false
                setTextColor(Color.WHITE)
                setBackgroundColor(if (key in arrayOf("÷","×","−","+","=")) Color.rgb(103,80,164) else Color.rgb(48,48,48))
                setOnClickListener { press(key) }
            }
            val p = GridLayout.LayoutParams().apply {
                width = 0
                height = 0
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(4,4,4,4)
            }
            grid.addView(b, p)
        }
        root.addView(grid, LinearLayout.LayoutParams(-1, 0, 2.4f))
        setContentView(root)
    }

    private fun press(key: String) {
        when (key) {
            "C" -> { current = "0"; stored = null; op = null; fresh = true }
            "±" -> if (current != "0") current = if (current.startsWith("-")) current.drop(1) else "-$current"
            "%" -> current = fmt(current.toDoubleOrNull()?.div(100.0) ?: 0.0)
            "." -> if (!current.contains(".")) current += "."
            "⌫" -> current = if (current.length > 1) current.dropLast(1) else "0"
            "+","−","×","÷" -> chooseOp(key[0])
            "=" -> calculate()
            else -> {
                if (fresh || current == "0") current = key else current += key
                fresh = false
            }
        }
        display.text = current
    }

    private fun chooseOp(symbol: Char) {
        stored = current.toDoubleOrNull() ?: 0.0
        op = symbol
        fresh = true
    }

    private fun calculate() {
        val a = stored ?: return
        val b = current.toDoubleOrNull() ?: return
        val result = when (op) {
            '+' -> a + b
            '−' -> a - b
            '×' -> a * b
            '÷' -> if (b == 0.0) { current = "Error"; stored = null; op = null; fresh = true; return } else a / b
            else -> b
        }
        current = fmt(result)
        stored = null
        op = null
        fresh = true
    }

    private fun fmt(x: Double): String =
        if (x.isNaN() || x.isInfinite()) "Error"
        else if (x == x.toLong().toDouble()) x.toLong().toString()
        else String.format(Locale.US, "%.10f", x).trimEnd('0').trimEnd('.')
}
