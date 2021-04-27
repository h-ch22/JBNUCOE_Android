package kr.ac.jbnu.coe.ui.more

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kr.ac.jbnu.coe.R

class activity_myFeedbackDetails : AppCompatActivity(){
    lateinit var contents : TextView
    lateinit var author : TextView
    lateinit var type : TextView
    lateinit var category : TextView
    lateinit var title : TextView
    lateinit var date : TextView
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var title_str : String
    lateinit var txt_answer : TextView
    lateinit var answerLL : LinearLayout
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_myfeedbackdetail)

        toolbar = findViewById(R.id.toolbar)
        date = findViewById(R.id.txt_date)
        contents = findViewById(R.id.txt_contents)
        author = findViewById(R.id.txt_author)
        type = findViewById(R.id.txt_type)
        category = findViewById(R.id.txt_category)
        title = findViewById(R.id.txt_title)
        txt_answer = findViewById(R.id.txt_answer)
        answerLL = findViewById(R.id.answerLL)

        title_str = intent.getStringExtra("feedbackTitle").toString()
        date.text = intent.getStringExtra("dateTime").toString()
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

                    if(document.get("answer").toString() != ""){
                        answerLL.visibility = View.VISIBLE
                        txt_answer.text = document.get("answer").toString()

                        if(document.get("answer_author") != ""){
                            author.visibility = View.VISIBLE
                            author.text = "답변 작성자 : " + document.get("answer_author").toString()
                        }
                    }
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