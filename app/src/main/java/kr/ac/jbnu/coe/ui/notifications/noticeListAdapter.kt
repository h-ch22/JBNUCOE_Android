package kr.ac.jbnu.coe.ui.notifications

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kr.ac.jbnu.coe.R
import kr.ac.jbnu.coe.ui.alliance.storeItem

class noticeListAdapter(val context : Context, val noticeList : ArrayList<noticeItem>, val itemClick : (noticeItem) -> Unit) : RecyclerView.Adapter<noticeListAdapter.Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_noticeitem, parent, false)
        return Holder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(noticeList[position], context)
    }

    inner class Holder(itemView: View?, itemClick: (noticeItem) -> Unit) : RecyclerView.ViewHolder(itemView!!){
        val noticeImg = itemView?.findViewById<ImageView>(R.id.img_notice)
        val noticeTitle = itemView?.findViewById<TextView>(R.id.noticeTitle)
        val noticeDate = itemView?.findViewById<TextView>(R.id.noticeDate)

        fun bind(noticeItem : noticeItem, context: Context){
            if(noticeItem.img.toString() != ""){
                if (noticeImg != null) {
                    Glide.with(context).load(noticeItem.img).apply(RequestOptions.bitmapTransform(
                        RoundedCorners(16)
                    )).into(noticeImg)
                }
            }

            noticeTitle?.text = noticeItem.title
            noticeDate?.text = noticeItem.date
            itemView.setOnClickListener{itemClick(noticeItem)}
        }
    }
}
