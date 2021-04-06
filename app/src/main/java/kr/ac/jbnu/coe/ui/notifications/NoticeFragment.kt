package kr.ac.jbnu.coe.ui.notifications

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kr.ac.jbnu.coe.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.Comparator

class NoticeFragment : Fragment(){
    lateinit var noticeListAdapter : noticeListAdapter
    var noticeList = ArrayList<noticeItem>()
    lateinit var noticeListView : RecyclerView
    val db = Firebase.firestore
    val storageReference = FirebaseStorage.getInstance().reference
    lateinit var mContext : Context
    lateinit var searchView: SearchView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_notice, container, false)

        searchView = root.findViewById(R.id.search_view)
        noticeListView = root.findViewById(R.id.noticeList)
        getData()

        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    fun getData(){
        db.collection("Notice").get().addOnSuccessListener { result ->
            for(document in result){
                val dateTime = document.get("timeStamp").toString()
                val index = document.get("index").toString()
                val read = document.get("read").toString()
                val title = document.id
                val format = DateTimeFormatter.ofPattern("yyyy. MM. dd. HH:mm")
                noticeList.add(noticeItem(title = title, date = dateTime, read = read))


            }

            noticeList.sortByDescending { it.date }

            noticeListAdapter = noticeListAdapter(mContext, noticeList){
                noticeItem ->
                val intent = Intent(mContext, activity_noticeDetail::class.java)
                intent.putExtra("noticeTitle", noticeItem.title)
                intent.putExtra("dateTime", noticeItem.date)

                startActivity(intent)
            }


            noticeListView.adapter = noticeListAdapter

            val layoutManager = LinearLayoutManager(mContext)
            noticeListView.layoutManager = layoutManager
            noticeListView.setHasFixedSize(true)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    noticeListAdapter.filter.filter(query)

                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    noticeListAdapter.filter.filter(newText)

                    return false
                }

            })

        }
    }


}