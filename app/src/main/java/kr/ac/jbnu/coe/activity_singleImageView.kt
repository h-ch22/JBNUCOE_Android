package kr.ac.jbnu.coe

import android.content.Intent
import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.github.chrisbanes.photoview.PhotoView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kr.ac.jbnu.coe.ui.handWriting.activity_handWriting_write

class activity_singleImageView : AppCompatActivity(){
    lateinit var imgView : PhotoView
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var url : String
    val storageReference = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_singleimageview)

        imgView = findViewById(R.id.imageView)
        url = intent.getStringExtra("url").toString()

        if (url != ""){
            downloadImage()
        }

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun downloadImage(){
        val downloadURL = storageReference.child(url)
        Glide.with(this).load(downloadURL).apply(
                RequestOptions.bitmapTransform(
                        RoundedCorners(16)
                )).into(imgView)
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