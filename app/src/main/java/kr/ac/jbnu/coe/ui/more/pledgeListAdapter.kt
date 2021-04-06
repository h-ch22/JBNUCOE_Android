package kr.ac.jbnu.coe.ui.more

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.jbnu.coe.R
import kr.ac.jbnu.coe.ui.handWriting.handWritingItem

class pledgeListAdapter(val context : Context, val pledgeList : ArrayList<pledgeItem>, val itemClick : (pledgeItem) -> Unit) : RecyclerView.Adapter<pledgeListAdapter.Holder>(), Filterable {
    var filteredList : ArrayList<pledgeItem>? = null

    init{
        filteredList = pledgeList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_pledgeitem, parent, false)
        return Holder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return filteredList!!.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(filteredList!![position], context)
    }

    inner class Holder(itemView: View?, itemClick: (pledgeItem) -> Unit) : RecyclerView.ViewHolder(itemView!!){
        val title = itemView?.findViewById<TextView>(R.id.pledgeName)
        val status = itemView?.findViewById<TextView>(R.id.status)

        fun bind(pledgeItem : pledgeItem, context: Context){
            title?.text = pledgeItem.title

            if(pledgeItem.status == "true"){
                status?.text = "이행 완료"
                status?.setBackgroundResource(R.drawable.background_status_ok)
            }

            else{
                status?.text = "준비 중"
                status?.setBackgroundResource(R.drawable.background_status_fail)
            }

            itemView.setOnClickListener{itemClick(pledgeItem)}
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filteredList = pledgeList
                } else {
                    val filteringList = ArrayList<pledgeItem>()

                    for (row in pledgeList) {
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
                filteredList = filterResults.values as ArrayList<pledgeItem>
                notifyDataSetChanged()
            }
        }
    }
}