package kr.ac.jbnu.coe.ui.more

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kr.ac.jbnu.coe.R

class activity_deliveryLog : AppCompatActivity(){
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var deliveryListView : RecyclerView
    lateinit var deliveryListAdapter : deliveryListAdapter
    var deliveryList = arrayListOf<deliveryItem>()
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_deliverylog)

        toolbar = findViewById(R.id.toolbar)
        deliveryListView = findViewById(R.id.deliveryLL)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        getData()
    }

    fun getData(){
        val docRef = db.collection("Delivery")

        docRef.get().addOnSuccessListener { result ->
            for(document in result){
                if(document.id.contains(FirebaseAuth.getInstance().currentUser?.email.toString())){
                    val date = document.get("requested").toString()
                    val status = document.get("isReceipt").toString()
                    val waybill = document.get("Waybill").toString()

                    deliveryList.add(deliveryItem(date = date, title = waybill , status = status))
                }
            }

            deliveryList.sortByDescending { it.date }

            deliveryListAdapter = deliveryListAdapter(this, deliveryList)

            deliveryListView.adapter = deliveryListAdapter

            val layoutManager = LinearLayoutManager(this)
            deliveryListView.layoutManager = layoutManager
            deliveryListView.setHasFixedSize(true)
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