package kr.ac.jbnu.coe.UserManagement

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import com.ornach.nobobutton.NoboButton
import com.royrodriguez.transitionbutton.TransitionButton
import kr.ac.jbnu.coe.MainActivity
import kr.ac.jbnu.coe.R
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

class activity_checkAcademic : AppCompatActivity(), View.OnClickListener{
    var email = ""
    var password = ""
    var phone = ""
    var name = ""
    var dept = ""
    var studentNo = ""
    var deptSpinner : Spinner? = null
    var btn_check : TransitionButton? = null
    var field_studentNo : EditText? = null
    var btn_load : NoboButton? = null
    var img : ImageView? = null
    var uri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_checkacademic)

        deptSpinner = findViewById(R.id.spinner_dept)
        btn_check = findViewById(R.id.btn_checkAcadmic)
        field_studentNo = findViewById(R.id.field_studentNo)
        btn_load = findViewById(R.id.btn_load)
        img = findViewById(R.id.img_idCard)

        email = intent.getStringExtra("email").toString()
        password = intent.getStringExtra("password").toString()
        phone = intent.getStringExtra("phone").toString()
        name = intent.getStringExtra("name").toString()

        val deptItems = resources.getStringArray(R.array.depts)
        val deptAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, deptItems)

        deptSpinner?.prompt = "학과 선택"
        deptSpinner?.adapter = deptAdapter

        deptSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                dept = deptSpinner?.selectedItem as String
            }

            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

        }

        btn_load?.setOnClickListener(this)
        btn_check?.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val images = ImagePicker.getImages(data)

        if(images != null && images.isNotEmpty()){
            img?.let { Glide.with(it).load(images[0].uri).into(it) }
            img?.visibility = View.VISIBLE

            uri = images[0].uri
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            if(v.id == R.id.btn_load){
                val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_checkAcademic,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                dlg.setTitle("원하시는 작업을 선택하세요.")
                dlg.setMessage("캡처된 학생증이 있을 경우 \"갤러리\" 버튼을 클릭해 학생증을 로드하십시오.\n 캡처된 학생증이 없을 경우 \"전북대앱 열기\" 버튼을 클릭해 전북대앱을 열어 학생증을 캡처한 후 돌아오십시오.")
                dlg.setPositiveButton("전북대앱 열기", DialogInterface.OnClickListener { dialog, which ->
                    val library = "https://play.google.com/store/apps/details?id=com.jbnu.jbnuapp"
                    val libraryIntent = Intent(Intent.ACTION_VIEW, Uri.parse(library))
                    startActivity(libraryIntent)
                })

                dlg.setNegativeButton("갤러리", DialogInterface.OnClickListener{dialog, which ->
                    ImagePicker.create(this)
                            .returnMode(ReturnMode.ALL)
                            .folderMode(true)
                            .toolbarFolderTitle("폴더 선택")
                            .toolbarImageTitle("학생증을 선택하세요.")
                            .toolbarArrowColor(Color.BLACK)
                            .includeVideo(false)
                            .single()
                            .showCamera(false)
                            .start()
                })

                dlg.show()
            }

            if(v.id == R.id.btn_checkAcadmic){
                studentNo = field_studentNo?.text.toString()
                btn_check?.startAnimation()

                if(studentNo.equals("")){
                    showDialog(title = "학번 입력", contents = "학번을 입력하십시오.")
                    btn_check?.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                }

                else{
                    loadImage()
                }
            }

        }
    }

    fun loadImage(){
        try{
            val image = uri?.let { FirebaseVisionImage.fromFilePath(applicationContext, it) }

            if (image != null) {
                checkAcadmic(image)
            }
        }   catch(e : IOException){
            e.printStackTrace()
        }
    }

    fun checkAcadmic(image : FirebaseVisionImage){
        val detector = FirebaseVision.getInstance().cloudTextRecognizer
        val options = FirebaseVisionCloudTextRecognizerOptions.Builder()
                .setLanguageHints(listOf("ko"))
                .build()

        var isNamePassed = false
        var isDeptPassed = false
        var isStudentNoPassed = false
        var isCheckPassed = false
        var textList : Array<String>

        val result = detector.processImage(image)
                .addOnSuccessListener { firebaseVisionText ->
                    for(block in firebaseVisionText.textBlocks){
                        val boundingBox = block.boundingBox
                        val cornerPoints = block.cornerPoints
                        val text = block.text

                        for(line in block.lines){
                            var lineText = line.getText()
                            Log.d("line", lineText)

                            if(lineText.equals(name)){
                                isNamePassed = true
                            }

                            if(lineText.equals(dept)){
                                isDeptPassed = true
                            }

                            if(lineText.contains(studentNo)){
                                isStudentNoPassed = true
                            }

                            if(lineText.equals("교육/부속기관")){
                                isCheckPassed = true
                            }
                        }
                    }

                    if(isCheckPassed && isDeptPassed && isNamePassed && isStudentNoPassed){
                        uploadUserData()
                    }

                    else{
                        btn_check?.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                        showDialog(title = "유효성 확인 실패", contents = "학생증의 정보와 입력한 정보가 일치하지 않거나, 변조된 학생증입니다.")
                        Log.d("result", isCheckPassed.toString() + ", dept : " + isDeptPassed + ", name : " +  isNamePassed + ", studentNo : " + isStudentNoPassed)
                    }
                }
    }

    fun signUp(){
        val auth : FirebaseAuth = Firebase.auth

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){task ->
                    if(task.isSuccessful){
                        btn_check?.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND, TransitionButton.OnAnimationStopEndListener {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            startActivity(intent)
                        })

                    }

                    else{
                        showDialog(title = "가입 실패", contents = "회원 가입 처리 중 오류가 발생하였습니다.\n가입 이력 여부와 네트워크 상태를 화인한 후 다시 시도하십시오.")
                    }
                }
    }

    fun uploadUserData(){
        val db : FirebaseFirestore = FirebaseFirestore.getInstance()

        val user = hashMapOf(
                "name" to name,
                "dept" to dept,
                "phone" to phone,
                "studentNo" to studentNo
        )

        db.collection("User").document(email).set(user)
                .addOnCompleteListener{
                    signUp()
                }
                .addOnFailureListener{
                    showDialog(title = "업로드 실패", contents = "사용자 정보를 업로드하는 중 문제가 발생하였습니다.\n" + it)
                }

    }

    fun showDialog(title : String, contents : String){
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_checkAcademic,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle(title)
        dlg.setMessage(contents)
        dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->

        })

        dlg.show()
    }

}