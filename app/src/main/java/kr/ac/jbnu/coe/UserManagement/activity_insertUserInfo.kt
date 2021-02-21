package kr.ac.jbnu.coe.UserManagement

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.royrodriguez.transitionbutton.TransitionButton
import kr.ac.jbnu.coe.R

class activity_insertUserInfo : AppCompatActivity(), View.OnClickListener{
    var field_email : EditText? = null
    var field_password : EditText? = null
    var field_checkPassword : EditText? = null
    var field_name : EditText? = null
    var field_phone : EditText? = null
    var btn_next : TransitionButton? = null
    var email : String? = null
    var password : String? = null
    var checkPassword : String? = null
    var name : String? = null
    var phone : String? = null
    var passwordHelper : TextView? = null
    var checkPasswordHelper : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_insertuserinfo)

        btn_next = findViewById(R.id.btn_next)
        field_email = findViewById(R.id.field_email)
        field_password = findViewById(R.id.field_password)
        field_checkPassword = findViewById(R.id.field_repassword)
        field_name = findViewById(R.id.field_name)
        field_phone = findViewById(R.id.field_phone)
        passwordHelper = findViewById(R.id.passwordhelper)
        checkPasswordHelper = findViewById(R.id.checkPasswordHelper)

        field_password?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                password = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if(s.length < 6){
                        passwordHelper?.text = "6자리 이상의 비밀번호를 입력하세요."
                        passwordHelper?.setTextColor(Color.parseColor("#ff6969"))
                    }

                    else{
                        passwordHelper?.text = "사용가능한 비밀번호입니다."
                        passwordHelper?.setTextColor(Color.parseColor("#32a852"))
                    }
                }
            }

        })

        field_checkPassword?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if(!s.toString().equals(password)){
                        checkPasswordHelper?.text = "비밀번호가 일치하지 않습니다."
                        checkPasswordHelper?.setTextColor(Color.parseColor("#ff6969"))
                    }

                    else{
                        checkPasswordHelper?.text = "비밀번호가 일치합니다."
                        checkPasswordHelper?.setTextColor(Color.parseColor("#32a852"))
                    }
                }

            }

        })

        btn_next?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            if(v.id == R.id.btn_next){
                email = field_email?.text.toString()
                password = field_password?.text.toString()
                checkPassword = field_checkPassword?.text.toString()
                name = field_name?.text.toString()
                phone = field_phone?.text.toString()

                if(email != "" && password != "" && checkPassword != "" && name != "" && phone != ""){
                    if(password!!.length < 6){
                        showDialog(title = "비밀번호 오류", contents = "6자리 이상의 비밀번호를 설정해주십시오.")
                    }

                    else{
                        if(!password.equals(checkPassword)){
                            showDialog(title = "비밀번호 오류", contents = "비밀번호가 일치하지 않습니다.")
                        }

                        else{
                            val intent = Intent(this, activity_checkAcademic::class.java)
                            intent.putExtra("email", email)
                            intent.putExtra("password", password)
                            intent.putExtra("name", name)
                            intent.putExtra("phone", phone)

                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, activity_license::class.java)
        startActivity(intent)
        finish()
    }

    fun showDialog(title : String, contents : String){
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_insertUserInfo,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle(title)
        dlg.setMessage(contents)
        dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->

        })

        dlg.show()
    }
}