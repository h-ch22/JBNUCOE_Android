package kr.ac.jbnu.coe.ui.handWriting

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kr.ac.jbnu.coe.R
import kr.ac.jbnu.coe.ui.notifications.activity_noticeDetail
import java.util.*

class activity_handWriting : AppCompatActivity(){
    val db = Firebase.firestore
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var handWritingListView : RecyclerView
    lateinit var handWritingListAdapter : handWritingListAdapter
    var handWritingList = arrayListOf<handWritingItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_handwritinglist)

        toolbar = findViewById(R.id.toolbar)
        handWritingListView = findViewById(R.id.handWritingList)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        getData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_handwritinglist, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.action_filter -> {
                val btn_filter : View = findViewById(R.id.action_filter)
                val popup : PopupMenu = PopupMenu(applicationContext, btn_filter)
                val inf : MenuInflater = popup.menuInflater
                inf.inflate(R.menu.menu_handwriting, popup.menu)
                popup.setOnMenuItemClickListener(PopupMenuListener())
                popup.show()
            }

            R.id.action_add -> {
                val intent = Intent(this, activity_handWriting_write::class.java)
                startActivity(intent)
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    inner class PopupMenuListener : PopupMenu.OnMenuItemClickListener{
        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when(item?.itemId){
                R.id.action_sortDate ->
                    sortByDate()

                R.id.action_sortName ->
                    sortByName()

                R.id.action_sortRecommend ->
                    sortByRecommend()
            }

            return true
        }

    }

    private fun sortByName(){
        handWritingList.sortByDescending { it.title }
        Collections.reverse(handWritingList)
        handWritingListAdapter.notifyDataSetChanged()
    }

    private fun sortByDate(){
        handWritingList.sortByDescending { it.dateTime }
        handWritingListAdapter.notifyDataSetChanged()

    }

    private fun sortByRecommend(){
        handWritingList.sortByDescending { it.recommend }
        handWritingListAdapter.notifyDataSetChanged()

    }

    private fun getData(){
        val docRef = db.collection("HandWriting")
        docRef.get().addOnSuccessListener { result ->
            for(document in result){
                val dateTime = document.get("Date Time").toString()
                val author = document.get("author").toString()
                val read = document.get("read").toString()
                val recommend = document.get("recommend").toString()

                handWritingList.add(handWritingItem(title = document.id, recommend = recommend, read = read, dateTime = dateTime, name = author))

                handWritingListAdapter = handWritingListAdapter(this, handWritingList){
                    handWritingItem ->
                    val intent = Intent(applicationContext, activity_handWritingDetails::class.java)
                    intent.putExtra("handWritingTitle", handWritingItem.title)

                    startActivity(intent)
                }

                handWritingList.sortByDescending { it.dateTime }

                handWritingListView.adapter = handWritingListAdapter

                val layoutManager = LinearLayoutManager(this)
                handWritingListView.layoutManager = layoutManager
                handWritingListView.setHasFixedSize(true)
            }
        }
    }
}