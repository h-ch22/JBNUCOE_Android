package kr.ac.jbnu.coe.ui.more

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kr.ac.jbnu.coe.R
import kr.ac.jbnu.coe.ui.alliance.storeItem
import kr.ac.jbnu.coe.ui.sports.sportsItem

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