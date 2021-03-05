package kr.ac.jbnu.coe.ui.more

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kr.ac.jbnu.coe.R

class activity_percentage : AppCompatActivity(), View.OnClickListener{
    lateinit var progressBar : CircularProgressBar
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var txt_percentage : TextView
    lateinit var btn_all : Button
    lateinit var btn_communication : Button
    lateinit var btn_welfare : Button
    lateinit var btn_culture : Button
    lateinit var btn_learning : Button
    lateinit var pledgeItem: pledgeItem
    lateinit var pledgeListView : RecyclerView
    lateinit var pledgeListAdapter: pledgeListAdapter
    val db = Firebase.firestore
    var pledgeList = ArrayList<String>()
    var pledgeItemList = ArrayList<pledgeItem>()
    var implementedCnt = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_percentage)

        progressBar = findViewById<CircularProgressBar>(R.id.circularProgressBar)
        toolbar = findViewById(R.id.toolbar)
        txt_percentage = findViewById(R.id.txt_percentage)
        btn_all = findViewById(R.id.btn_all)
        btn_communication = findViewById(R.id.btn_communication)
        btn_welfare = findViewById(R.id.btn_welfare)
        btn_culture = findViewById(R.id.btn_culture)
        btn_learning = findViewById(R.id.btn_learning)
        pledgeListView = findViewById(R.id.pledgeList)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        btn_all.setOnClickListener(this)
        btn_communication.setOnClickListener(this)
        btn_welfare.setOnClickListener(this)
        btn_culture.setOnClickListener(this)
        btn_learning.setOnClickListener(this)

        getPledgeList()
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

    fun getPledgeList(){
        implementedCnt = 0
        pledgeItemList.clear()
        pledgeList.clear()

        db.collection("Pledge").get().addOnSuccessListener { result ->
            lateinit var pledgeMap : MutableMap<String, Any?>

            for (document in result){
                pledgeMap = document.data

                if(pledgeMap != null){
                    for((key, value) in pledgeMap){
                        val value_split = value.toString().split("{implemented=")
                        val value_fin = value_split[1].split("}")

                        pledgeList.add(key)
                        pledgeItemList.add(pledgeItem(title = key, status = value_fin[0]))
                        pledgeListAdapter = pledgeListAdapter(this, pledgeItemList){pledgeItem ->

                        }

                        calculateProgress(pledge = key, implemented = value_fin[0])
                    }
                }
            }

            pledgeListView.adapter = pledgeListAdapter

            val layoutManager = LinearLayoutManager(this)
            pledgeListView.layoutManager = layoutManager
            pledgeListView.setHasFixedSize(true)

        }
    }

    fun calculateProgress(pledge : String, implemented : String){
        if(implemented.equals("true")){
            implementedCnt++
        }

        progressBar.apply{
            setProgressWithAnimation((implementedCnt * 100 / pledgeList.size).toFloat(), 1000)
            Log.d("percentage", ((implementedCnt * 100 / pledgeList.size)).toString())
            Log.d("implemented", implementedCnt.toString())
            Log.d("size", pledgeList.size.toString())
            txt_percentage.text = (implementedCnt * 100 / pledgeList.size).toString() + "%"
            progressMax = 100f

            progressBarWidth = 7f
            backgroundProgressBarWidth = 15f

            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }
    }

    fun getSpecifiedPledgeList(category : String){
        implementedCnt = 0
        pledgeItemList.clear()
        pledgeList.clear()
        pledgeListView.layoutManager = null
        pledgeListView.adapter = null
        pledgeListView.removeAllViewsInLayout()

        val docRef = db.collection("Pledge").document(category)
        docRef.get().addOnCompleteListener{ task ->
            if(task.isSuccessful){
                val document = task.result

                if(document != null){
                    val pledgeMap : MutableMap<String, Any>? = document.data

                    if(pledgeMap != null){
                        for((key, value) in pledgeMap){
                            val value_split = value.toString().split("{implemented=")
                            val value_fin = value_split[1].split("}")

                            pledgeList.add(key)
                            pledgeItemList.add(pledgeItem(title = key, status = value_fin[0]))
                            pledgeListAdapter = pledgeListAdapter(this, pledgeItemList){pledgeItem ->

                            }

                            calculateProgress(pledge = key, implemented = value_fin[0])
                            pledgeListView.adapter = pledgeListAdapter

                            val layoutManager = LinearLayoutManager(this)
                            pledgeListView.layoutManager = layoutManager
                            pledgeListView.setHasFixedSize(true)
                        }
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        if(v != null){
            if(v.id == R.id.btn_all){
                getPledgeList()
            }

            if(v.id == R.id.btn_culture){
                getSpecifiedPledgeList(category = "Culture")
            }

            if(v.id == R.id.btn_communication){
                getSpecifiedPledgeList(category = "Communication")
            }

            if(v.id == R.id.btn_learning){
                getSpecifiedPledgeList(category = "Learning")
            }

            if(v.id == R.id.btn_welfare){
                getSpecifiedPledgeList(category = "Welfare")
            }
        }
    }
}