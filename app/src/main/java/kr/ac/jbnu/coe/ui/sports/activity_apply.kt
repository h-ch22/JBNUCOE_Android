package kr.ac.jbnu.coe.ui.sports

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.royrodriguez.transitionbutton.TransitionButton
import kotlinx.android.synthetic.main.layout_apply.*
import kr.ac.jbnu.coe.R
import java.util.*

class activity_apply : AppCompatActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var date : EditText
    lateinit var roomName : EditText
    lateinit var event : EditText
    lateinit var allPeople : EditText
    lateinit var currentPeople : EditText
    lateinit var location : EditText
    lateinit var limit : EditText
    lateinit var apply : TransitionButton
    lateinit var date_str : String
    lateinit var roomName_str : String
    lateinit var event_str : String
    lateinit var allPeople_str : String
    lateinit var curPeople_str : String
    lateinit var location_str : String
    lateinit var limit_str : String
    var year = 0
    var month = 0
    var day = 0
    var hour = 0
    var minute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_apply)

        toolbar = findViewById(R.id.toolbar)
        date = findViewById(R.id.field_date)
        roomName = findViewById(R.id.field_roomName)
        event = findViewById(R.id.field_event)
        allPeople = findViewById(R.id.field_allPeople)
        currentPeople = findViewById(R.id.field_applyPeople)
        location = findViewById(R.id.field_location)
        limit = findViewById(R.id.field_limit)
        apply = findViewById(R.id.btn_apply)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        date.setOnClickListener(this)
        apply.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if(v != null){
            if(v.id == R.id.field_date){
                val calendar: Calendar = Calendar.getInstance()
                day = calendar.get(Calendar.DAY_OF_MONTH)
                month = calendar.get(Calendar.MONTH)
                year = calendar.get(Calendar.YEAR)
                val datePickerDialog =
                    DatePickerDialog(this@activity_apply, this@activity_apply, year, month, day)
                datePickerDialog.show()
            }

            if(v.id == R.id.btn_apply){
                btn_apply.startAnimation()

                date_str = date.text.toString()
                roomName_str = roomName.text.toString()
                event_str = event.text.toString()
                allPeople_str = allPeople.text.toString()
                curPeople_str = currentPeople.text.toString()
                location_str = location.text.toString()
                limit_str = limit.text.toString()

                if(date_str.equals("") || roomName_str.equals("") || event_str.equals("") || allPeople_str.equals("") || curPeople_str.equals("") || location_str.equals("")){
                    btn_apply.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                    showDialog(title = "공백 필드", contents = "모든 필드를 채워주십시오.")
                }

                else{
                    if(allPeople_str.toInt() <= curPeople_str.toInt()){
                        btn_apply.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                        showDialog(title = "인원 초과", contents = "전체 인원보다 현재 인원이 더 많거나 같을 수 없습니다.")
                    }

                    else{
                        getUserData()
                    }
                }


            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        day = dayOfMonth
        this.year = year
        this.month = month
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this@activity_apply, this@activity_apply, hour, minute,
            true)
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        hour = hourOfDay
        this.minute = minute
        var month_fin = ""
        var day_fin = ""
        var hour_fin = ""
        var minute_fin = ""

        if((month + 1) < 10){
            month_fin = "0" + (month + 1).toString()
        }

        else{
            month_fin = (month + 1).toString()
        }

        if(day < 10){
            day_fin = "0" + day.toString()
        }

        else{
            day_fin = day.toString()
        }

        if(hour < 10){
            hour_fin = "0" + hour.toString()
        }

        else{
            hour_fin = hour.toString()
        }

        if(minute < 10){
            minute_fin = "0" + minute.toString()
        }

        else{
            minute_fin = minute.toString()
        }

        val year_fin = year.toString()


        date.setText(year_fin + ". " + month_fin + ". " + day_fin + ". " + hour_fin + ":" + minute_fin)
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

    fun showDialog(title : String, contents : String){
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_apply,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle(title)
        dlg.setMessage(contents)
        dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->

        })

        dlg.show()
    }

    fun getUserData(){
        val db : FirebaseFirestore = FirebaseFirestore.getInstance()

        if(Firebase.auth.currentUser != null){
            db.collection("User").document(Firebase.auth.currentUser!!.email.toString()).get().addOnCompleteListener{task: Task<DocumentSnapshot> ->
                if (task.isSuccessful){
                    val document = task.result

                    if(document != null){
                        val adminName = document.get("name").toString()
                        val phone = document.get("phone").toString()
                        val dept = document.get("dept").toString()
                        val studentNo = document.get("studentNo").toString()

                        uploadData(adminName = adminName, dept = dept, phone = phone, studentNo = studentNo)
                    }
                }
            }
        }

        else{
            btn_apply.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
            showDialog(title = "비정상 접근", contents = "비정상적인 접근입니다. 다시 로그인해주십시오.")
        }

    }

    fun uploadData(adminName : String, studentNo : String, dept : String, phone : String){
        val db : FirebaseFirestore = FirebaseFirestore.getInstance()

        val sportsMap = hashMapOf(
            "adminName" to adminName,
            "allPeople" to allPeople_str.toInt(),
            "currentPeople" to curPeople_str.toInt(),
            "date" to date_str,
            "dept" to dept,
            "event" to event_str,
            "limit" to limit_str,
            "location" to location_str,
            "studentNo" to studentNo,
            "phone" to phone,
            "limit" to limit_str,
                "mail" to Firebase.auth.currentUser?.email.toString()
        )

        db.collection("Sports").document(roomName_str).set(sportsMap)
            .addOnCompleteListener{
                val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_apply,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                dlg.setTitle("처리 완료")
                dlg.setMessage("정상 처리되었습니다.")
                dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                    onBackPressed()
                })

                dlg.show()
            }
            .addOnFailureListener{
                btn_apply.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                showDialog(title = "업로드 실패", contents = "업로드하는 중 다음 문제가 발생하였습니다.\n" + it)
            }
    }
}