package kr.ac.jbnu.coe.ui.alliance

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rd.PageIndicatorView
import kr.ac.jbnu.coe.R
import java.util.*
import kotlin.concurrent.timer


class AllianceFragment : Fragment(), View.OnClickListener {
    private var pagerAdapter: PagerAdapter? = null
    private var currentPage : Int = 1
    lateinit var pager : ViewPager
    lateinit var txt_activity_1 : Button
    lateinit var txt_activity_2 : Button
    lateinit var txt_activity_3 : Button
    lateinit var txt_activity_4 : Button

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_alliance, container, false)
        val indicator: PageIndicatorView = root.findViewById(R.id.pageIndicatorView)
        val btn_all : ImageButton = root.findViewById(R.id.btn_all)
        val btn_meal : ImageButton = root.findViewById(R.id.btn_meal)
        val btn_soup : ImageButton = root.findViewById(R.id.btn_sports)
        val btn_cafe : ImageButton = root.findViewById(R.id.btn_cafe)
        val btn_convenience : ImageButton = root.findViewById(R.id.btn_convenience)
        val btn_alcohol : ImageButton = root.findViewById(R.id.btn_alcohol)
        val btn_details : ImageButton = root.findViewById(R.id.btn_details)
        txt_activity_1 = root.findViewById(R.id.btn_promote_1)
        txt_activity_2 = root.findViewById(R.id.btn_promote_2)
        txt_activity_3 = root.findViewById(R.id.btn_promote_3)
        txt_activity_4 = root.findViewById(R.id.btn_promote_4)

        val dpValue = 16
        val d = resources.displayMetrics.density
        val margin = (dpValue * d)
        pager = root.findViewById(R.id.viewPager)

        pager.setPadding(margin.toInt(), 0, margin.toInt(), 0)
        pagerAdapter = PagerAdapter(context)
        pager.adapter = pagerAdapter
        indicator.setCount(3)

        val handler = Handler()
        val runnable = object : Runnable{
            override fun run(){

                pager.setCurrentItem((pager.currentItem + 1) % 3, true)

                handler.postDelayed(this, 2000)

            }
        }

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
                currentPage = state + 1
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

            }

        })

        handler.post(runnable)

        getData()

        btn_all.setOnClickListener(this)
        btn_meal.setOnClickListener(this)
        btn_soup.setOnClickListener(this)
        btn_alcohol.setOnClickListener(this)
        btn_convenience.setOnClickListener(this)
        btn_cafe.setOnClickListener(this)
        btn_details.setOnClickListener(this)

        txt_activity_1.setOnClickListener(this)
        txt_activity_2.setOnClickListener(this)
        txt_activity_3.setOnClickListener(this)
        txt_activity_4.setOnClickListener(this)

        return root
    }

    override fun onClick(v: View?) {
        if(v!=null){
            if(v.id == R.id.btn_all){
                val intent = Intent(activity, activity_allianceList::class.java)
                intent.putExtra("category", "All")
                intent.putExtra("categoryKR", "전체 보기")
                startActivity(intent)
            }

            if(v.id == R.id.btn_meal){
                val intent = Intent(activity, activity_allianceList::class.java)
                intent.putExtra("category", "Meal")
                intent.putExtra("categoryKR", "식사")
                startActivity(intent)
            }

            if(v.id == R.id.btn_sports){
                val intent = Intent(activity, activity_allianceList::class.java)
                intent.putExtra("category", "Sports")
                intent.putExtra("categoryKR", "스포츠")
                startActivity(intent)
            }

            if(v.id == R.id.btn_cafe){
                val intent = Intent(activity, activity_allianceList::class.java)
                intent.putExtra("category", "Cafe")
                intent.putExtra("categoryKR", "카페")
                startActivity(intent)
            }

            if(v.id == R.id.btn_alcohol){
                val intent = Intent(activity, activity_allianceList::class.java)
                intent.putExtra("category", "Alcohol")
                intent.putExtra("categoryKR", "술")
                startActivity(intent)
            }

            if(v.id == R.id.btn_convenience){
                val intent = Intent(activity, activity_allianceList::class.java)
                intent.putExtra("category", "Convenience")
                intent.putExtra("categoryKR", "편의")
                startActivity(intent)
            }

            if(v.id == R.id.btn_promote_1){
                val db = Firebase.firestore
                val docRef = db.collection("Promotion").document("#1")

                docRef.get().addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val document = task.result

                        if(document.exists()){
                            val intent = Intent(activity, activity_allianceList::class.java)
                            intent.putExtra("category", document.get("link").toString())
                            intent.putExtra("categoryKR", document.get("link_KR").toString())
                            startActivity(intent)
                        }
                    }
                }
            }

            if(v.id == R.id.btn_promote_2){
                val db = Firebase.firestore
                val docRef = db.collection("Promotion").document("#2")

                docRef.get().addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val document = task.result

                        if(document.exists()){
                            val intent = Intent(activity, activity_allianceList::class.java)
                            intent.putExtra("category", document.get("link").toString())
                            intent.putExtra("categoryKR", document.get("link_KR").toString())
                            startActivity(intent)
                        }
                    }
                }
            }

            if(v.id == R.id.btn_promote_3){
                val db = Firebase.firestore
                val docRef = db.collection("Promotion").document("#3")

                docRef.get().addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val document = task.result

                        if(document.exists()){
                            val intent = Intent(activity, activity_allianceList::class.java)
                            intent.putExtra("category", document.get("link").toString())
                            intent.putExtra("categoryKR", document.get("link_KR").toString())
                            startActivity(intent)
                        }
                    }
                }
            }

            if(v.id == R.id.btn_promote_4){
                val db = Firebase.firestore
                val docRef = db.collection("Promotion").document("#4")

                docRef.get().addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val document = task.result

                        if(document.exists()){
                            val intent = Intent(activity, activity_allianceList::class.java)
                            intent.putExtra("category", document.get("link").toString())
                            intent.putExtra("categoryKR", document.get("link_KR").toString())
                            startActivity(intent)
                        }
                    }
                }
            }

            if(v.id == R.id.btn_details){
                val db = Firebase.firestore
                val docRef = db.collection("Ad").document(pager.currentItem.toString())

                docRef.get().addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val document = task.result

                        if(document.exists()){
                            val category = document.get("category").toString()
                            val storeName = document.get("storeName").toString()

                            val benefitRef = db.collection("Coalition").document(category)
                            benefitRef.get().addOnCompleteListener{task ->
                                if(task.isSuccessful){
                                    val document = task.result

                                    if(document != null){
                                        val storeMap : MutableMap<String, Any>? = document.data

                                        if (storeMap != null) {
                                            val value = storeMap.get(key = storeName)
                                            val value_split = value.toString().split("{benefits=")
                                            val value_fin = value_split[1].split("}")

                                            val intent = Intent(activity, activity_storeDetail::class.java)
                                            intent.putExtra("storeName", storeName)
                                            intent.putExtra("benefit", value_fin[0])

                                            startActivity(intent)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun getData(){
        val db = Firebase.firestore
        val docRef = db.collection("Promotion")

        docRef.get().addOnSuccessListener { result ->
            for(document in result){
                if(document.id == "#1"){
                    txt_activity_1.setText(document.get("title").toString() + "\n" + document.get("contents").toString())
                }

                if(document.id == "#2"){
                    txt_activity_2.setText(document.get("title").toString() + "\n" + document.get("contents").toString())
                }

                if(document.id == "#3"){
                    txt_activity_3.setText(document.get("title").toString() + "\n" + document.get("contents").toString())
                }

                if(document.id == "#4"){
                    txt_activity_4.setText(document.get("title").toString() + "\n" + document.get("contents").toString())
                }
            }
        }
    }
}