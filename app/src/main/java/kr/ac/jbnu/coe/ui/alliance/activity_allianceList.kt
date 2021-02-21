package kr.ac.jbnu.coe.ui.alliance

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kr.ac.jbnu.coe.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class activity_allianceList : AppCompatActivity(){
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var storeListAdapter : storeListAdapter
    var allianceList = arrayListOf<storeItem>()
    lateinit var storeList : RecyclerView
    var storeNameList = ArrayList<String>()
    lateinit var category : String
    lateinit var categoryKR : String
    lateinit var title : TextView
    val db = Firebase.firestore
    val storageReference = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_storelist)

        category = intent.getStringExtra("category").toString()
        categoryKR = intent.getStringExtra("categoryKR").toString()

        getData()

        storeList = findViewById(R.id.storeList)
        title = findViewById(R.id.txt_title)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        title.text = categoryKR
    }

    fun getData(){
        if(category.equals("All") && !category.equals("")){
            val docRef = db.collection("Coalition")
            docRef.get().addOnSuccessListener { result ->
                for(document in result){
                    val documentMap : MutableMap<String, Any>? = document.data

                    if (documentMap != null) {
                        for((key, value) in documentMap){
                            val value_split = value.toString().split("{benefits=")
                            val value_fin = value_split[1].split("}")

                            getTime(storeName = key, benefit = value_fin[0])
                            storeNameList.add(key)
                        }
                    }
                }
            }
        }

        if(!category.equals("All") && !category.equals("")){
            val docRef = db.collection("Coalition").document(category)
            docRef.get().addOnCompleteListener{task ->
                if(task.isSuccessful){
                    val document = task.result

                    if(document != null){
                        val storeMap : MutableMap<String, Any>? = document.data

                        if (storeMap != null) {
                            for((key, value) in storeMap){
                                val value_split = value.toString().split("{benefits=")
                                val value_fin = value_split[1].split("}")

                                getTime(storeName = key, benefit = value_fin[0])
                                storeNameList.add(key)
                            }
                        }
                    }

                    else{
                        showDialog(title = "데이터 오류",
                                    contents = "데이터를 받아오는 중 문제가 발생하였습니다.\n네트워크 상태를 확인한 후 다시시도하시거나, 공대 학생회에 문의해주세요.")
                    }
                }

                else{
                    showDialog(title = "데이터 오류",
                        contents = "데이터를 받아오는 중 문제가 발생하였습니다.\n네트워크 상태를 확인한 후 다시시도하시거나, 공대 학생회에 문의해주세요.")
                }
            }.addOnFailureListener{exception ->  showDialog(title = "데이터 오류",
                                                            contents = "데이터를 받아오는 중 다음 문제가 발생하였습니다.\n" + exception)}
        }
    }

    fun getImage(storeName : String, benefit : String, isAvailable : String){
        if(storeName != ""){
            val docRef = db.collection("Store").document("eng")
            docRef.get().addOnCompleteListener{task ->
                if(task.isSuccessful){
                    val document = task.result

                    if(document != null){
                        val downloadURL = storageReference.child( "storeLogo/"+document.get(storeName)+".png")
                        allianceList.add(storeItem(img = downloadURL, title = storeName, benefit = benefit, isAvailable = isAvailable))

                        storeListAdapter = storeListAdapter(this, allianceList){
                            storeItem ->
                            val intent = Intent(this, activity_storeDetail::class.java)
                            intent.putExtra("storeName", storeItem.title)
                            intent.putExtra("benefit",  storeItem.benefit)
                            intent.putExtra("image", storeItem.img.toString())

                            startActivity(intent)
                        }

                        storeList.adapter = storeListAdapter

                        val layoutManager = LinearLayoutManager(this)
                        storeList.layoutManager = layoutManager
                        storeList.setHasFixedSize(true)
                    }
                }
            }
        }
    }

    fun getTime(storeName : String, benefit : String){
        if(storeName != ""){
            val docRef = db.collection("location").document(storeName)
            docRef.get().addOnCompleteListener{task ->
                if(task.isSuccessful){
                    val document = task.result

                    if(document != null){
                        var isAvailable = ""
                        val open = document.get("open").toString()
                        val close = document.get("close").toString()

                        if(open.equals("unknown") || close.equals("unknown")){
                            isAvailable = "이용 시간을 알 수 없습니다."
                        }

                        else{
                            val sdf = SimpleDateFormat("yyyy.MM.dd")
                            val sdf_time = SimpleDateFormat("HH:mm")
                            val sdf_withTime = SimpleDateFormat("yyyy.MM.dd HH:mm")

                            val today = sdf_withTime.format(Date())
                            val today_date = sdf.format(Date())
                            val openDate = today_date + " " + open
                            var closeDate = today_date + " " + close
                            Log.d("close", closeDate)
                            Log.d("today", today)

                            var close_fin = sdf_withTime.parse(closeDate)
                            var open_fin = sdf_withTime.parse(openDate)

                            if(close_fin.after(sdf_withTime.parse(today))){
                                val today_tmp = Date()
                                val tomorrow = Date(today_tmp.time + ( 1000 * 60 * 60 * 24 ))
                                val tomorrow_str = sdf_withTime.format(tomorrow).toString()
                                close_fin = sdf_withTime.parse(tomorrow_str)
                            }

                            if(sdf_withTime.parse(today).before(close_fin) && sdf_withTime.parse(today).after(open_fin)){
                                isAvailable = "이용 가능"
                            }

                            else{
                                isAvailable = "이용 불가"
                            }
                        }

                        getImage(storeName = storeName, benefit = benefit, isAvailable = isAvailable)
                    }
                }
            }
        }
    }

    fun showDialog(title : String, contents : String){
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_allianceList,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle(title) //제목
        dlg.setMessage(contents)
        dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->

        })

        dlg.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_map, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.menu_map->{
                if(allianceList == null || allianceList.size == 0 || allianceList.isEmpty()){
                    showDialog(title = "업체 목록을 받아오는 중입니다.", contents = "업체 목록을 받아오고 있습니다.\n업체 목록이 완전히 표시되면 이용해주세요.")
                }

                else{
                    val intent = Intent(this, activity_storeMap::class.java)
                    intent.putExtra("alliance", storeNameList)
                    startActivity(intent)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}