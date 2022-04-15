package com.bryankeltonadams.materialcalcyoulater

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.bryankeltonadams.materialcalcyoulater.databinding.ActivityMainBinding
import java.lang.ArithmeticException

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var tvInput: TextView? = null
    var lastNumeric: Boolean = false
    var lastDot: Boolean = false
    var digitButtons: List<Button> = mutableListOf()
    var operatorButtons: List<Button> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        tvInput = binding.tvInput
        val digitOne: Button = binding.btnOne
        val digitTwo: Button = binding.btnTwo
        val digitThree: Button = binding.btnThree
        val digitFour: Button = binding.btnFour
        val digitFive: Button = binding.btnFive
        val digitSix: Button = binding.btnSix
        val digitSeven: Button = binding.btnSeven
        val digitEight: Button = binding.btnEight
        val digitNine: Button = binding.btnNine
        val digitZero: Button = binding.btnZero
        val digitDecimalPoint: Button = binding.btnDecimalPoint
        val digitMult: Button = binding.btnMultiply
        val digitDiv: Button = binding.btnDivide
        val digitClr: Button = binding.btnClr
        val digitSub: Button = binding.btnSub
        val digitAdd: Button = binding.btnAdd
        val digitEquals: Button = binding.btnEquals
        digitButtons = mutableListOf(
            digitZero,
            digitOne,
            digitTwo,
            digitThree,
            digitFour,
            digitFive,
            digitSix,
            digitSeven,
            digitEight,
            digitNine
        )
        operatorButtons = mutableListOf(
            digitAdd,
            digitSub,
            digitMult,
            digitDiv
        )
        for (button in digitButtons) {
            button.setOnClickListener {
                onDigit(it as Button)
            }
        }
        for (button in operatorButtons) {
            button.setOnClickListener {
                onOperator(it as Button)
            }
        }
        digitClr.setOnClickListener {
            onClear(it as Button)
        }
        digitDecimalPoint.setOnClickListener {
            onDecimalPoint(it as Button)
        }
        digitEquals.setOnClickListener {
            onEqual(it as Button)
        }

    }

    fun onDigit(button: Button) {
        tvInput?.append(button.text)
        lastNumeric = true
        lastDot = false
    }

    fun onClear(view: View) {
        tvInput?.text = ""
        lastNumeric = false
        lastDot = false
    }

    fun onOperator(button: Button) {
        tvInput?.text?.let {
            if (lastNumeric && !isOperatorAdded(it.toString())) {
                tvInput?.append(button.text)
                lastNumeric = false
                lastDot = false
            }
        }

    }

    fun onEqual(button: Button) {
        if (lastNumeric) {
            var tvValue = tvInput?.text.toString()
            var prefix = ""
            try {
                if (tvValue.startsWith("-")) {
                    prefix = "-"
                    tvValue = tvValue.substring(1)
                }
                val splitValue = tvValue.split("-", "+", "*", "/")
                if (splitValue.size == 2) {
                    var lhs = splitValue[0]
                    if (prefix.isNotEmpty()) {
                        lhs = prefix + lhs
                    }
                    var rhs = splitValue[1]
                    when {
                        tvValue.contains("+") -> {
                            tvInput?.text =
                                removeZeroAfterDot((lhs.toDouble() + rhs.toDouble()).toString())
                        }
                        tvValue.contains("-") -> {
                            tvInput?.text =
                                removeZeroAfterDot((lhs.toDouble() - rhs.toDouble()).toString())
                        }
                        tvValue.contains("/") -> {
                            tvInput?.text =
                                removeZeroAfterDot((lhs.toDouble() / rhs.toDouble()).toString())
                        }
                        tvValue.contains("*") -> {
                            tvInput?.text =
                                removeZeroAfterDot((lhs.toDouble() * rhs.toDouble()).toString())

                        }
                    }
                }


            } catch (e: ArithmeticException) {
                e.printStackTrace()
            }
        }
    }

    private fun removeZeroAfterDot(result: String): String {
        return if (result.contains(".0"))
            result.substring(0, result.length - 2)
        else {
            result
        }
    }

    fun onDecimalPoint(button: Button) {
        if (lastNumeric && !lastDot || (tvInput?.length() == 0 && !lastDot)) {
            tvInput?.append(button.text)
            lastNumeric = false
            lastDot = false
        }
    }

    private fun isOperatorAdded(value: String): Boolean {
        return if (value.startsWith('-')) {
            false
        } else {
            value.contains("+") || value.contains("-") || value.contains("*") || value.contains("/")
        }
    }
}