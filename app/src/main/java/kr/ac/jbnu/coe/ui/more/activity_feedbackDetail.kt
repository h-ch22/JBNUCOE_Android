package kr.ac.jbnu.coe.ui.more

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kr.ac.jbnu.coe.R
import kr.ac.jbnu.coe.ui.handWriting.activity_handWriting

class activity_feedbackDetail : AppCompatActivity(){
    lateinit var contents : TextView
    lateinit var author : TextView
    lateinit var type : TextView
    lateinit var category : TextView
    lateinit var title : TextView
    lateinit var date : TextView
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var title_str : String
    lateinit var answerLL : LinearLayout
    lateinit var txt_answer : TextView
    lateinit var txt_answer_author : TextView
    lateinit var field_answer : EditText
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_feedbackdetail)

        toolbar = findViewById(R.id.toolbar)
        date = findViewById(R.id.txt_date)
        contents = findViewById(R.id.txt_contents)
        author = findViewById(R.id.txt_author)
        type = findViewById(R.id.txt_type)
        category = findViewById(R.id.txt_category)
        title = findViewById(R.id.txt_title)
        answerLL = findViewById(R.id.answerLL)
        txt_answer = findViewById(R.id.txt_answer)
        txt_answer_author = findViewById(R.id.txt_answer_author)
        field_answer = findViewById(R.id.field_answer)

        title_str = intent.getStringExtra("feedbackTitle").toString()
        date.text = intent.getStringExtra("dateTime").toString()
        author.text = intent.getStringExtra("author").toString()
        type.text = intent.getStringExtra("type").toString()
        category.text = intent.getStringExtra("category").toString()

        contents.setMovementMethod(ScrollingMovementMethod())

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        title.text = title_str

        getData()
    }

    fun getData(){
        val docRef = db.collection("Feedback").document(title_str)

        docRef.get().addOnCompleteListener{task ->
            if(task.isSuccessful){
                val document = task.result

                if(document != null){
                    contents.text = document.get("Feedback").toString()

                    if(document.get("answer") != null){
                        answerLL.visibility = View.VISIBLE
                        txt_answer.text = document.get("answer").toString()

                        if(document.get("answer_author") != null){
                            txt_answer_author.visibility = View.VISIBLE
                            txt_answer_author.text = "답변 작성자 : " + document.get("answer_author").toString()
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_write, menu)

        return true
    }

    fun getUserInfo(){
        val email = FirebaseAuth.getInstance().currentUser?.email.toString()

        val userRef = db.collection("User").document(email)

        userRef.get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val document = task.result

                if(document != null){
                    val studentNo = document.get("studentNo").toString()

                    val adminRef = db.collection("User").document("Admin")

                    adminRef.get().addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            val doc = task.result

                            if(doc != null){
                                val admin = doc.get(studentNo).toString()

                                upload(admin)
                            }
                        }
                    }
                }
            }
        }
    }

    fun upload(admin : String){
        val docRef = db.collection("Feedback").document(intent.getStringExtra("feedbackTitle").toString())

        docRef.get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val document = task.result

                if(document != null){
                    docRef.update("answer", field_answer.text.toString(),
                                        "answer_author", admin
                    ).addOnSuccessListener {
                        val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_feedbackDetail,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                        dlg.setTitle("업로드 완료")
                        dlg.setMessage("정상 처리되었습니다.")
                        dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                            val intent = Intent(applicationContext, activity_allFeedback::class.java)
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
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_feedbackDetail,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle(title)
        dlg.setMessage(contents)
        dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->

        })

        dlg.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.action_upload -> {
                if(field_answer.text.toString() != ""){
                    val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_feedbackDetail,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                    dlg.setTitle("업로드 확인")
                    dlg.setMessage("업로드 하시겠습니까?")
                    dlg.setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                        getUserInfo()
                    })

                    dlg.setNegativeButton("아니오", {dialog, which ->  })

                    dlg.show()
                }

                else{
                    showDialog(title = "답변 내용 입력", contents = "답변의 내용을 입력하십시오.")
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}