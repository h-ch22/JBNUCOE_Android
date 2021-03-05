package kr.ac.jbnu.coe.ui.alliance

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.rd.PageIndicatorView
import kr.ac.jbnu.coe.R
import java.util.*
import kotlin.concurrent.timer


class AllianceFragment : Fragment(), View.OnClickListener {
    private var pagerAdapter: PagerAdapter? = null
    private var currentPage : Int = 1

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_alliance, container, false)
        val indicator: PageIndicatorView = root.findViewById(R.id.pageIndicatorView)
        val pager: ViewPager = root.findViewById(R.id.viewPager)
        val btn_all : ImageButton = root.findViewById(R.id.btn_all)
        val btn_meal : ImageButton = root.findViewById(R.id.btn_meal)
        val btn_soup : ImageButton = root.findViewById(R.id.btn_sports)
        val btn_cafe : ImageButton = root.findViewById(R.id.btn_cafe)
        val btn_convenience : ImageButton = root.findViewById(R.id.btn_convenience)
        val btn_alcohol : ImageButton = root.findViewById(R.id.btn_alcohol)

        val dpValue = 16
        val d = resources.displayMetrics.density
        val margin = (dpValue * d)

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

        btn_all.setOnClickListener(this)
        btn_meal.setOnClickListener(this)
        btn_soup.setOnClickListener(this)
        btn_alcohol.setOnClickListener(this)
        btn_convenience.setOnClickListener(this)
        btn_cafe.setOnClickListener(this)

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
        }
    }
}