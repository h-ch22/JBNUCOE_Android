package kr.ac.jbnu.coe.ui.more

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kr.ac.jbnu.coe.R
import java.io.InputStream

class activity_EULA : AppCompatActivity(){
    lateinit var txt_EULA : TextView
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var title : TextView
    lateinit var type : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_eula)

        txt_EULA = findViewById(R.id.txt_EULA)
        toolbar = findViewById(R.id.toolbar)
        title = findViewById(R.id.txt_title)

        txt_EULA.setMovementMethod(ScrollingMovementMethod())

        type = intent.getStringExtra("type").toString()

        if(type.equals("EULA")){
            title.text = "이용 약관"
        }

        if(type.equals("Privacy")){
            title.text = "개인 정보 처리 방침"
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setData()
    }

    fun setData(){
        if(type.equals("Privacy")){
            var fileName = "License.txt"
            var inputStream : InputStream = application.assets.open(fileName)
            var inputString = inputStream.bufferedReader().use{it.readText()}

            txt_EULA?.setText(inputString)
        }

        if(type.equals("EULA")){

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