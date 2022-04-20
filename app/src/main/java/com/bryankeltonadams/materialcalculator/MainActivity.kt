package com.bryankeltonadams.materialcalculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bryankeltonadams.materialcalculator.InfixToPostfixAndEvaluateUtil.Companion.evaluatePostfix
import com.bryankeltonadams.materialcalculator.InfixToPostfixAndEvaluateUtil.Companion.infixToPostfix
import com.bryankeltonadams.materialcalculator.databinding.ActivityMainBinding
import java.math.BigDecimal

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var tvInput: TextView? = null
    private var lastNumeric: Boolean = false
    private var lastDot: Boolean = false
    private var shouldClear: Boolean = false
    private var leftParenSet: Boolean = false
    private var parensContainValue: Boolean = false
    private var parenDepth: Int = 0
    private var digitButtons: List<Button> = mutableListOf()
    private var operatorButtons: List<Button> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        //Todo: Use MVVM with a ViewModel
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
        val digitMulti: Button = binding.btnMultiply
        val digitDiv: Button = binding.btnDivide
        val digitMod: Button = binding.btnMod
        val digitClr: Button = binding.btnClr
        val digitSub: Button = binding.btnSub
        val digitAdd: Button = binding.btnAdd
        val digitEquals: Button = binding.btnEquals
        val digitParenthesis: Button = binding.btnParenthesis
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
            digitMulti,
            digitDiv,
            digitMod,
            digitParenthesis
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
            onClear()
        }
        digitDecimalPoint.setOnClickListener {
            onDecimalPoint(it as Button)
        }
        digitEquals.setOnClickListener {
            onEqual()
        }

    }

    private fun onDigit(button: Button) {
        if (shouldClear) {
            tvInput?.text = ""
            shouldClear = false
        }
        tvInput?.append(button.text)
        lastNumeric = true
        lastDot = false
        if (leftParenSet) {
            parensContainValue = true
        }
    }

    private fun onClear() {
        tvInput?.text = ""
        lastNumeric = false
        lastDot = false
        parensContainValue = false
        parenDepth = 0
        leftParenSet = false
    }

    private fun onOperator(button: Button) {
        tvInput?.text?.let {
            if ((lastNumeric || button.text == "( )" || button.text == "-") && !lastDot && tvInput?.text.toString() != "Infinity" && tvInput?.text.toString() != "NaN") {
                shouldClear = false
                if (button.text == "( )") {
                    if (parensContainValue && lastNumeric) {
                        tvInput?.append(")")
                        parenDepth -= 1
                        if (parenDepth == 0) {
                            parensContainValue = false
                        }
                        leftParenSet = false
                        lastNumeric = true
                    } else {
                        tvInput?.append("(")
                        parenDepth += 1
                        leftParenSet = true
                        lastNumeric = false
                    }
                } else {
                    tvInput?.append(button.text)
                    lastNumeric = false
                }
                lastDot = false
            }
        }

    }

    private fun onEqual() {
        if (lastNumeric && !leftParenSet && parenDepth == 0 && tvInput?.text.toString() != "Infinity" && tvInput?.text.toString() != "NaN") {
            if (tvInput?.text?.contains('E') == true) {
                Toast.makeText(
                    this,
                    getString(R.string.unable_to_calculate_exponential),
                    Toast.LENGTH_LONG
                ).show()
                return
            }
            shouldClear = true
            val postfix = infixToPostfix(tvInput?.text.toString())
            when (val evaluatePostfix = evaluatePostfix(postfix)) {
                "Infinity" -> {
                    tvInput?.text = getString(R.string.infinity)
                }
                "NaN" -> {
                    tvInput?.text = getString(R.string.nan)
                }
                else -> {
                    val evaluatedBigDecimal =
                        BigDecimal(evaluatePostfix)
                    tvInput?.text = evaluatedBigDecimal.stripTrailingZeros().toPlainString()
                }
            }

            parenDepth = 0
            leftParenSet = false
            parensContainValue = false
            lastDot = false
            lastNumeric = true
        }
    }

    private fun onDecimalPoint(button: Button) {
        if (!lastDot && tvInput?.text.toString() != "Infinity" && tvInput?.text.toString() != "NaN") {
            if (shouldClear) {
                tvInput?.text = ""
                shouldClear = false
            }
            tvInput?.append(button.text)
            lastNumeric = false
            lastDot = true
        }
    }

}