package kr.ac.jbnu.coe.ui.handWriting

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kr.ac.jbnu.coe.R

class activity_handWriting_edit : AppCompatActivity(){
    val db = Firebase.firestore
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var txt_title : TextView
    lateinit var txt_contents : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_handwritingedit)

        txt_title = findViewById(R.id.field_title)
        txt_contents = findViewById(R.id.field_contents)
        toolbar = findViewById(R.id.toolbar)

        txt_title.text = intent.getStringExtra("handWritingTitle").toString()
        txt_contents.text = intent.getStringExtra("handWritingContents").toString()

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.action_upload -> {
                val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_handWriting_edit,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                dlg.setTitle("수정 확인")
                dlg.setMessage("업로드하시겠습니까?")
                dlg.setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                    upload()
                })

                dlg.setNegativeButton("아니오", {dialog, which ->  })

                dlg.show()

            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun upload(){
        val docRef = db.collection("HandWriting").document(txt_title.text.toString())

        docRef.get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val document = task.result

                if(document != null){
                    docRef.update("contents", txt_contents.text.toString()).addOnSuccessListener {
                        val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_handWriting_edit,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                        dlg.setTitle("업로드 완료")
                        dlg.setMessage("정상 처리되었습니다.")
                        dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                            val intent = Intent(applicationContext, activity_handWriting::class.java)
                            startActivity(intent)
                            finish()
                        })

                        dlg.show()
                    }
                            .addOnFailureListener{e -> Log.e("error", "error while uploading", e)
                                                showDialog(title = "오류", contents = "업로드 중 오류가 발생했습니다.\n네트워크 상태를 확인하거나 나중에 다시 시도하십시오.\n" + e)
                            }
                }
            }
        }
    }

    fun showDialog(title : String, contents : String){
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_handWriting_edit,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle(title)
        dlg.setMessage(contents)
        dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->

        })

        dlg.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_write, menu)

        return true
    }
}