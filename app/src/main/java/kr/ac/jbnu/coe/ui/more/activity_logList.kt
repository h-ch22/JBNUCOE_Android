package kr.ac.jbnu.coe.ui.more

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kr.ac.jbnu.coe.R
import kr.ac.jbnu.coe.ui.notifications.activity_noticeDetail
import kr.ac.jbnu.coe.ui.notifications.noticeItem
import kr.ac.jbnu.coe.ui.notifications.noticeListAdapter
import java.time.format.DateTimeFormatter
import java.util.ArrayList
import android.util.Log

class activity_logList : AppCompatActivity(){
    val db = Firebase.firestore
    lateinit var studentNo : String
    lateinit var logListAdapter : logAdapter
    var logList = ArrayList<logItem>()
    lateinit var logListView : RecyclerView
    lateinit var toolbar : androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_productlog)

        toolbar = findViewById(R.id.toolbar)
        logListView = findViewById(R.id.loglist)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        getUserData()
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

    fun getUserData(){
        val docRef = db.collection("User").document(Firebase.auth.currentUser?.email.toString())
        docRef.get().addOnCompleteListener{task ->
            if(task.isSuccessful){
                val document = task.result

                if(document != null){
                    studentNo = document.get("studentNo").toString()
                    getData()
                }
            }
        }
    }

    fun getData(){
        val batteryRef = db.collection("Products").document("battery").collection("Log")
        val calcRef = db.collection("Products").document("calculator").collection("Log")
        val labcoatRef = db.collection("Products").document("labcoat").collection("Log")
        val umbrellaRef = db.collection("Products").document("umbrella").collection("Log")
        val slipperRef = db.collection("Products").document("slipper").collection("Log")
        val uniformRef = db.collection("Products").document("uniform").collection("Log")

        batteryRef.get().addOnSuccessListener { result ->
            for(document in result){
                val studentNo_doc = document.get("studentNo").toString()

                Log.d("studentNo_doc", studentNo_doc)
                Log.d("studentNo", studentNo)

                if(studentNo_doc.equals(studentNo)){
                    logList.add(logItem(productName = "보조 배터리", date = document.id, returned = document.get("returned") as Boolean, num = document.get("number") as String))
                }
            }

            calcRef.get().addOnSuccessListener { result ->
                for (document in result){
                    val studentNo_doc = document.get("studentNo").toString()

                    if(studentNo_doc.equals(studentNo)){
                        logList.add(logItem(productName = "공학용 계산기", date = document.id, returned = document.get("returned") as Boolean, num = document.get("number") as String))
                    }
                }

                labcoatRef.get().addOnSuccessListener { result ->
                    for(document in result){
                        val studentNo_doc = document.get("studentNo").toString()

                        if(studentNo_doc.equals(studentNo)){
                            logList.add(logItem(productName = "실험복", date = document.id, returned = document.get("returned") as Boolean, num = document.get("number") as String))
                        }

                    }

                    umbrellaRef.get().addOnSuccessListener { result ->
                        for (document in result) {
                            val studentNo_doc = document.get("studentNo").toString()

                            if (studentNo_doc.equals(studentNo)) {
                                logList.add(
                                        logItem(
                                                productName = "우산",
                                                date = document.id,
                                                returned = document.get("returned") as Boolean,
                                                num = document.get("number") as String
                                        )
                                )
                            }
                        }

                        slipperRef.get().addOnSuccessListener { result ->
                            for (document in result) {
                                val studentNo_doc = document.get("studentNo").toString()

                                if (studentNo_doc.equals(studentNo)) {
                                    logList.add(
                                            logItem(
                                                    productName = "슬리퍼",
                                                    date = document.id,
                                                    returned = document.get("returned") as Boolean,
                                                    num = document.get("number") as String
                                            )
                                    )
                                }
                            }

                            uniformRef.get().addOnSuccessListener { result ->
                                for (document in result) {
                                    val studentNo_doc = document.get("studentNo").toString()

                                    if (studentNo_doc.equals(studentNo)) {
                                        logList.add(
                                                logItem(
                                                        productName = "유니폼",
                                                        date = document.id,
                                                        returned = document.get("returned") as Boolean,
                                                        num = document.get("number") as String
                                                )
                                        )
                                    }
                                }

                                logList.sortByDescending { it.date }

                                logListAdapter = logAdapter(applicationContext, logList)


                                logListView.adapter = logListAdapter

                                val layoutManager = LinearLayoutManager(applicationContext)
                                logListView.layoutManager = layoutManager
                                logListView.setHasFixedSize(true)
                            }
                        }
                    }
                }
            }
        }
    }
}