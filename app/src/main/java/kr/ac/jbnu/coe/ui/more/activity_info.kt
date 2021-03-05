package kr.ac.jbnu.coe.ui.more

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.royrodriguez.transitionbutton.TransitionButton
import kr.ac.jbnu.coe.R

class activity_info : AppCompatActivity(), View.OnClickListener{
    lateinit var txt_current : TextView
    lateinit var txt_latest : TextView
    lateinit var txt_status : TextView
    lateinit var btn_update : TransitionButton
    lateinit var btn_EULA : TransitionButton
    lateinit var btn_privacy : TransitionButton
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    val db = Firebase.firestore
    lateinit var currentVer : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_info)

        txt_current = findViewById(R.id.txt_currentVersion)
        txt_latest = findViewById(R.id.txt_latestVersion)
        txt_status = findViewById(R.id.txt_versionStatus)
        btn_update = findViewById(R.id.btn_update)
        btn_EULA = findViewById(R.id.btn_EULA)
        btn_privacy = findViewById(R.id.btn_privacy)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        getCurrentVersion()

        btn_privacy.setOnClickListener(this)
        btn_EULA.setOnClickListener(this)
        btn_update.setOnClickListener(this)
    }

    private fun getCurrentVersion(){
        try{
            val pInfo : PackageInfo = applicationContext.packageManager.getPackageInfo(applicationContext.packageName, 0)
            txt_current.text = "현재 버전 : v " + pInfo.versionName
            currentVer = pInfo.versionName

            Log.d("current", pInfo.versionName)

            getLatestVersion()
        }   catch(e : PackageManager.NameNotFoundException){
            e.printStackTrace()
        }
    }

    private fun getLatestVersion(){
        db.collection("Version").document("Android").get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val document = task.result

                if(document.exists()){
                    val latest : String = document.get("latest").toString()

                    txt_latest.text = "최신 버전 : v " + latest

                    if (!latest.equals(currentVer)){
                        txt_status.text = "최신 버전이 아닙니다."
                        btn_update.visibility = View.VISIBLE
                        txt_status?.setTextColor(Color.parseColor("#ff5145"))
                        txt_status?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0)
                        txt_status?.compoundDrawables?.get(0)?.setTint(Color.parseColor("#ff5145"))
                    }

                    else{
                        txt_status.text = "최신 버전 입니다."
                        btn_update.visibility = View.GONE
                        txt_status.setTextColor(Color.parseColor("#009630"))
                        txt_status?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0)
                        txt_status?.compoundDrawables?.get(0)?.setTint(Color.parseColor("#009630"))
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        if(v != null){
            if(v.id == R.id.btn_privacy){
                val intent = Intent(applicationContext, activity_EULA::class.java)
                intent.putExtra("type", "Privacy")
                startActivity(intent)
                finish()
            }

            if(v.id == R.id.btn_EULA){
                val intent = Intent(applicationContext, activity_EULA::class.java)
                intent.putExtra("type", "EULA")
                startActivity(intent)
                finish()
            }

            if(v.id == R.id.btn_update){
                val intent = Intent(android.content.Intent.ACTION_VIEW)
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=kr.ac.jbnu.coe"))
                startActivity(intent)
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