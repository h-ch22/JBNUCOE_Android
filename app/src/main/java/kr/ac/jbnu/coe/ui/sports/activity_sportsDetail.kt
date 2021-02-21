package kr.ac.jbnu.coe.ui.sports

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.royrodriguez.transitionbutton.TransitionButton
import kr.ac.jbnu.coe.R
import kr.ac.jbnu.coe.UserManagement.activity_signIn
import kr.ac.jbnu.coe.ui.alliance.activity_storeMap

class activity_sportsDetail : AppCompatActivity(), View.OnClickListener{
    lateinit var title : TextView
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var roomName : String
    lateinit var btn_apply : TransitionButton
    lateinit var txt_contents : TextView
    lateinit var date : String
    lateinit var adminName : String
    lateinit var event : String
     var allPeople = 0
     var currentPeople = 0
    lateinit var location : String
    lateinit var status : String
    lateinit var limit : String
    lateinit var contact : String
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_sportsdetail)

        roomName = intent.getStringExtra("roomName").toString()
        date = intent.getStringExtra("date").toString()
        adminName = intent.getStringExtra("adminName").toString()
        event = intent.getStringExtra("event").toString()
        allPeople = intent.getIntExtra("allPeople", 0)
        currentPeople = intent.getIntExtra("currentPeople", 0)
        location = intent.getStringExtra("location").toString()
        status = intent.getStringExtra("status").toString()

        toolbar = findViewById(R.id.toolbar)
        btn_apply = findViewById(R.id.btn_apply)
        txt_contents = findViewById(R.id.txt_contents)
        title = findViewById(R.id.txt_title)

        if(status.equals("지원 가능")){
            btn_apply.visibility = View.VISIBLE
        }

        else{
            btn_apply.visibility = View.INVISIBLE
        }

        getExtraData()

        btn_apply.setOnClickListener(this)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        title.text = roomName
    }

    fun getExtraData(){
        val docRef = db.collection("Sports").document(roomName)

        docRef.get().addOnCompleteListener{task ->
            if(task.isSuccessful){
                val document = task.getResult()

                if(document != null){
                    limit = document.get("limit").toString()
                    contact = document.get("phone").toString()

                    txt_contents.setText(
                            "관리자 성명 : " + adminName + "\n" +
                                    "관리자 학과 : " + document.get("dept") + " " + document.get("studentNo") + "\n" +
                                    "모집 인원 : " + allPeople.toString() + "\n" +
                                    "현재 인원 : " + currentPeople.toString() + "\n" +
                                    "종목 : " + event + "\n" +
                                    "장소 : " + location + "\n" +
                                    "날짜 및 시간 : " + date + "\n" +
                                    "제한 및 우대 사항 : " + limit + "\n" +
                                    "대표자 연락처 : " + contact + "\n"
                    )
                }
            }
        }
    }

    fun showDialog(title : String, contents : String){
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_sportsDetail,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle(title) //제목
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
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        if (v!=null){
            if(v.id == R.id.btn_apply){
                val dlg: AlertDialog.Builder = AlertDialog.Builder(this,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                dlg.setTitle("지원 확인")
                dlg.setMessage("지원하시겠습니까?")
                dlg.setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                    btn_apply.startAnimation()
                    var current_new = currentPeople

                    val docRef = db.collection("Sports").document(roomName)
                            .get().addOnCompleteListener{task ->
                                if(task.isSuccessful){
                                    val document = task.getResult()

                                    if(document != null){
                                        current_new = document.get("currentPeople").toString().toInt()

                                        if(current_new >= allPeople){
                                            showDialog(title  = "인원 초과",
                                                    contents = "제한 인원을 초과하였습니다.")

                                            btn_apply.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                                            btn_apply.visibility = View.INVISIBLE
                                        }

                                        else{
                                            var name = ""

                                            if(Firebase.auth.currentUser != null){
                                                db.collection("User").document(Firebase.auth.currentUser!!.email.toString()).get().addOnCompleteListener{task: Task<DocumentSnapshot> ->
                                                    if (task.isSuccessful){
                                                        val document = task.result

                                                        if(document != null){
                                                            name = document.getString("name").toString()

                                                            if(name.equals(adminName)){
                                                                showDialog(title = "관리자 오류", contents = "해당 방의 관리자는 용병에 지원할 수 없습니다.")
                                                                btn_apply.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                                                            }

                                                            else{
                                                                val applyRef = db.collection("Sports").document(roomName)
                                                                        .collection("applies").document(Firebase.auth.currentUser!!.email.toString())

                                                                applyRef.get().addOnCompleteListener{task ->
                                                                    if(task.isSuccessful){
                                                                        val document = task.getResult()

                                                                        if(document.exists()){
                                                                            showDialog(title = "기지원자", contents = "기지원자는 해당 방에 다시 지원할 수 없습니다.")
                                                                            btn_apply.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                                                                        }

                                                                        else{
                                                                            var dept = ""
                                                                            var phone = ""
                                                                            var studentNo = ""

                                                                            val userRef = db.collection("User").document(Firebase.auth.currentUser!!.email.toString())
                                                                            userRef.get().addOnCompleteListener{task ->
                                                                                if(task.isSuccessful){
                                                                                    val document = task.getResult()

                                                                                    if(document != null){
                                                                                        dept = document.get("dept").toString()
                                                                                        studentNo = document.get("studentNo").toString()
                                                                                        phone = document.get("phone").toString()

                                                                                        val user = hashMapOf(
                                                                                                "name" to name,
                                                                                                "dept" to dept,
                                                                                                "phone" to phone,
                                                                                                "studentNo" to studentNo
                                                                                        )

                                                                                        applyRef.set(user).addOnCompleteListener{
                                                                                            var current_final = current_new

                                                                                            val updateRef = db.collection("Sports").document(roomName)
                                                                                            updateRef.get().addOnCompleteListener{task ->
                                                                                                if(task.isSuccessful){
                                                                                                    val document = task.getResult()

                                                                                                    if(document != null){
                                                                                                        updateRef.update("currentPeople", current_new + 1).addOnSuccessListener{
                                                                                                            val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_sportsDetail,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                                                                                                            dlg.setTitle("지원 완료") //제목
                                                                                                            dlg.setMessage("정상 처리되었습니다.")
                                                                                                            dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                                                                                                                val intent = Intent(this, activity_sportsCheck :: class.java)
                                                                                                                startActivity(intent)
                                                                                                                finish()
                                                                                                            })

                                                                                                            dlg.show()
                                                                                                        }

                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }.addOnFailureListener{
                                                                                            showDialog(title = "지원 실패", contents = "지원 처리 중 오류가 발생하였습니다.\n네트워크 상태를 확인한 후 다시 시도하십시오.\n에러 코드 : " + it)
                                                                                            btn_apply.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            else{
                                                btn_apply.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                                                showDialog(title = "로그인 오류", contents = "비정상 접근입니다.")
                                                val intent = Intent(this, activity_signIn::class.java)
                                                startActivity(intent)
                                                finish()
                                            }
                                        }
                                    }
                                }
                            }
                })

                dlg.setNegativeButton("아니오", {dialog, which ->

                })

                dlg.show()
            }
        }
    }

}