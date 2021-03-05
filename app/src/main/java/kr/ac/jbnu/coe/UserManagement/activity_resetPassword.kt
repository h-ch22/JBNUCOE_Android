package kr.ac.jbnu.coe.UserManagement

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.royrodriguez.transitionbutton.TransitionButton
import kr.ac.jbnu.coe.R

class activity_resetPassword : AppCompatActivity(), View.OnClickListener{
    var field_email : EditText? = null
    var btn_send : TransitionButton? = null
    lateinit var email : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_resetpassword)

        btn_send = findViewById(R.id.btn_send)
        field_email = findViewById(R.id.field_email)

        btn_send?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            if(v.id == R.id.btn_send){
                email = field_email?.text.toString()

                if(email != ""){
                    Firebase.auth.sendPasswordResetEmail(email).addOnCompleteListener(){ task ->
                        if(task.isSuccessful){
                            showDialog(title = "E-Mail 전송 완료", contents = "비밀번호 재설정 링크가 포함된 E-Mail을 정상적으로 발송하였습니다.")
                        }

                        else{
                            showDialog(title = "E-Mail 전송 실패", contents = "E-Mail 발송 중 오류가 발생하였습니다.\n가입한 E-Mail이 맞는지 확인하거나, 네트워크 상태를 확인하신 후 다시시도하십시오.")
                        }
                    }
                }

                else{
                    showDialog(title = "공백 필드", contents = "E-Mail을 입력하세요.")
                }
            }
        }
    }

    fun showDialog(title : String, contents : String){
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_resetPassword,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle(title) //제목
        dlg.setMessage(contents)
        dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->

        })

        dlg.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, activity_signIn::class.java)
        startActivity(intent)
        finish()
    }
}