package kr.ac.jbnu.coe.ui.more

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.royrodriguez.transitionbutton.TransitionButton
import kr.ac.jbnu.coe.R
import kr.ac.jbnu.coe.ui.alliance.activity_storeMap

class activity_introduceMain : AppCompatActivity(), View.OnClickListener{
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var btn_hello : TransitionButton
    lateinit var btn_introduce : TransitionButton
    lateinit var btn_percentage : TransitionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_introduce_main)

        btn_hello = findViewById(R.id.btn_hello)
        btn_introduce = findViewById(R.id.btn_introduce)
        btn_percentage = findViewById(R.id.btn_percent)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        btn_hello.setOnClickListener(this)
        btn_introduce.setOnClickListener(this)
        btn_percentage.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v!=null){
            if(v.id == R.id.btn_hello){
                val intent = Intent(this@activity_introduceMain, activity_introduce::class.java)
                startActivity(intent)
            }

            if(v.id == R.id.btn_introduce){
                val intent = Intent(this@activity_introduceMain, activity_introduceDept::class.java)
                startActivity(intent)
            }

            if(v.id == R.id.btn_percent){
                val intent = Intent(this@activity_introduceMain, activity_percentage::class.java)
                startActivity(intent)
            }
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