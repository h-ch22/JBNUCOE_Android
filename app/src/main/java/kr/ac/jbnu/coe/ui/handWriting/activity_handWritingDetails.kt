package kr.ac.jbnu.coe.ui.handWriting

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kr.ac.jbnu.coe.R
import kr.ac.jbnu.coe.activity_singleImageView

class activity_handWritingDetails : AppCompatActivity(){
    lateinit var title : TextView
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var title_str : String
    lateinit var imgLL : LinearLayout
    lateinit var txt_name : TextView
    lateinit var txt_dateTime : TextView
    lateinit var txt_recommend : TextView
    lateinit var txt_read : TextView
    lateinit var field_examName : TextView
    lateinit var field_date : TextView
    lateinit var field_meter : TextView
    lateinit var field_term : TextView
    lateinit var field_review : TextView
    lateinit var field_howTO : TextView
    lateinit var docId : String
    val db = Firebase.firestore
    val email = FirebaseAuth.getInstance().currentUser?.email.toString()
    lateinit var imageIndex : String
    lateinit var scrollView : HorizontalScrollView
    val storageReference = FirebaseStorage.getInstance().reference
    lateinit var id : String
    lateinit var authorMail : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_handwritingdetails)

        docId = intent.getStringExtra("docId").toString()
        title = findViewById(R.id.txt_title)
        toolbar = findViewById(R.id.toolbar)
        imgLL = findViewById(R.id.imgLL)
        txt_name = findViewById(R.id.name)
        txt_dateTime = findViewById(R.id.dateTime)
        txt_read = findViewById(R.id.txt_read)
        txt_recommend = findViewById(R.id.txt_recommend)
        scrollView = findViewById(R.id.scrollView)
        field_examName = findViewById(R.id.txt_examName)
        field_date = findViewById(R.id.txt_date)
        field_meter = findViewById(R.id.txt_meter)
        field_term = findViewById(R.id.txt_term)
        field_review = findViewById(R.id.txt_review)
        field_howTO = findViewById(R.id.txt_howTO)

        field_meter.setMovementMethod(ScrollingMovementMethod())
        field_review.setMovementMethod(ScrollingMovementMethod())
        field_howTO.setMovementMethod(ScrollingMovementMethod())

        title_str = intent.getStringExtra("handWritingTitle").toString()

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        title.text = title_str

        increaseRead()
        getData()
    }

    private fun getData(){
        val docRef = db.collection("HandWriting").document(docId)

        docRef.get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val document = task.result

                if(document != null){
                    txt_name.text = document.getString("author")
                    txt_dateTime.text = document.getString("Date Time")
                    txt_read.text = document.get("read").toString()
                    txt_recommend.text = document.get("recommend").toString()
                    imageIndex = document.get("imageIndex").toString()
                    id = document.get("id").toString()
                    authorMail = document.get("mail").toString()
                    field_examName.text = document.get("examName").toString()
                    field_date.text = document.get("examDate").toString()
                    field_howTO.text = document.get("howTO").toString()
                    field_meter.text = document.get("meter").toString()
                    field_review.text = document.get("review").toString()
                    field_term.text = document.get("term").toString()

                    if(imageIndex != "0"){
                        downloadImage()
                    }

                    else{
                        scrollView.visibility = View.GONE
                    }
                }
            }
        }
    }

    fun dpToPx(dp: Int, context: Context): Int {
        val density: Float = context.getResources().getDisplayMetrics().density

        return Math.round(dp.toFloat() * density)
    }

    private fun downloadImage(){
        val index : Int = imageIndex.toInt()

        for(i in 0..index - 1){
            val child = "handWriting/"+ authorMail + "_" + id + "/" + i + ".png"
            val downloadURL = storageReference.child(child)

            val imageView = ImageView(this)
            imageView.layoutParams = LinearLayout.LayoutParams(dpToPx(250, applicationContext),dpToPx(250, applicationContext))

            imageView.scaleType = ImageView.ScaleType.FIT_XY

            Glide.with(this).load(downloadURL).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).apply(
                    RequestOptions.bitmapTransform(
                            RoundedCorners(16)
                    )).into(imageView)

            imgLL?.addView(imageView)

            imageView.setOnTouchListener { v, event ->
                when (event?.action){
                    MotionEvent.ACTION_UP -> {
                        val intent = Intent(applicationContext, activity_singleImageView::class.java)
                        intent.putExtra("url", child)
                        startActivity(intent)
                    }
                }

                true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val docRef = db.collection("HandWriting").document(docId)
        var isAuthor = false
        var isAdmin = false

        docRef.get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val document = task.result

                if(document != null){
                    authorMail = document.get("mail").toString()

                    if(authorMail == email){
                        isAuthor = true
                    }
                }
            }

            val adminRef = db.collection("User").document("Admin")
            val userRef = db.collection("User").document(email)

            userRef.get().addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val document = task.result

                    if(document != null){
                        val studentNo = document.get("studentNo").toString()

                        adminRef.get().addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                val document = task.result

                                if(document != null){
                                    if(document.get(studentNo) != null){
                                        isAdmin = true

                                    }
                                }
                            }


                            menuInflater.inflate(R.menu.menu_handwritingdetail, menu)

                            if(isAuthor){
                                menu?.get(1)?.setVisible(false)
                                menu?.get(0)?.setVisible(true)
                                menu?.get(2)?.setVisible(true)

                            }

                            else{
                                menu?.get(1)?.setVisible(true)
                                menu?.get(0)?.setVisible(false)
                                menu?.get(2)?.setVisible(false)
                            }

                            if(isAdmin){
                                menu?.get(1)?.setVisible(true)
                                menu?.get(2)?.setVisible(true)
                                menu?.get(0)?.setVisible(false)
                            }

                            if(isAuthor && isAdmin){
                                menu?.get(1)?.setVisible(false)
                                menu?.get(0)?.setVisible(true)
                                menu?.get(2)?.setVisible(true)
                            }
                        }
                    }
                }
            }
        }

        return true
    }

    private fun increaseRead(){
        val docRef = db.collection("HandWriting").document(docId).collection("read").document(email)
        val readRef = db.collection("HandWriting").document(docId)

        docRef.get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val document = task.result

                if(document.exists()){

                }

                else{
                    val anyData = hashMapOf(
                           "read" to "true"
                    )

                    docRef.set(anyData)
                            .addOnCompleteListener {
                                readRef.get().addOnCompleteListener { task ->
                                    if(task.isSuccessful){
                                        val document = task.result

                                        if(document != null){
                                            val read_new = document.get("read").toString().toInt()

                                            readRef.update("read", read_new + 1)
                                        }
                                    }
                                }
                            }
                }
            }
        }
    }

    fun showDialog(title : String, contents : String){
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_handWritingDetails,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle(title)
        dlg.setMessage(contents)
        dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->

        })

        dlg.show()
    }

    private fun delete(){
        val docRef = db.collection("HandWriting").document(docId)
        val recommendRef = db.collection("HandWriting").document(docId).collection("recommend")
        val readRef = db.collection("HandWriting").document(docId).collection("read")

        val index : Int = imageIndex.toInt()

            recommendRef.get().addOnSuccessListener { result ->
                for(document in result){
                    recommendRef.document(document.id).delete().addOnSuccessListener {
                    }

                }

                readRef.get().addOnSuccessListener { result ->
                    for(document in result){
                        readRef.document(document.id).delete().addOnSuccessListener {

                        }.addOnFailureListener { showDialog(title = "오류", contents = "제거 중 오류가 발생하였습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.\n" + it) }


                    }


                }.addOnFailureListener { showDialog(title = "오류", contents = "제거 중 오류가 발생하였습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.\n" + it) }
            }

            for(i in 0..index - 1){
                val childRef = storageReference.child("handWriting/"+ authorMail + "_" + id + "/" + i + ".png")

                childRef.delete().addOnSuccessListener {
                }.addOnFailureListener { showDialog(title = "오류", contents = "제거 중 오류가 발생하였습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.\n" + it) }
            }

            docRef.delete().addOnSuccessListener { showDialog(title = "제거 완료", contents = "정상 처리되었습니다.") }
                    .addOnFailureListener { e -> Log.e("delete", "Error while delete document", e)
                        showDialog(title = "오류", contents = "제거 중 오류가 발생하였습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.\n" + e)}

    }

    private fun recommend(){
        val docRef = db.collection("HandWriting").document(docId).collection("recommend").document(email)
        val recommendRef = db.collection("HandWriting").document(docId)

        docRef.get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val document = task.result

                if(document.exists()){
                    showDialog(title = "오류", contents = "이미 추천한 글입니다.")
                }

                else{
                    val anyData = hashMapOf(
                            "recommend" to "true"
                    )

                    docRef.set(anyData)
                            .addOnCompleteListener {
                                recommendRef.get().addOnCompleteListener { task ->
                                    if(task.isSuccessful){
                                        val document = task.result

                                        if(document != null){
                                            val recommend_new = document.get("recommend").toString().toInt()

                                            recommendRef.update("recommend", recommend_new + 1)

                                            recommendRef.get().addOnCompleteListener { task ->
                                                if(task.isSuccessful){
                                                    val document = task.result

                                                    if(document != null){
                                                        txt_recommend.text = document.get("recommend").toString()
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

    private fun edit(){
        val intent = Intent(applicationContext, activity_handWriting_edit::class.java)
        intent.putExtra("handWritingTitle", title_str)
        intent.putExtra("handWritingDate", field_date.text.toString())
        intent.putExtra("handWritingName", field_examName.text.toString())
        intent.putExtra("handWritingTerm", field_term.text.toString())
        intent.putExtra("handWritinghowTO", field_howTO.text.toString())
        intent.putExtra("handWritingMeter", field_meter.text.toString())
        intent.putExtra("handWritingReview", field_review.text.toString())
        intent.putExtra("docId", docId)

        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.action_delete -> {
                val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_handWritingDetails,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                dlg.setTitle("삭제 확인")
                dlg.setMessage("게시물 삭제 시 복구가 불가능합니다.\n계속하시겠습니까?")
                dlg.setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                    delete()
                })

                dlg.setNegativeButton("아니오", {dialog, which ->  })

                dlg.show()

            }

            R.id.action_edit -> {
                edit()
            }

            R.id.action_recommend -> {
                recommend()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}