package kr.ac.jbnu.coe.ui.handWriting

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kr.ac.jbnu.coe.R
import java.util.*

class activity_handWriting_edit : AppCompatActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener{
    val db = Firebase.firestore
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var txt_title : EditText
    lateinit var txt_contents : EditText
    lateinit var field_name : EditText
    lateinit var field_term : EditText
    lateinit var field_date : EditText
    lateinit var field_howTO : EditText
    lateinit var field_meter : EditText
    lateinit var docId : String
    var year = 0
    var month = 0
    var day = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_handwritingedit)

        txt_title = findViewById(R.id.field_title)
        txt_contents = findViewById(R.id.field_contents)
        field_name = findViewById(R.id.field_name)
        field_term = findViewById(R.id.field_term)
        field_date = findViewById(R.id.field_date)
        field_howTO = findViewById(R.id.field_howto)
        field_meter = findViewById(R.id.field_meter)
        toolbar = findViewById(R.id.toolbar)

        txt_title.setText(intent.getStringExtra("handWritingTitle").toString())
        txt_contents.setText(intent.getStringExtra("handWritingReview").toString())
        field_date.setText(intent.getStringExtra("handWritingDate").toString())
        field_name.setText(intent.getStringExtra("handWritingName").toString())
        field_term.setText(intent.getStringExtra("handWritingTerm").toString())
        field_meter.setText(intent.getStringExtra("handWritingMeter").toString())
        field_howTO.setText(intent.getStringExtra("handWritinghowTO").toString())

        docId = intent.getStringExtra("docId").toString()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.action_upload -> {
                val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_handWriting_edit,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                dlg.setTitle("수정 확인")
                dlg.setMessage("업로드하시겠습니까?")
                dlg.setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                    upload()
                })

                dlg.setNegativeButton("아니오", {dialog, which ->  })

                dlg.show()

            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun upload(){
        val docRef = db.collection("HandWriting").document(docId)

        docRef.get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val document = task.result

                if(document != null){
                    docRef.update("review", txt_contents.text.toString(),
                            "examDate", field_date.text.toString(),
                            "title", txt_title.text.toString(),
                            "howTO", field_howTO.text.toString(),
                            "meter", field_meter.text.toString(),
                            "term", field_term.text.toString(),
                            "examName", field_name.text.toString()
                    ).addOnSuccessListener {
                        val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_handWriting_edit,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                        dlg.setTitle("업로드 완료")
                        dlg.setMessage("정상 처리되었습니다.")
                        dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                            val intent = Intent(applicationContext, activity_handWriting::class.java)
                            startActivity(intent)
                            finish()
                        })

                        dlg.show()
                    }
                            .addOnFailureListener{e -> Log.e("error", "error while uploading", e)
                                                showDialog(title = "오류", contents = "업로드 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.\n" + e)
                            }
                }
            }
        }
    }

    fun showDialog(title : String, contents : String){
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_handWriting_edit,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle(title)
        dlg.setMessage(contents)
        dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->

        })

        dlg.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_write, menu)

        return true
    }

    override fun onClick(v: View?) {
        if(v != null){
            if(v.id == R.id.field_date){
                val calendar: Calendar = Calendar.getInstance()
                day = calendar.get(Calendar.DAY_OF_MONTH)
                month = calendar.get(Calendar.MONTH)
                year = calendar.get(Calendar.YEAR)
                val datePickerDialog =
                        DatePickerDialog(this@activity_handWriting_edit, this@activity_handWriting_edit, year, month, day)
                datePickerDialog.show()
            }
        }

        }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        day = dayOfMonth
        this.year = year
        this.month = month
        var month_fin = ""
        var day_fin = ""

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

        val year_fin = year.toString()

        field_date.setText(year_fin + ". " + month_fin + ". " + day_fin + ". ")

    }
    }