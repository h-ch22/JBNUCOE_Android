package kr.ac.jbnu.coe.ui.sports

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.royrodriguez.transitionbutton.TransitionButton
import kr.ac.jbnu.coe.R

class activity_myRoomDetail : AppCompatActivity(), View.OnClickListener{
    lateinit var title : TextView
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var roomName : String
    lateinit var btn_delete : TransitionButton
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
    lateinit var sportsListView : RecyclerView
    lateinit var sportsListAdapter : mySportsListAdapter
    var sportsList = arrayListOf<mySportsItem>()
    lateinit var btn_cancel : TransitionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_mysportsdetail)

        roomName = intent.getStringExtra("roomName").toString()
        date = intent.getStringExtra("date").toString()
        adminName = intent.getStringExtra("adminName").toString()
        event = intent.getStringExtra("event").toString()
        allPeople = intent.getIntExtra("allPeople", 0)
        currentPeople = intent.getIntExtra("currentPeople", 0)
        location = intent.getStringExtra("location").toString()

        toolbar = findViewById(R.id.toolbar)
        btn_delete = findViewById(R.id.btn_delete)
        txt_contents = findViewById(R.id.txt_contents)
        title = findViewById(R.id.txt_title)
        sportsListView = findViewById(R.id.accountList)
        btn_cancel = findViewById(R.id.btn_cancel)

        getData()
        getExtraData()

        btn_delete.setOnClickListener(this)

        if(intent.getStringExtra("type").equals("admin")){
            btn_delete.visibility = View.VISIBLE
            btn_cancel.visibility = View.GONE
        }

        else{
            btn_delete.visibility = View.GONE
            btn_cancel.visibility = View.VISIBLE
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        title.text = roomName
        btn_cancel.setOnClickListener(this)

    }

    fun getData(){
        val docRef = db.collection("Sports").document(roomName).collection("applies")
        docRef.get().addOnSuccessListener { result ->
            for(document in result){
                val name = document.get("name").toString()
                val dept = document.get("dept").toString() + " " + document.get("studentNo").toString()
                val phone = document.get("phone").toString()

                sportsList.add(mySportsItem(name = name, dept = dept, phone = phone))

                sportsListAdapter = mySportsListAdapter(this, sportsList)

                sportsListView.adapter = sportsListAdapter

                val layoutManager = LinearLayoutManager(this)
                sportsListView.layoutManager = layoutManager
                sportsListView.setHasFixedSize(true)
            }
        }

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
        if (v != null) {
            if (v.id == R.id.btn_delete) {
                val dlg: AlertDialog.Builder = AlertDialog.Builder(
                    this,
                    android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
                )
                dlg.setTitle("제거 확인")
                dlg.setMessage("방 제거 시 모든 데이터가 영구적으로 제거되며, 복구할 수 없습니다.\n계속 하시겠습니까?")
                dlg.setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                    btn_delete.startAnimation()
                    val applyRef = db.collection("Sports").document(roomName).collection("applies")
                    applyRef.get().addOnSuccessListener { result ->
                        for (document in result) {
                            applyRef.document(document.id).delete()
                        }

                        db.collection("Sports").document(roomName).delete().addOnSuccessListener {
                            showDialog(title = "제거 완료", contents = "정상 처리되었습니다.")
                        }.addOnFailureListener {
                            btn_delete.stopAnimation(
                                TransitionButton.StopAnimationStyle.SHAKE,
                                null
                            )
                            showDialog(
                                title = "제거 실패",
                                contents = "제거 중 문제가 발생하였습니다. 네트워크 상태를 확인한 후 다시 시도하십시오.\n오류 코드 : " + it
                            )
                        }
                    }
                })

                dlg.setNegativeButton("아니오", { dialog, which ->

                })

                dlg.show()
            }

            if (v.id == R.id.btn_cancel) {
                Log.d("listener", "clicked")
                val dlg: AlertDialog.Builder = AlertDialog.Builder(
                    this,
                    android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
                )
                dlg.setTitle("취소 확인")
                dlg.setMessage("지원 취소하시겠습니까?")
                dlg.setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                    btn_cancel.startAnimation()

                    db.collection("Sports").document(roomName).collection("applies").document(Firebase.auth.currentUser?.email.toString()).delete().addOnSuccessListener {
                        val userRef = db.collection("Sports").document(roomName)
                        userRef.get().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val document = task.result

                                if (document.exists()) {
                                    val current_old = document.get("currentPeople") as Long

                                    userRef.update("currentPeople", current_old - 1)
                                        .addOnSuccessListener {
                                            showDialog(title = "취소 완료", contents = "정상 처리되었습니다.")
                                        }
                                }
                            }


                        }
                    }.addOnFailureListener{
                        showDialog(
                            title = "취소 실패",
                            contents = "처리 중 문제가 발생하였습니다. 네트워크 상태를 확인한 후 다시 시도하십시오.\n오류 코드 : " + it
                        )
                    }

                })
                dlg.setNegativeButton("아니오", { dialog, which ->
                    btn_cancel.stopAnimation(
                        TransitionButton.StopAnimationStyle.SHAKE,
                        null
                    )

                })

                dlg.show()


            }
        }
    }

    fun showDialog(title : String, contents : String){
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle(title)
        dlg.setMessage(contents)
        dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
            onBackPressed()
        })

        dlg.show()
    }

}