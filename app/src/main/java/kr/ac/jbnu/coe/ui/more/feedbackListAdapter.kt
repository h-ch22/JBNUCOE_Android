package kr.ac.jbnu.coe.ui.more

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.jbnu.coe.R

class feedbackListAdapter(val context : Context, val feedbackList : ArrayList<feedbackItem>, val itemClick : (feedbackItem) -> Unit) : RecyclerView.Adapter<feedbackListAdapter.Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_feedbackitem, parent, false)
        return Holder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return feedbackList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(feedbackList[position], context)
    }

    inner class Holder(itemView: View?, itemClick: (feedbackItem) -> Unit) : RecyclerView.ViewHolder(itemView!!){
        val title = itemView?.findViewById<TextView>(R.id.feedbackTitle)
        val author = itemView?.findViewById<TextView>(R.id.author)
        val category = itemView?.findViewById<TextView>(R.id.category)
        val type = itemView?.findViewById<TextView>(R.id.type)
        val date = itemView?.findViewById<TextView>(R.id.dateTime)

        fun bind(feedbackItem : feedbackItem, context: Context){
            title?.text = feedbackItem.feedbackTitle
            date?.text = feedbackItem.date
            author?.text = feedbackItem.author
            category?.text = feedbackItem.category
            type?.text = feedbackItem.type

            itemView.setOnClickListener{itemClick(feedbackItem)}
        }
    }
}