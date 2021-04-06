package kr.ac.jbnu.coe.ui.handWriting

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.*
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.royrodriguez.transitionbutton.TransitionButton
import kr.ac.jbnu.coe.R
import java.text.SimpleDateFormat
import java.util.*

class activity_handWriting_write : AppCompatActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener{
    val db = Firebase.firestore
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var txt_title : EditText
    lateinit var txt_contents : EditText
    lateinit var btn_camera : ImageButton
    lateinit var field_name : EditText
    lateinit var field_term : EditText
    lateinit var field_date : EditText
    lateinit var field_howTO : EditText
    lateinit var field_meter : EditText
    lateinit var imageLL : LinearLayout
    val Gallery = 0
    var uriArray = arrayListOf<Uri>()
    var uploaded = false
    lateinit var mail : String
    lateinit var studentNo : String
    lateinit var name : String
    lateinit var dept : String
    lateinit var phone : String
    val storageReference = FirebaseStorage.getInstance().reference
    val id = java.util.UUID.randomUUID().toString()
    lateinit var numOfTxt : TextView
    var year = 0
    var month = 0
    var day = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_writehandwrite)

        txt_title = findViewById(R.id.field_title)
        txt_contents = findViewById(R.id.field_contents)
        field_name = findViewById(R.id.field_name)
        field_term = findViewById(R.id.field_term)
        field_date = findViewById(R.id.field_date)
        field_howTO = findViewById(R.id.field_howto)
        field_meter = findViewById(R.id.field_meter)
        btn_camera = findViewById(R.id.btn_camera)
        imageLL = findViewById(R.id.imageLL)
        toolbar = findViewById(R.id.toolbar)

        field_date.setOnClickListener(this)
        btn_camera.setOnClickListener(this)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        if(getSharedPreferences("handWriting", Activity.MODE_PRIVATE) != null){
            val title_shared = getSharedPreferences("handWriting", Activity.MODE_PRIVATE)
            val title_editor = title_shared.edit()

            if (title_shared.getString("title", null) != null || title_shared.getString("contents", null) != null){
                val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_handWriting_write,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                dlg.setTitle("임시 저장")
                dlg.setMessage("임시 저장된 글이 있습니다.\n복구하시겠습니까?")
                dlg.setPositiveButton("복구", DialogInterface.OnClickListener { dialog, which ->
                    txt_title.setText(title_shared.getString("title", null))
                    txt_contents.setText(title_shared.getString("contents", null))
                    field_howTO.setText(title_shared.getString("howTO", null))
                    field_date.setText(title_shared.getString("examDate", null))
                    field_meter.setText(title_shared.getString("meter", null))
                    field_name.setText(title_shared.getString("name", null))
                    field_term.setText(title_shared.getString("term", null))

                    title_editor.clear()
                    title_editor.apply()
                })

                dlg.setNegativeButton("제거", {dialog, which ->
                    title_editor.clear()
                    title_editor.commit()
                })

                dlg.show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == Gallery){
            if(resultCode == Activity.RESULT_OK){
                if(data?.clipData != null){
                    val clipData : ClipData = data.clipData!!

                    if (clipData.itemCount > 10){
                        Toast.makeText(applicationContext, "이미지는 최대 10장까지 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                    }

                    for(i in 0..clipData.itemCount-1){
                        val item : ClipData.Item = clipData.getItemAt(i)
                        val uri : Uri = item.uri

                        uriArray.add(uri)

                        val imageView = ImageView(this)
                        imageView.layoutParams = LinearLayout.LayoutParams(dpToPx(100, applicationContext),dpToPx(100, applicationContext))
                        imageView.scaleType = ImageView.ScaleType.FIT_XY
                        imageView.setImageURI(uriArray[i])
                        imageLL?.addView(imageView)
                    }

                }

                else if(data?.data != null){
                    uriArray.add(data?.data!!)

                    val imageView = ImageView(this)
                    imageView.layoutParams = LinearLayout.LayoutParams(dpToPx(100, applicationContext),dpToPx(100, applicationContext))
                    imageView.scaleType = ImageView.ScaleType.FIT_XY
                    imageView.setImageURI(uriArray[0])
                    imageLL?.addView(imageView)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_write, menu)

        return true
    }

    override fun onDestroy() {
        super.onDestroy()

        if(!uploaded && txt_title.text.toString() != "" || txt_contents.text.toString() != "" || field_date.text.toString() != "" || field_howTO.text.toString() != "" || field_meter.text.toString() != "" || field_name.text.toString() != "" || field_term.text.toString() != ""){
            val title_shared = getSharedPreferences("handWriting", Activity.MODE_PRIVATE)
            val title_editor = title_shared.edit()

            title_editor.putString("title", txt_title.text.toString())
            title_editor.putString("contents", txt_contents.text.toString())
            title_editor.putString("examDate", field_date.text.toString())
            title_editor.putString("howTO", field_howTO.text.toString())
            title_editor.putString("meter", field_meter.text.toString())
            title_editor.putString("name", field_name.text.toString())
            title_editor.putString("term", field_term.text.toString())

            title_editor.apply()
        }
    }

    private fun upload(){
        val title = txt_title.text.toString()
        val contents = txt_contents.text.toString()
        val examName = field_name.text.toString()
        val examDate = field_date.text.toString()
        val howTO = field_howTO.text.toString()
        val term = field_term.text.toString()
        val meter = field_meter.text.toString()
        val studentNo_short : String = studentNo.get(2).toString() + studentNo.get(3).toString()
        val name_no = name.length
        val name_hidden = name.get(0).toString()
        var hiddenChar : String = ""

        for(i in 0..name_no-2){
            hiddenChar = hiddenChar + "*"
        }

        if(title == "" || contents == "" || examDate == "" || examName == "" || howTO == "" || term == "" || meter == ""){
            showDialog(title = "공백 필드", contents = "제목 및 내용을 입력하십시오.")
        }

        else{
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val current = sdf.format(Date())
            var size = 0

            if(uriArray.isEmpty()){
                size = 0
            }

            else{
                size = uriArray.size
            }

            val handWrite = hashMapOf(
                    "examDate" to examDate,
                    "examName" to examName,
                    "howTO" to howTO,
                    "meter" to meter,
                    "review" to contents,
                    "term" to term,
                    "author_full" to dept + " " + studentNo + " " + name,
                    "author" to dept + " " + studentNo_short + " " + name_hidden + hiddenChar,
                    "imageIndex" to size,
                    "mail" to mail,
                    "phone" to phone,
                    "read" to 0,
                    "recommend" to 0,
                    "title" to title,
                    "Date Time" to current.toString(),
                    "id" to id
            )

            db.collection("HandWriting").document().set(handWrite)
                    .addOnCompleteListener{
                        if(!uriArray.isEmpty()){
                            uploadImage()
                        }

                        else{
                            val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_handWriting_write,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                            dlg.setTitle("업로드 완료")
                            dlg.setMessage("정상 처리되었습니다.")
                            dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                                uploaded = true
                                val intent = Intent(applicationContext, activity_handWriting::class.java)
                                startActivity(intent)
                                finish()
                            })

                            dlg.show()
                        }
                    }
                    .addOnFailureListener{
                        showDialog(title = "업로드 실패", contents = "업로드하는 중 문제가 발생하였습니다.\n네트워크 상태를 확인한 후 다시 시도하십시오.\n" + it)
                    }
        }
    }

    private fun uploadImage(){
        for(i in 0..uriArray.size - 1){
            val profileRef = storageReference.child("handWriting/" + mail + "_" + id + "/" + i.toString() + ".png")
            val file = uriArray.get(i)

            val upload = file?.let { profileRef.putFile(it) }

            if (upload != null) {
                upload.addOnFailureListener{
                    showDialog(title = "업로드 실패", contents = "프로필 이미지 업로드 중 오류가 발생했습니다.\n네트워크 상태를 확인한 후 다시 시도하십시오.\n" + it)
                }.addOnSuccessListener { taskSnapshot ->
                    val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_handWriting_write,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                    dlg.setTitle("업로드 완료")
                    dlg.setMessage("정상 처리되었습니다.")
                    dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                        uploaded = true
                        val intent = Intent(applicationContext, activity_handWriting::class.java)
                        startActivity(intent)
                        finish()
                    })

                    dlg.show()
                }
            }
        }
    }

    private fun getUserInfo(){
        mail = FirebaseAuth.getInstance().currentUser?.email.toString()

        val docRef = db.collection("User").document(mail)

        docRef.get().addOnCompleteListener{task ->
            if(task.isSuccessful){
                val document = task.result

                if(document != null){
                    phone = document.get("phone").toString()
                    name = document.get("name").toString()
                    dept = document.get("dept").toString()
                    studentNo = document.get("studentNo").toString()

                    upload()
                }
            }
        }
    }

    fun dpToPx(dp: Int, context: Context): Int {
        val density: Float = context.getResources().getDisplayMetrics().density

        return Math.round(dp.toFloat() * density)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.action_upload -> {
                val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_handWriting_write,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                dlg.setTitle("업로드 확인")
                dlg.setMessage("업로드 하시겠습니까?")
                dlg.setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                    getUserInfo()
                })

                dlg.setNegativeButton("아니오", {dialog, which ->  })

                dlg.show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun showDialog(title : String, contents : String){
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_handWriting_write,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle(title)
        dlg.setMessage(contents)
        dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->

        })

        dlg.show()
    }

    override fun onClick(v: View?) {
        if (v != null){
            if(v.id == R.id.btn_camera){
                imageLL?.removeAllViewsInLayout()

                if(!uriArray.isEmpty()){
                    uriArray.clear()
                }

                getImage()
            }

            if(v.id == R.id.field_date){
                val calendar: Calendar = Calendar.getInstance()
                day = calendar.get(Calendar.DAY_OF_MONTH)
                month = calendar.get(Calendar.MONTH)
                year = calendar.get(Calendar.YEAR)
                val datePickerDialog =
                        DatePickerDialog(this@activity_handWriting_write, this@activity_handWriting_write, year, month, day)
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

    fun getImage(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

        startActivityForResult(Intent.createChooser(intent, "이미지 로드"), Gallery)
    }
}