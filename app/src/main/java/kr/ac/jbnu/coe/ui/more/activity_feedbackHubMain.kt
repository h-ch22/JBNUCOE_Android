package kr.ac.jbnu.coe.ui.more

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.royrodriguez.transitionbutton.TransitionButton
import kotlinx.android.synthetic.main.layout_apply.*
import kr.ac.jbnu.coe.R
import java.text.SimpleDateFormat
import java.util.*

class activity_feedbackHubMain : AppCompatActivity(), View.OnClickListener{
    lateinit var typeSpinner : Spinner
    lateinit var categorySpinner: Spinner
    lateinit var field_feedback : EditText
    lateinit var btn_sendFeedback : TransitionButton
    lateinit var btn_allFeedback : TransitionButton
    lateinit var field_title : EditText
    lateinit var btn_myFeedback : TransitionButton
    var type : String = ""
    var category : String = ""
    var feedback : String = ""
    val user = Firebase.auth.currentUser
    val email = user?.email.toString()
    val db = Firebase.firestore
    var title : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_feedbackhub)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        typeSpinner = findViewById(R.id.spinner_type)
        categorySpinner = findViewById(R.id.spinner_category)
        field_feedback = findViewById(R.id.field_feedback)
        btn_sendFeedback = findViewById(R.id.sendFeedback)
        btn_allFeedback = findViewById(R.id.allFeedback)
        field_title = findViewById(R.id.field_title)
        btn_myFeedback = findViewById(R.id.btn_myFeedback)

        val typeItems = resources.getStringArray(R.array.type)
        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, typeItems)

        val categoryItems = resources.getStringArray(R.array.category)
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categoryItems)

        typeSpinner?.prompt = "피드백 종류 선택"
        typeSpinner?.adapter = typeAdapter

        typeSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                type = typeSpinner?.selectedItem as String
            }

            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

        }

        categorySpinner?.prompt = "카테고리 선택"
        categorySpinner?.adapter = categoryAdapter

        categorySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                category = categorySpinner?.selectedItem as String
            }

            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

        }

        val docRef = db.collection("User").document(email)
        docRef.get().addOnCompleteListener{task ->
            if(task.isSuccessful){
                val document = task.result

                if(document != null){
                    val studentNo = document.get("studentNo").toString()

                    if(!studentNo.equals("")){
                        db.collection("User").document("Admin")
                            .get().addOnCompleteListener{task ->
                                if(task.isSuccessful){
                                    val document = task.result

                                    if(document != null){
                                        val isAdmin = document.get(studentNo)

                                        if(isAdmin != null){
                                            btn_allFeedback.visibility = View.VISIBLE
                                        }

                                        else{
                                            btn_allFeedback.visibility = View.INVISIBLE
                                        }
                                    }
                                }
                            }
                    }
                }
            }
        }

        btn_sendFeedback.setOnClickListener(this)
        btn_allFeedback.setOnClickListener(this)
        btn_myFeedback.setOnClickListener(this)
    }

    fun sendFeedback(author : String){
        val docRef = db.collection("Feedback").document(title)
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val current = sdf.format(Date())

        val feedbackMap = hashMapOf(
            "Category" to category,
            "Date Time" to current,
            "Feedback" to feedback,
            "Type" to type,
            "author" to author
        )

        docRef.set(feedbackMap)
            .addOnCompleteListener{
                val dlg: AlertDialog.Builder = AlertDialog.Builder(this,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                dlg.setTitle("피드백 전송 완료")
                dlg.setMessage("피드백이 정상적으로 업로드 되었습니다.\n" +
                        "보내주신 소중한 피드백은 최대한 반영할 수 있도록 노력하겠습니다.\n" +
                        "감사합니다.")
                dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                    btn_sendFeedback.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND, null)
                    onBackPressed()
                })

                dlg.show()
            }
            .addOnFailureListener{
                btn_apply.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                val dlg: AlertDialog.Builder = AlertDialog.Builder(this,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                dlg.setTitle("피드백 업로드 실패")
                dlg.setMessage("업로드하는 중 문제가 발생했습니다.\n네트워크 상태를 확인한 후 다시시도하십시오.\n에러 코드 : " + it)
                dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                })

                dlg.show()
            }
    }

    override fun onClick(v: View?) {
        if (v != null){
            if(v.id == R.id.sendFeedback){
                btn_sendFeedback.startAnimation()
                feedback = field_feedback.text.toString()
                title = field_title.text.toString()

                if(type == ""){
                    showDialog(title = "피드백 종류 선택", contents = "피드백 종류를 선택하세요.")
                    btn_sendFeedback.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                }

                if(category == ""){
                    showDialog(title = "카테고리 선택", contents = "카테고리를 선택하세요.")
                    btn_sendFeedback.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                }

                if(feedback == ""){
                    showDialog(title = "공백 필드", contents = "피드백을 입력해주세요.")
                    btn_sendFeedback.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                }

                if(title == ""){
                    showDialog(title = "공백 필드", contents = "피드백의 제목을 입력해주세요.")
                    btn_sendFeedback.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                }

                if(!type.equals("") && !category.equals("") && !feedback.equals("") && !title.equals("")){

                    db.collection("User").document(email).get().addOnCompleteListener{task ->
                        if(task.isSuccessful){
                            val document = task.result

                            if(document != null){
                                val author = document.get("dept").toString() + " " +
                                        document.get("studentNo").toString() + " " +
                                        document.get("name").toString()

                                sendFeedback(author = author)
                            }
                        }
                    }
                }
            }

            if(v.id == R.id.allFeedback){
                val intent = Intent(applicationContext, activity_allFeedback::class.java)
                startActivity(intent)
            }

            if(v.id == R.id.btn_myFeedback){
                val intent = Intent(applicationContext, activity_myFeedback::class.java)
                startActivity(intent)
            }
        }
    }

    fun showDialog(title : String, contents : String){
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this@activity_feedbackHubMain,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle(title)
        dlg.setMessage(contents)
        dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
            onBackPressed()
        })

        dlg.show()
    }
}