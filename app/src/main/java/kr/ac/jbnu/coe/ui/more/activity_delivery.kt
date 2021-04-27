package kr.ac.jbnu.coe.ui.more

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.royrodriguez.transitionbutton.TransitionButton
import kotlinx.android.synthetic.main.layout_apply.*
import kr.ac.jbnu.coe.R
import java.text.SimpleDateFormat
import java.util.*

class activity_delivery : AppCompatActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener{
    lateinit var field_date : EditText
    lateinit var field_waybill : EditText
    lateinit var deliverySpinner : Spinner
    lateinit var field_others : EditText
    lateinit var btn_request : TransitionButton
    lateinit var btn_log : Button
    var type = ""
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    var year = 0
    var month = 0
    var day = 0
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_delivery)

        toolbar = findViewById(R.id.toolbar)
        field_date = findViewById(R.id.field_date)
        field_waybill = findViewById(R.id.field_waybill)
        field_others = findViewById(R.id.field_others)
        deliverySpinner = findViewById(R.id.spinner_delivery)
        btn_request = findViewById(R.id.btn_request)
        btn_log = findViewById(R.id.btn_log)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val items = resources.getStringArray(R.array.company)
        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)

        deliverySpinner?.prompt = "택배사"
        deliverySpinner?.adapter = typeAdapter

        deliverySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                type = deliverySpinner?.selectedItem as String

                if(type.equals("기타 (쿠팡 등)")){
                    field_waybill.setText("조회 불가능한 택배사")
                    field_waybill.inputType = InputType.TYPE_NULL
                }

                else{
                    field_waybill.setText("")
                    field_waybill.inputType = InputType.TYPE_CLASS_TEXT
                }
            }

            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

        }

        field_date.setOnClickListener(this)
        btn_request.setOnClickListener(this)
        btn_log.setOnClickListener(this)
    }

    fun upload(email : String, studentNo : String, phone : String, name : String, dept : String){
        val db : FirebaseFirestore = FirebaseFirestore.getInstance()
        val sdf = SimpleDateFormat("yyyy. MM. dd. HH:mm:ss")
        val current = sdf.format(Date())

        val deliveryMap = hashMapOf(
            "Waybill" to type + " " + field_waybill.text.toString(),
            "date" to field_date.text.toString(),
            "isReceipt" to false,
            "name" to name,
            "dept" to dept,
            "studentNo" to studentNo,
            "phone" to phone,
            "requested" to current.toString(),
            "others" to field_others.text.toString()
        )

        val docRef = db.collection("Delivery").document(email + "_" + current.toString())
        docRef.set(deliveryMap)
            .addOnCompleteListener{
                val dlg: AlertDialog.Builder = AlertDialog.Builder(this,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                dlg.setTitle("업로드 완료")
                dlg.setMessage("정상 처리되었습니다.")
                dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                    btn_request.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND, null)
                    onBackPressed()
                })

                dlg.show()
            }
            .addOnFailureListener{
                btn_apply.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                val dlg: AlertDialog.Builder = AlertDialog.Builder(this,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                dlg.setTitle("업로드 실패")
                dlg.setMessage("업로드하는 중 문제가 발생했습니다.\n네트워크 상태를 확인한 후 다시 시도하십시오.\n에러 코드 : " + it)
                dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                })

                dlg.show()
            }
    }

    fun getUserInfo(){
        val email = FirebaseAuth.getInstance().currentUser?.email.toString()
        val docRef = db.collection("User").document(email).get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val document = task.result

                if(document != null && document.exists()){
                    upload( email = email,
                            studentNo = document.get("studentNo").toString(),
                            phone = document.get("phone").toString(),
                            name = document.get("name").toString(),
                            dept = document.get("dept").toString())
                }
            }
        }
    }

    override fun onClick(v: View?) {
        if(v != null){
            if(v.id == R.id.field_date){
                val calendar: Calendar = Calendar.getInstance()
                day = calendar.get(Calendar.DAY_OF_MONTH)
                month = calendar.get(Calendar.MONTH)
                year = calendar.get(Calendar.YEAR)
                val datePickerDialog =
                    DatePickerDialog(this@activity_delivery, this@activity_delivery, year, month, day)
                datePickerDialog.show()
            }

            if(v.id == R.id.btn_request){
                btn_request.startAnimation()

                if(field_waybill.text.toString() == "" || field_date.text.toString() == ""){
                    btn_request.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)

                    val dlg: AlertDialog.Builder = AlertDialog.Builder(this,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                    dlg.setTitle("공배 필드")
                    dlg.setMessage("모든 요구사항을 충족시켜주세요. : ")
                    dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                    })

                    dlg.show()
                }

                else{
                    getUserInfo()
                }
            }

            if(v.id == R.id.btn_log){
                val intent = Intent(applicationContext, activity_deliveryLog :: class.java)
                startActivity(intent)
                finish()
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

    fun showDialog(title : String, contents : String){
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_delivery,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle(title)
        dlg.setMessage(contents)
        dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
            onBackPressed()
        })

        dlg.show()
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

        field_date.setText(year_fin + ". " + month_fin + ". " + day_fin)

    }
}