package kr.ac.jbnu.coe.ui.sports

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kr.ac.jbnu.coe.R
import java.text.SimpleDateFormat
import java.util.*

class activity_getMyRoom : AppCompatActivity(){
    val db = Firebase.firestore
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var sportsListView : RecyclerView
    lateinit var sportsListAdapter : sportsListAdapter
    var sportsList = arrayListOf<sportsItem>()
    lateinit var title : TextView
    var type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_check)

        toolbar = findViewById(R.id.toolbar)
        sportsListView = findViewById(R.id.sportsList)
        title = findViewById(R.id.txt_title)
        type = intent.getStringExtra("type").toString()

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        if(type.equals("user")){
            getApplyData()
            title.text = "지원한 방 목록"
        }

        if(type.equals("admin")){
            getData()
            title.text = "관리 중인 방 목록"
        }
    }

    fun getApplyData(){
        val docRef = db.collection("Sports")
        docRef.get().addOnSuccessListener { result ->
            for(document in result){
                val applyRef = db.collection("Sports").document(document.id).collection("applies")

                applyRef.get().addOnSuccessListener { result ->
                    for(applyDoc in result){
                        if(applyDoc.id.equals(Firebase.auth.currentUser?.email.toString())){
                            val dateTime = document.get("date").toString()
                            val current_str = document.get("currentPeople").toString()
                            val max_str = document.get("allPeople").toString()
                            val current = current_str.toInt()
                            val max = max_str.toInt()
                            var status = ""

                            if(current >= max){
                                status = "지원 불가"
                            }

                            else{
                                val sdf = SimpleDateFormat("yyyy. MM. dd. HH:mm")
                                val today = sdf.format(Date())
                                println(dateTime)
                                println(today)
                                println(sdf.parse(dateTime))
                                if(sdf.parse(today).before(sdf.parse(dateTime))){
                                    status = "지원 가능"
                                }

                                else{
                                    status = "지원 불가"
                                }
                            }

                            sportsList.add(sportsItem(roomName = document.id,
                                date = document.get("date").toString(),
                                allPeople = max, currentPeople = current, status = status, location = document.get("location").toString(),
                                event = document.get("event").toString(), adminName = document.get("adminName").toString()))

                            sportsListAdapter = sportsListAdapter(this, sportsList){
                                    sportsItem ->
                                val intent = Intent(this, activity_myRoomDetail::class.java)
                                intent.putExtra("roomName", sportsItem.roomName)
                                intent.putExtra("date", sportsItem.date)
                                intent.putExtra("adminName", sportsItem.adminName)
                                intent.putExtra("event", sportsItem.event)
                                intent.putExtra("allPeople", sportsItem.allPeople)
                                intent.putExtra("currentPeople", sportsItem.currentPeople)
                                intent.putExtra("location", sportsItem.location)
                                intent.putExtra("status", sportsItem.status)
                                intent.putExtra("type", "user")
                                startActivity(intent)
                            }

                            sportsListView.adapter = sportsListAdapter

                            val layoutManager = LinearLayoutManager(this)
                            sportsListView.layoutManager = layoutManager
                            sportsListView.setHasFixedSize(true)
                        }
                    }
                }

            }
        }
    }

    fun getData(){
        val docRef = db.collection("Sports")
        docRef.get().addOnSuccessListener { result ->
            for(document in result){
                if(document.get("mail").toString().equals(Firebase.auth.currentUser?.email.toString())){
                    val dateTime = document.get("date").toString()
                    val current_str = document.get("currentPeople").toString()
                    val max_str = document.get("allPeople").toString()
                    val current = current_str.toInt()
                    val max = max_str.toInt()
                    var status = ""

                    if(current >= max){
                        status = "지원 불가"
                    }

                    else{
                        val sdf = SimpleDateFormat("yyyy. MM. dd. HH:mm")
                        val today = sdf.format(Date())
                        println(dateTime)
                        println(today)
                        println(sdf.parse(dateTime))
                        if(sdf.parse(today).before(sdf.parse(dateTime))){
                            status = "지원 가능"
                        }

                        else{
                            status = "지원 불가"
                        }
                    }

                    sportsList.add(sportsItem(roomName = document.id,
                            date = document.get("date").toString(),
                            allPeople = max, currentPeople = current, status = status, location = document.get("location").toString(),
                            event = document.get("event").toString(), adminName = document.get("adminName").toString()))

                    sportsListAdapter = sportsListAdapter(this, sportsList){
                        sportsItem ->
                        val intent = Intent(this, activity_myRoomDetail::class.java)
                        intent.putExtra("roomName", sportsItem.roomName)
                        intent.putExtra("date", sportsItem.date)
                        intent.putExtra("adminName", sportsItem.adminName)
                        intent.putExtra("event", sportsItem.event)
                        intent.putExtra("allPeople", sportsItem.allPeople)
                        intent.putExtra("currentPeople", sportsItem.currentPeople)
                        intent.putExtra("location", sportsItem.location)
                        intent.putExtra("status", sportsItem.status)
                        intent.putExtra("type", "admin")

                        startActivity(intent)
                    }

                    sportsListView.adapter = sportsListAdapter

                    val layoutManager = LinearLayoutManager(this)
                    sportsListView.layoutManager = layoutManager
                    sportsListView.setHasFixedSize(true)
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
}