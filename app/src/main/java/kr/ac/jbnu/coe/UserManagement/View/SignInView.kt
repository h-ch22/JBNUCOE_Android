package kr.ac.jbnu.coe.UserManagement.View

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.royrodriguez.transitionbutton.TransitionButton
import kr.ac.jbnu.coe.MainActivity
import kr.ac.jbnu.coe.R

class SignInView : AppCompatActivity(), View.OnClickListener{
    var btn_signUp : Button? = null
    var btn_signIn : TransitionButton? = null
    var btn_resetPassword : Button? = null
    var field_email : EditText? = null
    var field_password : EditText? = null
    var autoSignin : CheckBox? = null
    var context : Context? = null
    private val db = FirebaseFirestore.getInstance()
    private lateinit var email : String
    private lateinit var password : String
    private lateinit var auth : FirebaseAuth
    lateinit var auto : SharedPreferences
    lateinit var autoSignIn : SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_signin)

        context = this@SignInView
        auth = Firebase.auth
        btn_signUp = findViewById(R.id.btn_signUp)
        btn_signIn = findViewById(R.id.btn_signIn)
        btn_resetPassword = findViewById(R.id.btn_resetPassword)
        field_email = findViewById(R.id.field_email)
        field_password = findViewById(R.id.field_password)
        autoSignin = findViewById(R.id.checkbox_autoSignIn)

        btn_signUp?.setOnClickListener(this)
        btn_resetPassword?.setOnClickListener(this)
        btn_signIn?.setOnClickListener(this)

        auto = (context as SignInView).getSharedPreferences("signIn Information", Activity.MODE_PRIVATE)
        autoSignIn = auto.edit()

        email = auto.getString("Email", "").toString()
        password = auto.getString("Password", "").toString()

        if(email != "" && password != ""){
            btn_signIn?.startAnimation()
            signIn()
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            if(v.getId() == R.id.btn_signIn){
                email = field_email?.text.toString()
                password = field_password?.text.toString()

                if(email != "" && password != ""){
                    btn_signIn?.startAnimation()
                    signIn()
                }

                else{
                    btn_signIn?.startAnimation()
                    btn_signIn?.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                    showDialog(title = "공백 필드", contents = "E-Mail과 비밀번호를 입력하세요.")
                }
            }

            if (v.getId() == R.id.btn_signUp){
                val intent = Intent(this, LicenseView::class.java)
                startActivity(intent)
                finish()
            }

            if(v.getId() == R.id.btn_resetPassword){
                val intent = Intent(this, ResetPasswordView::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    fun signIn(){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){task ->
                    Log.d("status", "signing...")
                    if(task.isSuccessful){
                        Log.d("result", "signin success")
                        if(autoSignin?.isChecked!!){
                            autoSignIn.putString("Email", email)
                            autoSignIn.putString("Password", password)
                            autoSignIn.commit()
                        }

                        FirebaseMessaging.getInstance().token.addOnCompleteListener(
                            OnCompleteListener { task ->
                                if(!task.isSuccessful){
                                    Log.w("FCM", "Fetching FCM Registration token failed", task.exception)

                                }

                                else{
                                    val token = task.result
                                    val docRef = db.collection("User").document(FirebaseAuth.getInstance().currentUser?.email.toString())
                                    docRef.update("token", token)

                                    docRef.get().addOnCompleteListener { task ->
                                        if(task.isSuccessful){
                                            val document = task.result

                                            if(document != null){
                                                val studentNo = document.get("studentNo").toString()

                                                val adminRef = db.collection("User").document("Admin")

                                                adminRef.get().addOnCompleteListener { task ->
                                                    if(task.isSuccessful){
                                                        val document = task.result

                                                        if(document != null && document.get(studentNo) != null){
                                                            val tokenRef = adminRef.collection("tokens").document(studentNo)

                                                            tokenRef.update("token", token)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }


                                }
                            })

                        btn_signIn?.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND, TransitionButton.OnAnimationStopEndListener {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            startActivity(intent)
                        })
                    }

                    else{
                        btn_signIn?.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                        showDialog(title = "로그인 오류", contents = "로그인 중 오류가 발생했습니다.\nE-Mail과 비밀번호가 올바른지 확인하거나 네트워크 상태를 확인한 후 다시시도하십시오.")
                    }
                }.addOnFailureListener(this){
                Log.d("Auth : ", it.toString())
            }
    }

    fun showDialog(title : String, contents : String){
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this@SignInView,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle(title) //제목
        dlg.setMessage(contents)
        dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->

        })

        dlg.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        System.exit(0)
    }
}