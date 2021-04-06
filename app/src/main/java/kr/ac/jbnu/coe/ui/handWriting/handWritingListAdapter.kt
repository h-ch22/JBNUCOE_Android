package kr.ac.jbnu.coe.ui.handWriting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kr.ac.jbnu.coe.R
import kr.ac.jbnu.coe.ui.alliance.storeItem
import kr.ac.jbnu.coe.ui.notifications.noticeItem

class handWritingListAdapter(val context : Context, val handWritingList : ArrayList<handWritingItem>, val itemClick : (handWritingItem) -> Unit) : RecyclerView.Adapter<handWritingListAdapter.Holder>(), Filterable{
    var filteredList : ArrayList<handWritingItem>? = null

    init{
        filteredList = handWritingList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_handwritingitem, parent, false)
        return Holder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return filteredList!!.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(filteredList!![position], context)
    }

    inner class Holder(itemView: View?, itemClick: (handWritingItem) -> Unit) : RecyclerView.ViewHolder(itemView!!){
        val noticeTitle = itemView?.findViewById<TextView>(R.id.noticeTitle)
        val noticeDate = itemView?.findViewById<TextView>(R.id.noticeDate)

        val title = itemView?.findViewById<TextView>(R.id.title)
        val recommend = itemView?.findViewById<TextView>(R.id.txt_recommend)
        val read = itemView?.findViewById<TextView>(R.id.txt_read)
        val author = itemView?.findViewById<TextView>(R.id.name)
        val dateTime = itemView?.findViewById<TextView>(R.id.dateTime)

        fun bind(handWritingItem : handWritingItem, context: Context){
            title?.text = handWritingItem.title
            recommend?.text = handWritingItem.recommend
            read?.text = handWritingItem.read
            author?.text = handWritingItem.name
            dateTime?.text = handWritingItem.dateTime

            itemView.setOnClickListener{itemClick(handWritingItem)}

        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filteredList = handWritingList
                } else {
                    val filteringList = ArrayList<handWritingItem>()

                    for (row in handWritingList) {
                        if (row.title.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(row)
                        }
                    }
                    filteredList = filteringList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredList = filterResults.values as ArrayList<handWritingItem>
                notifyDataSetChanged()
            }
        }
    }
}