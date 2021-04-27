package kr.ac.jbnu.coe.ui.more

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.jbnu.coe.R

class myFeedbackListAdapter(val context : Context, val feedbackList : ArrayList<myFeedbackItem>, val itemClick : (myFeedbackItem) -> Unit) : RecyclerView.Adapter<myFeedbackListAdapter.Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_myfeedbackitem, parent, false)
        return Holder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return feedbackList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(feedbackList[position], context)
    }

    inner class Holder(itemView: View?, itemClick: (myFeedbackItem) -> Unit) : RecyclerView.ViewHolder(itemView!!){
        val title = itemView?.findViewById<TextView>(R.id.feedbackTitle)
        val category = itemView?.findViewById<TextView>(R.id.category)
        val type = itemView?.findViewById<TextView>(R.id.type)
        val date = itemView?.findViewById<TextView>(R.id.dateTime)
        val status = itemView?.findViewById<TextView>(R.id.status)

        fun bind(feedbackItem : myFeedbackItem, context: Context){
            title?.text = feedbackItem.feedbackTitle
            date?.text = feedbackItem.date
            category?.text = feedbackItem.category
            type?.text = feedbackItem.type

            if(feedbackItem.status == "true"){
                status?.text = "답변 완료"
                status?.setBackgroundResource(R.drawable.background_status_ok)
            }

            else{
                status?.text = "답변 대기"
                status?.setBackgroundResource(R.drawable.background_status_fail)
            }

            itemView.setOnClickListener{itemClick(feedbackItem)}
        }
    }
}