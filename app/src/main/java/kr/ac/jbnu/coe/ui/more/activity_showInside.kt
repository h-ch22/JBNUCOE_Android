package kr.ac.jbnu.coe.ui.more

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.royrodriguez.transitionbutton.TransitionButton
import kr.ac.jbnu.coe.R

class activity_showInside : AppCompatActivity(){
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var title : TextView
    lateinit var btn_B1F : TransitionButton
    lateinit var btn_1F : TransitionButton
    lateinit var btn_2F : TransitionButton
    lateinit var btn_3F : TransitionButton
    lateinit var btn_4F : TransitionButton
    lateinit var btn_5F : TransitionButton
    lateinit var btn_6F : TransitionButton
    lateinit var btn_7F : TransitionButton
    lateinit var btn_8F : TransitionButton
    lateinit var btn_9F : TransitionButton
    lateinit var img_inside : com.github.chrisbanes.photoview.PhotoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_showinside)

        toolbar = findViewById(R.id.toolbar)
        title = findViewById(R.id.txt_title)
        btn_B1F = findViewById(R.id.btn_B1F)
        btn_1F = findViewById(R.id.btn_1F)
        btn_2F = findViewById(R.id.btn_2F)
        btn_3F = findViewById(R.id.btn_3F)
        btn_4F = findViewById(R.id.btn_4F)
        btn_5F = findViewById(R.id.btn_5F)
        btn_6F = findViewById(R.id.btn_6F)
        btn_7F = findViewById(R.id.btn_7F)
        btn_8F = findViewById(R.id.btn_8F)
        btn_9F = findViewById(R.id.btn_9F)
        img_inside = findViewById(R.id.img_inside)

        title.text = intent.getStringExtra("dept")

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadImage(building = title.text.toString())
    }

    fun loadImage(building : String){
        if (building.equals("공과대학 1호관")){
            btn_B1F.visibility = View.GONE
            btn_1F.visibility = View.VISIBLE
            btn_2F.visibility = View.VISIBLE
            btn_3F.visibility = View.VISIBLE
            btn_4F.visibility = View.GONE
            btn_5F.visibility = View.GONE
            btn_6F.visibility = View.GONE
            btn_7F.visibility = View.GONE
            btn_8F.visibility = View.GONE
            btn_9F.visibility = View.GONE

            btn_1F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_1_1f)
            })

            btn_2F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_1_2f)
            })

            btn_3F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_1_3f)
            })
        }

        if (building.equals("공과대학 2호관")){
            btn_B1F.visibility = View.GONE
            btn_1F.visibility = View.VISIBLE
            btn_2F.visibility = View.VISIBLE
            btn_3F.visibility = View.VISIBLE
            btn_4F.visibility = View.VISIBLE
            btn_5F.visibility = View.GONE
            btn_6F.visibility = View.GONE
            btn_7F.visibility = View.GONE
            btn_8F.visibility = View.GONE
            btn_9F.visibility = View.GONE

            btn_1F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_2_1f)
            })

            btn_2F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_2_2f)
            })

            btn_3F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_2_3f)
            })

            btn_4F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_2_4f)
            })
        }

        if (building.equals("공과대학 3호관")){
            btn_B1F.visibility = View.GONE
            btn_1F.visibility = View.VISIBLE
            btn_2F.visibility = View.VISIBLE
            btn_3F.visibility = View.VISIBLE
            btn_4F.visibility = View.VISIBLE
            btn_5F.visibility = View.GONE
            btn_6F.visibility = View.GONE
            btn_7F.visibility = View.GONE
            btn_8F.visibility = View.GONE
            btn_9F.visibility = View.GONE

            btn_1F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_3_1f)
            })

            btn_2F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_3_2f)
            })

            btn_3F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_3_3f)
            })

            btn_4F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_3_4f)
            })
        }

        if (building.equals("공과대학 4호관")){
            btn_B1F.visibility = View.VISIBLE
            btn_1F.visibility = View.VISIBLE
            btn_2F.visibility = View.VISIBLE
            btn_3F.visibility = View.VISIBLE
            btn_4F.visibility = View.VISIBLE
            btn_5F.visibility = View.GONE
            btn_6F.visibility = View.GONE
            btn_7F.visibility = View.GONE
            btn_8F.visibility = View.GONE
            btn_9F.visibility = View.GONE

            btn_B1F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_4_b1f)
            })

            btn_1F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_4_1f)
            })

            btn_2F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_4_2f)
            })

            btn_3F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_4_3f)
            })

            btn_4F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_4_4f)
            })
        }

        if (building.equals("공과대학 5호관")){
            btn_B1F.visibility = View.GONE
            btn_1F.visibility = View.VISIBLE
            btn_2F.visibility = View.VISIBLE
            btn_3F.visibility = View.VISIBLE
            btn_4F.visibility = View.VISIBLE
            btn_5F.visibility = View.VISIBLE
            btn_6F.visibility = View.GONE
            btn_7F.visibility = View.GONE
            btn_8F.visibility = View.GONE
            btn_9F.visibility = View.GONE

            btn_1F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_5_1f)
            })

            btn_2F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_5_2f)
            })

            btn_3F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_5_3f)
            })

            btn_4F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_5_4f)
            })

            btn_5F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_5_5f)
            })
        }

        if (building.equals("공과대학 6호관")){
            btn_B1F.visibility = View.VISIBLE
            btn_1F.visibility = View.VISIBLE
            btn_2F.visibility = View.VISIBLE
            btn_3F.visibility = View.VISIBLE
            btn_4F.visibility = View.VISIBLE
            btn_5F.visibility = View.VISIBLE
            btn_6F.visibility = View.GONE
            btn_7F.visibility = View.GONE
            btn_8F.visibility = View.GONE
            btn_9F.visibility = View.GONE

            btn_B1F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_6_b1f)
            })

            btn_1F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_6_1f)
            })

            btn_2F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_6_2f)
            })

            btn_3F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_6_3f)
            })

            btn_4F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_6_4f)
            })

            btn_5F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_6_5f)
            })
        }

        if (building.equals("공과대학 7호관")){
            btn_B1F.visibility = View.GONE
            btn_1F.visibility = View.VISIBLE
            btn_2F.visibility = View.VISIBLE
            btn_3F.visibility = View.VISIBLE
            btn_4F.visibility = View.VISIBLE
            btn_5F.visibility = View.VISIBLE
            btn_6F.visibility = View.VISIBLE
            btn_7F.visibility = View.GONE
            btn_8F.visibility = View.GONE
            btn_9F.visibility = View.GONE

            btn_1F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_7_1f)
            })

            btn_2F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_7_2f)
            })

            btn_3F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_7_3f)
            })

            btn_4F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_7_4f)
            })

            btn_5F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_7_5f)
            })

            btn_6F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_7_6f)
            })
        }

        if (building.equals("공과대학 8호관")){
            btn_B1F.visibility = View.GONE
            btn_1F.visibility = View.VISIBLE
            btn_2F.visibility = View.VISIBLE
            btn_3F.visibility = View.VISIBLE
            btn_4F.visibility = View.VISIBLE
            btn_5F.visibility = View.GONE
            btn_6F.visibility = View.GONE
            btn_7F.visibility = View.GONE
            btn_8F.visibility = View.GONE
            btn_9F.visibility = View.GONE

            btn_1F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_8_1f)
            })

            btn_2F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_8_2f)
            })

            btn_3F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_8_3f)
            })

            btn_4F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_8_4f)
            })
        }

        if (building.equals("공과대학 9호관")){
            btn_B1F.visibility = View.GONE
            btn_1F.visibility = View.VISIBLE
            btn_2F.visibility = View.VISIBLE
            btn_3F.visibility = View.VISIBLE
            btn_4F.visibility = View.VISIBLE
            btn_5F.visibility = View.VISIBLE
            btn_6F.visibility = View.VISIBLE
            btn_7F.visibility = View.VISIBLE
            btn_8F.visibility = View.VISIBLE
            btn_9F.visibility = View.VISIBLE

            btn_1F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_9_1f)
            })

            btn_2F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_9_2f)
            })

            btn_3F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_9_3f)
            })

            btn_4F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_9_4f)
            })

            btn_5F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_9_5f)
            })

            btn_6F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_9_6f)
            })

            btn_7F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_9_7f)
            })

            btn_8F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_9_8f)
            })

            btn_9F.setOnClickListener(View.OnClickListener {
                img_inside.setImageResource(R.drawable.building_9_9f)
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}