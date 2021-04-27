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

class activity_myFeedback : AppCompatActivity(){
    val db = Firebase.firestore
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var feedbackListView : RecyclerView
    lateinit var feedbackListAdapter : myFeedbackListAdapter
    var feedbackList = arrayListOf<myFeedbackItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_myfeedback)

        toolbar = findViewById(R.id.toolbar)
        feedbackListView = findViewById(R.id.feedbackList)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        getUserData()
    }

    fun getUserData(){
        val docRef = db.collection("User").document(FirebaseAuth.getInstance().currentUser?.email.toString())

        docRef.get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val document = task.result

                if(document != null){
                    val studentNo = document.get("studentNo").toString()

                    getData(studentNo)
                }
            }
        }
    }

    fun getData(studentNo : String){
        val docRef = db.collection("Feedback")
        docRef.get().addOnSuccessListener { result ->
            for(document in result){
                val dateTime = document.get("Date Time").toString()
                val author = document.get("author").toString()
                val type = document.get("Type").toString()
                val category = document.get("Category").toString()
                val title = document.id.toString()
                var status = ""

                if(document.get("answer") != null){
                    status = "true"
                }

                else{
                    status = "false"
                }

                if(author.contains(studentNo)){
                    feedbackList.add(
                            myFeedbackItem(feedbackTitle = title, date = dateTime, type = type, category = category, status = status)
                    )
                }

            }

            feedbackList.sortByDescending { it.date }

            feedbackListAdapter = myFeedbackListAdapter(this, feedbackList){
                feedbackItem ->
                val intent = Intent(this, activity_myFeedbackDetails::class.java)
                intent.putExtra("dateTime", feedbackItem.date)
                intent.putExtra("feedbackTitle", feedbackItem.feedbackTitle)
                intent.putExtra("category", feedbackItem.category)
                intent.putExtra("type", feedbackItem.type)

                startActivity(intent)
            }

            feedbackListView.adapter = feedbackListAdapter

            val layoutManager = LinearLayoutManager(this)
            feedbackListView.layoutManager = layoutManager
            feedbackListView.setHasFixedSize(true)
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