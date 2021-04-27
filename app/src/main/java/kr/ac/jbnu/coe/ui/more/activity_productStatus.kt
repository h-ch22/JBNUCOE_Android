package kr.ac.jbnu.coe.ui.more

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.royrodriguez.transitionbutton.TransitionButton
import kr.ac.jbnu.coe.R
import java.text.SimpleDateFormat
import java.util.*

class activity_productStatus : AppCompatActivity(), View.OnClickListener{
    lateinit var img_status : ImageView
    lateinit var txt_status : TextView
    lateinit var txt_batteryStatus : TextView
    lateinit var txt_umbrellaStatus : TextView
    lateinit var txt_calculatorStatus : TextView
    lateinit var txt_labcoatStatus : TextView
    lateinit var btn_checkLog : TransitionButton
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var txt_slipperStatus : TextView
    lateinit var txt_uniformStatus : TextView
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_product)

        img_status = findViewById(R.id.img_status)
        txt_status = findViewById(R.id.txt_status)
        txt_batteryStatus = findViewById(R.id.battery_status)
        txt_umbrellaStatus = findViewById(R.id.umbrella_status)
        txt_calculatorStatus = findViewById(R.id.calculator_status)
        txt_labcoatStatus = findViewById(R.id.labcoat_status)
        btn_checkLog = findViewById(R.id.btn_checkLog)
        toolbar = findViewById(R.id.toolbar)
        txt_slipperStatus = findViewById(R.id.slipper_status)
        txt_uniformStatus = findViewById(R.id.uniform_status)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        btn_checkLog.setOnClickListener(this)
        setStatus()
        setProductStatus()
    }

    override fun onClick(v: View?) {
        if (v != null){
            if (v.id == R.id.btn_checkLog){
                val intent = Intent(this, activity_logList::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun setStatus(){
        val docRef = db.collection("Products").document("status")
        docRef.get().addOnCompleteListener{task ->
            if(task.isSuccessful){
                val document = task.result

                if(document.exists()){
                    val status : Boolean = document.get("isAvailable") as Boolean

                    if (status) {
                        img_status.setImageResource(R.drawable.ic_checkmark)
                        img_status.setColorFilter(Color.parseColor("#8fff93"))
                        txt_status.text = "서비스를 정상적으로 이용하실 수 있습니다."
                        txt_status.setTextColor(Color.parseColor("#8fff93"))
                    }

                    else{
                        img_status.setImageResource(R.drawable.ic_notavailable)
                        txt_status.text = "서비스 준비 중입니다."
                        txt_status.setTextColor(Color.parseColor("#ffd864"))
                    }
                }
            }
        }
    }

    fun setProductStatus(){
        val calcRef = db.collection("Products").document("calculator")
        val umbrellaRef = db.collection("Products").document("umbrella")
        val labcoatRef = db.collection("Products").document("labcoat")
        val batteryRef = db.collection("Products").document("battery")
        val uniformRef = db.collection("Products").document("uniform")
        val slipperRef = db.collection("Products").document("slipper")

        calcRef.get().addOnCompleteListener{task ->
            if(task.isSuccessful){
                val document = task.result

                if(document.exists()){
                    var calcAll : Long = document.get("all") as Long
                    var calcCurrent : Long = document.get("late") as Long

                    if(calcAll >= calcCurrent){
                        txt_calculatorStatus.text = "대여 가능 (" + calcCurrent + " / " + calcAll + ")"
                        txt_calculatorStatus?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0)
                        txt_calculatorStatus.setTextColor(Color.parseColor("#009630"))
                    }

                    if(calcCurrent == 0L){
                        txt_calculatorStatus.text = "대여 불가"
                        txt_calculatorStatus?.setTextColor(Color.parseColor("#ff5145"))
                        txt_calculatorStatus?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0)
                        txt_calculatorStatus?.compoundDrawables?.get(0)?.setTint(Color.parseColor("#ff5145"))

                    }
                }
            }
        }

        umbrellaRef.get().addOnCompleteListener{task ->
            if(task.isSuccessful){
                val document = task.result

                if(document.exists()){
                    var umbrellaAll : Long = document.get("all") as Long
                    var umbrellaCurrent : Long = document.get("late") as Long

                    if(umbrellaAll >= umbrellaCurrent){
                        txt_umbrellaStatus.text = "대여 가능 (" + umbrellaCurrent + " / " + umbrellaAll + ")"
                        txt_umbrellaStatus.setTextColor(Color.parseColor("#009630"))
                        txt_umbrellaStatus?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0)
                        txt_umbrellaStatus?.compoundDrawables?.get(0)?.setTint(Color.parseColor("#009630"))
                    }

                    if(umbrellaCurrent == 0L){
                        txt_umbrellaStatus.text = "대여 불가"
                        txt_umbrellaStatus?.setTextColor(Color.parseColor("#ff5145"))
                        txt_umbrellaStatus?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0)
                        txt_umbrellaStatus?.compoundDrawables?.get(0)?.setTint(Color.parseColor("#ff5145"))
                    }
                }
            }
        }

        labcoatRef.get().addOnCompleteListener{task ->
            if(task.isSuccessful){
                val document = task.result

                if(document.exists()){
                    var labcoatAll : Long = document.get("all") as Long
                    var labcoatCurrent : Long = document.get("late") as Long

                    if(labcoatAll >= labcoatCurrent){
                        txt_labcoatStatus.text = "대여 가능 (" + labcoatCurrent + " / " + labcoatAll + ")"
                        txt_labcoatStatus.setTextColor(Color.parseColor("#009630"))
                        txt_labcoatStatus?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0)
                        txt_labcoatStatus?.compoundDrawables?.get(0)?.setTint(Color.parseColor("#009630"))
                    }

                    if(labcoatCurrent == 0L){
                        txt_labcoatStatus.text = "대여 불가"
                        txt_labcoatStatus?.setTextColor(Color.parseColor("#ff5145"))
                        txt_labcoatStatus?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0)
                        txt_labcoatStatus?.compoundDrawables?.get(0)?.setTint(Color.parseColor("#ff5145"))
                    }
                }
            }
        }

        batteryRef.get().addOnCompleteListener{task ->
            if(task.isSuccessful){
                val document = task.result

                if(document.exists()){
                    var batteryAll : Long = document.get("all") as Long
                    var batteryCurrent : Long = document.get("late") as Long

                    if(batteryAll >= batteryCurrent){
                        txt_batteryStatus.text = "대여 가능 (" + batteryCurrent + " / " + batteryAll + ")"
                        txt_batteryStatus.setTextColor(Color.parseColor("#009630"))
                        txt_batteryStatus?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0)
                        txt_batteryStatus?.compoundDrawables?.get(0)?.setTint(Color.parseColor("#009630"))
                    }

                    if(batteryCurrent == 0L){
                        txt_batteryStatus.text = "대여 불가"
                        txt_batteryStatus?.setTextColor(Color.parseColor("#ff5145"))
                        txt_batteryStatus?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0)
                        txt_batteryStatus?.compoundDrawables?.get(0)?.setTint(Color.parseColor("#ff5145"))
                    }
                }
            }
        }

        uniformRef.get().addOnCompleteListener{task ->
            if(task.isSuccessful){
                val document = task.result

                if(document.exists()){
                    var uniformAll : Long = document.get("all") as Long
                    var uniformCurrent : Long = document.get("late") as Long

                    if(uniformAll >= uniformCurrent){
                        txt_uniformStatus.text = "대여 가능 (" + uniformCurrent + " / " + uniformAll + ")"
                        txt_uniformStatus.setTextColor(Color.parseColor("#009630"))
                        txt_uniformStatus?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0)
                        txt_uniformStatus?.compoundDrawables?.get(0)?.setTint(Color.parseColor("#009630"))
                    }

                    if(uniformCurrent == 0L){
                        txt_uniformStatus.text = "대여 불가"
                        txt_uniformStatus?.setTextColor(Color.parseColor("#ff5145"))
                        txt_uniformStatus?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0)
                        txt_uniformStatus?.compoundDrawables?.get(0)?.setTint(Color.parseColor("#ff5145"))
                    }
                }
            }
        }

        slipperRef.get().addOnCompleteListener{task ->
            if(task.isSuccessful){
                val document = task.result

                if(document.exists()){
                    var slipperAll : Long = document.get("all") as Long
                    var slipperCurrent : Long = document.get("late") as Long

                    if(slipperAll >= slipperCurrent){
                        txt_slipperStatus.text = "대여 가능 (" + slipperCurrent + " / " + slipperAll + ")"
                        txt_slipperStatus.setTextColor(Color.parseColor("#009630"))
                        txt_slipperStatus?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0)
                        txt_slipperStatus?.compoundDrawables?.get(0)?.setTint(Color.parseColor("#009630"))
                    }

                    if(slipperCurrent == 0L){
                        txt_slipperStatus.text = "대여 불가"
                        txt_slipperStatus?.setTextColor(Color.parseColor("#ff5145"))
                        txt_slipperStatus?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0)
                        txt_slipperStatus?.compoundDrawables?.get(0)?.setTint(Color.parseColor("#ff5145"))
                    }
                }
            }
        }
    }
}