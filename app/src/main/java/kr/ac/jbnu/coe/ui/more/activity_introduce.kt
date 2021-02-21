package kr.ac.jbnu.coe.ui.more

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kr.ac.jbnu.coe.R
import java.io.InputStream

class activity_introduce : AppCompatActivity(){
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var txt_contents : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_greet)

        toolbar = findViewById(R.id.toolbar)
        txt_contents = findViewById(R.id.txt_contents)

        txt_contents?.setMovementMethod(ScrollingMovementMethod())
        readContents()

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    fun readContents(){
        var fileName = "greeting.txt"
        var inputStream : InputStream = application.assets.open(fileName)
        var inputString = inputStream.bufferedReader().use{it.readText()}

        txt_contents?.setText(inputString)
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