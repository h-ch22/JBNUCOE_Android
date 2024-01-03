package kr.ac.jbnu.coe.ui.notifications

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
import java.util.*

class noticeListAdapter(val context : Context, val noticeList : ArrayList<noticeItem>, val itemClick : (noticeItem) -> Unit) : RecyclerView.Adapter<noticeListAdapter.Holder>(), Filterable {
    var filteredList : ArrayList<noticeItem>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_noticeitem, parent, false)
        return Holder(view, itemClick)
    }

    init{
        filteredList = noticeList
    }

    override fun getItemCount(): Int {
        return filteredList!!.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(filteredList!![position], context)
    }

    inner class Holder(itemView: View?, itemClick: (noticeItem) -> Unit) : RecyclerView.ViewHolder(itemView!!){
        val noticeTitle = itemView?.findViewById<TextView>(R.id.noticeTitle)
        val noticeDate = itemView?.findViewById<TextView>(R.id.noticeDate)
        val read = itemView?.findViewById<TextView>(R.id.read)

        fun bind(noticeItem : noticeItem, context: Context){
            noticeTitle?.text = noticeItem.title
            noticeDate?.text = noticeItem.date
            read?.text = "조회 : " +  noticeItem.read

            itemView.setOnClickListener{itemClick(noticeItem)}
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filteredList = noticeList
                } else {
                    val filteringList = ArrayList<noticeItem>()

                    for (row in noticeList) {
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
                filteredList = filterResults.values as ArrayList<noticeItem>
                notifyDataSetChanged()
            }
        }
    }
}
