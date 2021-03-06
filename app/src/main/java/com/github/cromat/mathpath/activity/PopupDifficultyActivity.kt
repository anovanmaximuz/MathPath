package com.github.cromat.mathpath.activity

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.WindowManager
import com.github.cromat.mathpath.R
import com.github.cromat.mathpath.enums.GameType
import com.github.cromat.mathpath.model.EquationConfig
import kotlinx.android.synthetic.main.activity_popup_difficulty.*

class PopupDifficultyActivity : BgMusicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_difficulty)

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val width: Int = dm.widthPixels
        val height: Int = dm.heightPixels
        val intent = Intent()
        setResult(1, intent)
        window.setLayout((width * .8).toInt(), (height * .7).toInt())
        val params: WindowManager.LayoutParams = window.attributes
        params.gravity = Gravity.CENTER
        window.attributes = params

        // Buttons
        btnCustom.setOnClickListener {
            startActivity(Intent(applicationContext, CustomGameActivity::class.java))
            finish()
        }

        btnEasy.setOnClickListener {
            val equationConfig = EquationConfig(
                    maxNumOperands = 2,
                    minNumOperands = 2,
                    maxNum = 10,
                    minNum = 0,
                    operators = listOf("+", "-"),
                    gameType = GameType.STEPS.toString(),
                    timeSec = 60,
                    stepsNum = 10,
                    negativeRes = false,
                    randomizeInput = false
            )
            val intent = Intent(applicationContext, SolvingActivity::class.java)
            intent.putExtra("equationConfig", equationConfig)
            startActivity(intent)
            finish()
        }

        btnMedium.setOnClickListener {
            val equationConfig = EquationConfig(
                    maxNumOperands = 3,
                    minNumOperands = 2,
                    maxNum = 15,
                    minNum = 0,
                    operators = listOf("+", "-", "*", "/"),
                    gameType = GameType.STEPS.toString(),
                    timeSec = 60,
                    stepsNum = 15,
                    negativeRes = false,
                    randomizeInput = true,
                    goldPerTask = 3,
                    braces = true
            )
            val intent = Intent(applicationContext, SolvingActivity::class.java)
            intent.putExtra("equationConfig", equationConfig)
            startActivity(intent)
            finish()
        }

        btnHard.setOnClickListener {
            val equationConfig = EquationConfig(
                    maxNumOperands = 5,
                    minNumOperands = 3,
                    maxNum = 20,
                    minNum = 0,
                    operators = listOf("+", "-", "*", "/"),
                    gameType = GameType.STEPS.toString(),
                    timeSec = 60,
                    stepsNum = 20,
                    negativeRes = true,
                    randomizeInput = true,
                    goldPerTask = 5,
                    braces = true
            )
            val intent = Intent(applicationContext, SolvingActivity::class.java)
            intent.putExtra("equationConfig", equationConfig)
            startActivity(intent)
            finish()
        }
    }
}
