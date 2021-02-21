package kr.ac.jbnu.coe.ui.more

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.royrodriguez.transitionbutton.TransitionButton
import kr.ac.jbnu.coe.R
import kr.ac.jbnu.coe.ui.alliance.activity_storeMap

class activity_feedbackDetail : AppCompatActivity(){
    lateinit var contents : TextView
    lateinit var author : TextView
    lateinit var type : TextView
    lateinit var category : TextView
    lateinit var title : TextView
    lateinit var date : TextView
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var title_str : String
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