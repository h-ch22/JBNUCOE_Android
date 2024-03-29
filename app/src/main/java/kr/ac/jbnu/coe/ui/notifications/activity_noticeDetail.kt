package kr.ac.jbnu.coe.ui.notifications

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.royrodriguez.transitionbutton.TransitionButton
import kr.ac.jbnu.coe.R
import kr.ac.jbnu.coe.activity_singleImageView

class activity_noticeDetail : AppCompatActivity(){
    lateinit var btn_url : TransitionButton
    lateinit var contents : TextView
    lateinit var title : TextView
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var title_str : String
    lateinit var date : TextView
    lateinit var imgLL : LinearLayout
    val db = Firebase.firestore
    val storageReference = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_noticedetail)

        toolbar = findViewById(R.id.toolbar)
        date = findViewById(R.id.txt_date)
        contents = findViewById(R.id.txt_contents)
        title = findViewById(R.id.txt_title)
        btn_url = findViewById(R.id.btn_url)
        imgLL = findViewById(R.id.imgLL)

        date.text = intent.getStringExtra("dateTime")
        title_str = intent.getStringExtra("noticeTitle").toString()

        contents.setMovementMethod(ScrollingMovementMethod())

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        title.text = title_str

        getData()
    }

    fun dpToPx(dp: Int, context: Context): Int {
        val density: Float = context.getResources().getDisplayMetrics().density

        return Math.round(dp.toFloat() * density)
    }

    fun getData(){
        val docRef = db.collection("Notice").document(title_str)

        docRef.get().addOnCompleteListener{task ->
            if(task.isSuccessful){
                val document = task.result

                if(document != null){
                    contents.setText(document.get("contents").toString().replace("\\n", System.getProperty("line.separator")))
                    val index = document.get("index").toString()
                    val url = document.get("url").toString()
                    if(document.get("imageIndex") != null){
                        val imageIndex = document.get("imageIndex").toString().toInt()

                        if(imageIndex > 1){
                            for(i in 0..imageIndex - 1){
                                val child = "notice/" + index + "/" + i + ".png"
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

                        else{
                            val child = "notice/" + index  + ".png"
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

                    else{
                        val child = "notice/" + index  + ".png"
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

                    if(url != ""){
                        btn_url.visibility = View.VISIBLE
                        btn_url.setOnClickListener(View.OnClickListener {
                            btn_url.startAnimation()
                            val intent = Intent(Intent.ACTION_VIEW)
                            val uri = Uri.parse(url)
                            intent.setData(uri)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            btn_url.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND, null)
                            startActivity(intent);
                        })
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