package kr.ac.jbnu.coe.ui.sports

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kr.ac.jbnu.coe.R
import java.text.SimpleDateFormat
import java.util.*

class activity_sportsCheck : AppCompatActivity() {
    val db = Firebase.firestore
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var sportsListView : RecyclerView
    lateinit var sportsListAdapter : sportsListAdapter
    var sportsList = arrayListOf<sportsItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_check)

        toolbar = findViewById(R.id.toolbar)
        sportsListView = findViewById(R.id.sportsList)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        getData()
    }

    fun getData(){
        val docRef = db.collection("Sports")
        docRef.get().addOnSuccessListener { result ->
            for(document in result){
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
                    val intent = Intent(this, activity_sportsDetail::class.java)
                    intent.putExtra("roomName", sportsItem.roomName)
                    intent.putExtra("date", sportsItem.date)
                    intent.putExtra("adminName", sportsItem.adminName)
                    intent.putExtra("event", sportsItem.event)
                    intent.putExtra("allPeople", sportsItem.allPeople)
                    intent.putExtra("currentPeople", sportsItem.currentPeople)
                    intent.putExtra("location", sportsItem.location)
                    intent.putExtra("status", sportsItem.status)

                    startActivity(intent)
                }

                sportsListView.adapter = sportsListAdapter

                val layoutManager = LinearLayoutManager(this)
                sportsListView.layoutManager = layoutManager
                sportsListView.setHasFixedSize(true)
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