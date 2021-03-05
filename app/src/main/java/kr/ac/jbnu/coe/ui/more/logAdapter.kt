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
import kr.ac.jbnu.coe.ui.notifications.noticeItem

class logAdapter(val context : Context, val logList : ArrayList<logItem>) : RecyclerView.Adapter<logAdapter.Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_logitem, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return logList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(logList[position], context)
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!){
        val itemName = itemView?.findViewById<TextView>(R.id.itemName)
        val isReturned = itemView?.findViewById<TextView>(R.id.isReturned)
        val date = itemView?.findViewById<TextView>(R.id.dateTime)

        fun bind(logItem : logItem, context: Context){
            itemName?.text = logItem.productName + ", " + logItem.num + "개"

            if(logItem.returned){
                isReturned?.text = "반납 여부 : 예"
                isReturned?.setTextColor(Color.parseColor("#32a852"))

            }

            else{
                isReturned?.text = "반납 여부 : 아니오"
                isReturned?.setTextColor(Color.parseColor("#bf0000"))
            }

            date?.text = logItem.date
        }
    }
}