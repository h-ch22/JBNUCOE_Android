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
        var isAvailable = false
        val sdf = SimpleDateFormat("yyyy.MM.dd")
        val sdf_time = SimpleDateFormat("HH:mm")
        val sdf_withTime = SimpleDateFormat("yyyy.MM.dd HH:mm")
        val closeTime = "18:00"
        val openTime = "09:00"

        val today = sdf_withTime.format(Date())
        val today_date = sdf.format(Date())
        val openDate = today_date + " " + openTime
        val closeDate = today_date + " " + closeTime
        Log.d("close", closeDate)
        Log.d("today", today)

        var close_fin = sdf_withTime.parse(closeDate)
        val open_fin = sdf_withTime.parse(openDate)

        if(close_fin.after(sdf_withTime.parse(today))){
            val today_tmp = Date()
            val tomorrow = Date(today_tmp.time + ( 1000 * 60 * 60 * 24 ))
            val tomorrow_str = sdf_withTime.format(tomorrow).toString()
            close_fin = sdf_withTime.parse(tomorrow_str)
        }

        if(sdf_withTime.parse(today).before(close_fin) && sdf_withTime.parse(today).after(open_fin)){
            isAvailable = true
        }

        else{
            isAvailable = false
        }

        val docRef = db.collection("Products").document("status")
        docRef.get().addOnCompleteListener{task ->
            if(task.isSuccessful){
                val document = task.result

                if(document.exists()){
                    val status : Boolean = document.get("isAvailable") as Boolean

                    if (status && isAvailable){
                        img_status.setImageResource(R.drawable.ic_checkmark)
                        txt_status.text = "서비스를 정상적으로 이용하실 수 있습니다."
                        txt_status.setTextColor(Color.parseColor("#008D2A"))
                    }

                    if (status && !isAvailable){
                        img_status.setImageResource(R.drawable.ic_notavailable)
                        txt_status.text = "서비스 준비 중입니다.\n이용 가능 시간 : 평일 오전 9시 ~ 오후 6시"
                        txt_status.setTextColor(Color.parseColor("#ffd864"))
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

        calcRef.get().addOnCompleteListener{task ->
            if(task.isSuccessful){
                val document = task.result

                if(document.exists()){
                    var calcAll : Long = document.get("all") as Long
                    var calcCurrent : Long = document.get("late") as Long

                    if(calcAll >= calcCurrent){
                        txt_calculatorStatus.text = "대여 가능 (" + calcCurrent + " / " + calcAll + ")"
                        txt_calculatorStatus.setTextColor(Color.parseColor("#009630"))
                        txt_calculatorStatus?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0)
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
    }
}