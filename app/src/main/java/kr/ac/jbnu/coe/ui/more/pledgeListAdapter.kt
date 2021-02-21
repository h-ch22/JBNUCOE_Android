package kr.ac.jbnu.coe.ui.more

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kr.ac.jbnu.coe.R
import kr.ac.jbnu.coe.ui.notifications.noticeItem

class pledgeListAdapter(val context : Context, val pledgeList : ArrayList<pledgeItem>, val itemClick : (pledgeItem) -> Unit) : RecyclerView.Adapter<pledgeListAdapter.Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_pledgeitem, parent, false)
        return Holder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return pledgeList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(pledgeList[position], context)
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
}