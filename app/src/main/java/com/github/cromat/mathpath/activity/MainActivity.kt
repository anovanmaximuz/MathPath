package com.github.cromat.mathpath.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import com.github.cromat.mathpath.DbHelper
import com.github.cromat.mathpath.R
import com.github.cromat.mathpath.activity.adapters.PetItemAdapter
import com.github.cromat.mathpath.model.PetItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.pet_container.*
import kotlinx.android.synthetic.main.activity_main.pet_item_list as petItemList
import com.github.cromat.mathpath.BackgroundSoundService
import java.util.*


@SuppressLint("SetTextI18n")
class MainActivity : BgMusicActivity() {
    private val timer = Timer(false)
    private val rand = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Open Db and create tables if not existing
        DbHelper(applicationContext).writableDatabase

        // Start music
        try {
            startService(Intent(this, BackgroundSoundService::class.java))
        }
        catch (e: Exception){
            e.printStackTrace()
        }

        val goldCurrent = DbHelper.getGoldValue(applicationContext).toString()
        goldViewMain.text = goldCurrent

        btnStart.setOnClickListener {
            startActivityForResult(Intent(applicationContext, PopupDifficultyActivity::class.java), 1)
        }

        iconChart.setOnClickListener {
            startActivity(Intent(applicationContext, GraphActivity::class.java))
        }

        iconInfo.setOnClickListener {
            startActivity(Intent(applicationContext, PopupInfoActivity::class.java))
        }

        imagePetCar.setOnClickListener {
            MediaPlayer.create(applicationContext, R.raw.car_horn).start()
        }

        imagePetBall.setOnClickListener {
            val rotate = RotateAnimation(0f, 720f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            rotate.duration = 2000
            rotate.interpolator = LinearInterpolator()
            imagePetBall.startAnimation(rotate)
        }

        imagePetPlane.setOnClickListener {
            MediaPlayer.create(applicationContext, R.raw.airplane).start()
            val rotate = RotateAnimation(0f, -350f, 15f, 15f)
            rotate.duration = 1000
            rotate.interpolator = LinearInterpolator()
            imagePetPlane.startAnimation(rotate)
        }

        imagePet.setOnClickListener {
            val sounds = listOf(R.raw.pet_sound1, R.raw.pet_sound2)
            MediaPlayer.create(applicationContext, sounds[rand.nextInt(sounds.size)]).start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        goldViewMain.text = DbHelper.getGoldValue(applicationContext).toString()
    }

    override fun onResume() {
        super.onResume()
        val petItems: List<PetItem> = DbHelper.getPetItems(applicationContext)

        for (petItem in petItems) {
            if (petItem.bindedElementId > 0) {
                val imgId = petItem.bindedElementId
                val relImg = (this as Activity).findViewById(imgId) as ImageView
                if (petItem.activated && petItem.bought) {
                    relImg.visibility = View.VISIBLE
                } else {
                    relImg.visibility = View.INVISIBLE
                }
            }
        }

        val adapter = PetItemAdapter(petItems)
        petItemList.adapter = adapter
        petItemList.layoutManager = LinearLayoutManager(this)
        checkHappiness()
    }

    fun checkHappiness() {
        val curHappiness = DbHelper.getHapiness(this)
        textHappiness.text = getString(R.string.happiness) + curHappiness.toString() + "%"
        progressHappiness.progress = curHappiness
        when (curHappiness) {
            in 66..100 -> {
                progressHappiness.progressDrawable.setColorFilter(
                        ContextCompat.getColor(this, R.color.colorBgGreen),
                        android.graphics.PorterDuff.Mode.SRC_IN)
                textCloud.text = getString(R.string.cloud_happy)
                imagePet.setImageResource(R.drawable.pet_happy)
            }
            in 34..65 -> {
                progressHappiness.progressDrawable.setColorFilter(
                        ContextCompat.getColor(this, R.color.colorBtnYellow),
                        android.graphics.PorterDuff.Mode.SRC_IN)
                textCloud.text = getString(R.string.cloud_average)
                imagePet.setImageResource(R.drawable.pet)
            }
            in 0..33 -> {
                progressHappiness.progressDrawable.setColorFilter(
                        ContextCompat.getColor(this, R.color.colorBtnRed),
                        android.graphics.PorterDuff.Mode.SRC_IN)
                textCloud.text = getString(R.string.cloud_unhappy)
                imagePet.setImageResource(R.drawable.pet_unhappy)

            }
        }
    }

    fun textCloudThankYou() {
        textCloud.text = getString(R.string.thank_you)
        timer.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread { checkHappiness() }
            }
        }, 2000)
    }
}
