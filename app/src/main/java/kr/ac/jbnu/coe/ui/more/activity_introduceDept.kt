package kr.ac.jbnu.coe.ui.more

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kr.ac.jbnu.coe.R
import java.io.InputStream

class activity_introduceDept : AppCompatActivity(){
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var txt_plan : TextView
    lateinit var txt_cooperation : TextView
    lateinit var txt_affairs : TextView
    lateinit var txt_welfair : TextView
    lateinit var txt_support : TextView
    lateinit var txt_policy : TextView
    lateinit var txt_promote : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_department)
        toolbar = findViewById(R.id.toolbar)
        txt_promote = findViewById(R.id.txt_promote)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        getContent()
    }

    fun getContent(){
        var promoteName = "promote.txt"
        var inputStream_promote : InputStream = application.assets.open(promoteName)
        var inputString_promote = inputStream_promote.bufferedReader().use{it.readText()}

        txt_promote?.setText(inputString_promote)


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