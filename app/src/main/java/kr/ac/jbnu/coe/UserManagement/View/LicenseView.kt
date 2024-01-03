package kr.ac.jbnu.coe.UserManagement.View

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.royrodriguez.transitionbutton.TransitionButton
import kr.ac.jbnu.coe.R
import java.io.InputStream

class LicenseView : AppCompatActivity(), View.OnClickListener{
    var txtView : TextView? = null
    var btn_accept : TransitionButton? = null
    var btn_disAccept : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_license)

        txtView = findViewById(R.id.txt_license)
        btn_accept = findViewById(R.id.btn_accept)
        btn_disAccept = findViewById(R.id.btn_disAccept)

        txtView?.setMovementMethod(ScrollingMovementMethod())
        readLicense()

        btn_accept?.setOnClickListener(this)
        btn_disAccept?.setOnClickListener(this)
    }

    fun readLicense(){
        var fileName = "License.txt"
        var inputStream : InputStream = application.assets.open(fileName)
        var inputString = inputStream.bufferedReader().use{it.readText()}

        txtView?.setText(inputString)
    }



    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_accept -> showDialog()
            R.id.btn_disAccept -> disAccept()
        }
    }

    fun showDialog(){
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this@LicenseView,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle("약관 동의 확인") //제목
        dlg.setMessage("소프트웨어 이용약관을 읽고 동의합니다.")
        dlg.setPositiveButton("동의", DialogInterface.OnClickListener { dialog, which ->
            val intent = Intent(this, InsertUserInfoView::class.java)
            startActivity(intent)
            finish()
        })

        dlg.setNegativeButton("동의 안함", DialogInterface.OnClickListener{ dialog, which ->

        })

        dlg.show()
    }

    fun disAccept(){
        Toast.makeText(this, "이용약관에 동의하지 않았기 때문에 더 이상 진행할 수 없습니다.", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, SignInView::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, SignInView::class.java)
        startActivity(intent)
        finish()
    }
}