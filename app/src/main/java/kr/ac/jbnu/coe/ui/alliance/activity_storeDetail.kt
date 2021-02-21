package kr.ac.jbnu.coe.ui.alliance

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kr.ac.jbnu.coe.R

class activity_storeDetail : AppCompatActivity(), View.OnClickListener{
    lateinit var storeName : TextView
    lateinit var benefit : TextView
    lateinit var img : ImageView
    lateinit var storeName_String : String
    lateinit var img_menu : ImageView
    lateinit var price : TextView
    lateinit var menuName : TextView
    lateinit var btn_map : Button
    lateinit var btn_call : Button
    lateinit var phone : String

    var REQUEST_PHONE_CALL = 1000
    val db = Firebase.firestore
    val storageReference = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_storedetail)

        storeName = findViewById(R.id.txt_title)
        benefit = findViewById(R.id.txt_benefit)
        img = findViewById(R.id.img_store)
        price = findViewById(R.id.price)
        menuName = findViewById(R.id.menuName)
        img_menu = findViewById(R.id.img_menu)
        btn_map = findViewById(R.id.btn_map)
        btn_call = findViewById(R.id.btn_call)

        storeName_String = intent.getStringExtra("storeName").toString()
        storeName.text = storeName_String
        benefit.text = intent.getStringExtra("benefit").toString()

        getMenu()

        btn_map.setOnClickListener(this)
        btn_call.setOnClickListener(this)
    }

    fun getMenu(){
        val docRef = db.collection("Store").document("eng")
        val menuRef = db.collection("location").document(storeName_String)
        docRef.get().addOnCompleteListener{task ->
            if(task.isSuccessful){
                val document = task.result

                if(document != null){
                    val engName = document.get(storeName_String)
                    val downloadURL = storageReference.child("storeLogo/"+ engName +".png")
                    Glide.with(this).load(downloadURL).apply(
                        RequestOptions.bitmapTransform(
                            RoundedCorners(16)
                        )).into(img)

                    menuRef.get().addOnCompleteListener{task ->
                        if(task.isSuccessful){
                            val document = task.result
                            val menuURL = storageReference.child("menu/"+ engName +".jpg")

                            if(document != null){
                                menuName.text = document.get("menu").toString()
                                price.text = document.get("price").toString()
                                phone = document.get("tel").toString()

                                Glide.with(this).load(menuURL).apply(
                                    RequestOptions.bitmapTransform(
                                        RoundedCorners(16)
                                    )).into(img_menu)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        if(v!=null){
            if(v.id == R.id.btn_map){
                val intent = Intent(this, activity_storeMap::class.java)
                intent.putExtra("storeName", storeName_String)
                intent.putExtra("benefit", benefit.text.toString())

                startActivity(intent)
            }

            if(v.id == R.id.btn_call){
                val intent =
                    Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone))

                var permission = arrayOf(Manifest.permission.CALL_PHONE)

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permission, REQUEST_PHONE_CALL)
                }
                else
                {
                    startActivity(intent)
                }

            }
        }
    }

}