package kr.ac.jbnu.coe.ui.more

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.royrodriguez.transitionbutton.TransitionButton
import kr.ac.jbnu.coe.MainActivity
import kr.ac.jbnu.coe.R
import kr.ac.jbnu.coe.UserManagement.View.SignInView
import kr.ac.jbnu.coe.ui.handWriting.activity_handWriting

class MoreFragment : Fragment(), View.OnClickListener {
    lateinit var img_profile : ImageView
    lateinit var name : TextView
    lateinit var dept : TextView
    lateinit var admin : TextView
    lateinit var btn_changeProfile : TransitionButton
    lateinit var btn_introduce : TransitionButton
    lateinit var btn_feedbackHub : TransitionButton
    lateinit var btn_campusMap : TransitionButton
    lateinit var btn_product : TransitionButton
    lateinit var btn_signOut : TransitionButton
    lateinit var btn_secession : TransitionButton
    lateinit var btn_handWriting : TransitionButton
    lateinit var btn_info : TransitionButton
    lateinit var mContext : Context
    lateinit var downloadURL : StorageReference
    lateinit var auto : SharedPreferences
    lateinit var autoSignIn : SharedPreferences.Editor
    lateinit var txt_studentNo : TextView
    lateinit var btn_percentage : TransitionButton
    lateinit var btn_delivery : TransitionButton

    var uri : Uri? = null
    val Gallery = 0
    val email = Firebase.auth.currentUser?.email.toString()
    val db = Firebase.firestore
    val storageReference = FirebaseStorage.getInstance().reference

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_more, container, false)
        auto = (context as MainActivity).getSharedPreferences("signIn Information", Activity.MODE_PRIVATE)
        autoSignIn = auto.edit()

        name = root.findViewById(R.id.txt_name)
        dept = root.findViewById(R.id.txt_dept)
        admin = root.findViewById(R.id.isAdmin)
        img_profile = root.findViewById(R.id.img_profile)
        btn_changeProfile = root.findViewById(R.id.btn_changeProfile)
        btn_introduce = root.findViewById(R.id.btn_introduce)
        btn_feedbackHub = root.findViewById(R.id.btn_feedbackHub)
        btn_campusMap = root.findViewById(R.id.btn_campusmap)
        btn_product = root.findViewById(R.id.btn_checkLate)
        btn_signOut = root.findViewById(R.id.btn_signOut)
        btn_secession = root.findViewById(R.id.btn_secsession)
        txt_studentNo = root.findViewById(R.id.txt_studentNo)
        btn_handWriting = root.findViewById(R.id.btn_handwriting)
        btn_percentage = root.findViewById(R.id.btn_percent)
        btn_delivery = root.findViewById(R.id.btn_delivery)

        getData()

        btn_changeProfile.setOnClickListener(this)
        btn_introduce.setOnClickListener(this)
        btn_feedbackHub.setOnClickListener(this)
        btn_campusMap.setOnClickListener(this)
        btn_product.setOnClickListener(this)
        btn_signOut.setOnClickListener(this)
        btn_secession.setOnClickListener(this)
        btn_handWriting.setOnClickListener(this)
        btn_percentage.setOnClickListener(this)
        btn_delivery.setOnClickListener(this)

        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == Gallery){
            if(resultCode == Activity.RESULT_OK){
                uri = data?.data

                try{
                    if(uri != null){
                        val file = uri

                        val profileRef = storageReference.child("profile/" + email + "/profile_" + email + ".jpg")
                        val upload = file?.let { profileRef.putFile(it) }

                        if (upload != null) {
                            upload.addOnFailureListener{
                                btn_changeProfile.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                                showDialog(title = "업로드 실패", contents = "프로필 이미지 업로드 중 오류가 발생했습니다.\n네트워크 상태를 확인한 후 다시 시도하십시오.\n" + it)

                            }.addOnSuccessListener { taskSnapshot ->
                                btn_changeProfile.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                                Toast.makeText(mContext, "정상 처리 되었습니다.", Toast.LENGTH_SHORT).show()

                                val ft = fragmentManager?.beginTransaction()
                                ft?.detach(this)?.attach(this)?.commit()
                            }
                        }
                    }
                }   catch(e:Exception){
                    e.printStackTrace()
                }
            }
        }


    }

    fun getData(){
        val docRef = db.collection("User").document(Firebase.auth.currentUser?.email.toString())
        img_profile.setImageURI(null)
        docRef.get().addOnCompleteListener{task ->
            if(task.isSuccessful){
                val document = task.result

                if(document != null){
                    name.text = document.get("name").toString()
                    dept.text = document.get("dept").toString()
                    txt_studentNo.text = document.get("studentNo").toString()
                    var studentNo = document.get("studentNo").toString()

                    val docRef = db.collection("User").document("Admin")
                    docRef.get().addOnCompleteListener{task ->
                        if(task.isSuccessful){
                            val document = task.result

                            if(document != null){
                                val adminCheck = document.get(studentNo)
                                if(adminCheck != null){
                                    admin.text = document.get(studentNo).toString()
                                    admin.setTextColor(Color.parseColor("#009630"))
                                    admin.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_admin, 0, 0, 0)
                                    admin.compoundDrawables?.get(0)?.setTint(Color.parseColor("#009630"))
                                }

                                else{
                                    admin.visibility = View.INVISIBLE
                                }
                            }
                        }
                    }
                    downloadURL = storageReference.child("profile/" + email + "/profile_" + email + ".jpg")
                    Glide.with(this).load(downloadURL).diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true).apply(
                        RequestOptions.bitmapTransform(
                            CircleCrop()
                        )).into(img_profile)
                }
            }
        }
    }

    fun changeProfile(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(intent, "이미지 로드"), Gallery)
    }

    override fun onClick(v: View?) {
        if (v != null){
            if(v.id == R.id.btn_delivery){
                val intent = Intent(mContext, activity_delivery::class.java)
                startActivity(intent)
            }

            if(v.id == R.id.btn_changeProfile){
                btn_changeProfile.startAnimation()
                changeProfile()
            }

            if(v.id == R.id.btn_percent){
                val intent = Intent(mContext, activity_percentage::class.java)
                startActivity(intent)
            }

            if(v.id == R.id.btn_introduce){
                val intent = Intent(mContext, activity_introduceMain::class.java)
                startActivity(intent)
            }

            if(v.id == R.id.btn_campusmap){
                val intent = Intent(mContext, activity_campusMap::class.java)
                startActivity(intent)
            }

            if(v.id == R.id.btn_checkLate){
                val intent = Intent(mContext, activity_productStatus::class.java)
                startActivity(intent)
            }

            if(v.id == R.id.btn_feedbackHub){
                val intent = Intent(mContext, activity_feedbackHubMain::class.java)
                startActivity(intent)
            }

            if(v.id == R.id.btn_handwriting){
                val intent = Intent(mContext, activity_handWriting::class.java)
                startActivity(intent)
            }

            if(v.id == R.id.btn_signOut){
                val dlg: AlertDialog.Builder = AlertDialog.Builder(mContext,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                dlg.setTitle("로그아웃 확인")
                dlg.setMessage("로그아웃 시 자동로그인은 자동으로 해제됩니다.\n계속 하시겠습니까?")
                dlg.setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                    autoSignIn.clear()
                    autoSignIn.commit()
                    signOut()
                })

                dlg.setNegativeButton("아니오", {dialog, which ->

                })

                dlg.show()
            }

            if(v.id == R.id.btn_secsession){
                val dlg: AlertDialog.Builder = AlertDialog.Builder(mContext,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                dlg.setTitle("회원탈퇴 확인")
                dlg.setMessage("회원탈퇴 시 모든 데이터가 제거되며, 서비스 재이용 시 다시 가입하셔야합니다.\n계속 하시겠습니까?")
                dlg.setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                    autoSignIn.clear()
                    autoSignIn.commit()
                    secession()
                })

                dlg.setNegativeButton("아니오", {dialog, which ->

                })

                dlg.show()
            }
        }
    }

    fun showDialog(title : String, contents : String){
        val dlg: AlertDialog.Builder = AlertDialog.Builder(mContext,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle(title)
        dlg.setMessage(contents)
        dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->

        })

        dlg.show()
    }

    fun signOut(){
        val docRef = db.collection("User").document(email)

        docRef.get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val document = task.result

                if(document != null && document.exists()){
                    val studentNo = document.get("studentNo").toString()

                    val update = hashMapOf<String, Any>(
                        "token" to FieldValue.delete()
                    )

                    docRef.update(update)

                    val adminRef = db.collection("User").document("Admin")

                    adminRef.get().addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            val document = task.result

                            if(document != null && document.get(studentNo) != null){
                                val tokenRef = adminRef.collection("tokens").document(studentNo)

                                val update = hashMapOf<String, Any>(
                                    "token" to FieldValue.delete()
                                )

                                tokenRef.update(update)
                            }
                        }
                    }
                }
            }
        }

        Firebase.auth.signOut()

        if(Firebase.auth.currentUser == null){
            val dlg: AlertDialog.Builder = AlertDialog.Builder(mContext,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
            dlg.setTitle("로그아웃 완료")
            dlg.setMessage("정상 처리 되었습니다.")
            dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                val intent = Intent(mContext, SignInView::class.java)
                startActivity(intent)
            })

            dlg.show()
        }

        else{
            showDialog(title = "로그아웃 실패", contents = "처리 중 문제가 발생하였으나, 자동 로그인은 해제되었습니다.\n다시 로그인하려면 앱을 재실행하십시오.")
        }
    }

    fun secession(){
        val userRef = storageReference.child("profile/" + email + "/profile_" + email + ".jpg")
        val docRef = db.collection("User").document(email)

        docRef.get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val document = task.result

                if(document != null && document.exists()){
                    val adminRef = db.collection("User").document("Admin").collection("tokens").document(document.get("studentNo").toString())

                    adminRef.delete()
                }
            }
        }

        docRef.delete()
        userRef.delete()


        Firebase.auth.currentUser?.delete()?.addOnCompleteListener{ task ->
            if(task.isSuccessful){
                val dlg: AlertDialog.Builder = AlertDialog.Builder(mContext,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                dlg.setTitle("감사 인사")
                dlg.setMessage("회원 탈퇴가 정상적으로 처리되었습니다.\n더 나은 서비스로 다시 찾아뵐 수 있도록 노력하겠습니다.\n그 동안 서비스를 이용해주셔서 진심으로 감사드립니다.")
                dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                    val intent = Intent(mContext, SignInView::class.java)
                    startActivity(intent)
                })

                dlg.show()
            }
        }
    }
}