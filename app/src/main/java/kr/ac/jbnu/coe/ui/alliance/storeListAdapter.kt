package kr.ac.jbnu.coe.ui.alliance

import android.content.Context
import android.graphics.Color
import android.util.Log
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

class storeListAdapter(val context : Context, val storeList : ArrayList<storeItem>, val itemClick : (storeItem) -> Unit) : RecyclerView.Adapter<storeListAdapter.Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_storeitem, parent, false)
        return Holder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return storeList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(storeList[position], context)
    }

    inner class Holder(itemView: View?, itemClick: (storeItem) -> Unit) : RecyclerView.ViewHolder(itemView!!){
        val storeImg = itemView?.findViewById<ImageView>(R.id.img_store)
        val storeName = itemView?.findViewById<TextView>(R.id.storeName)
        val benefit = itemView?.findViewById<TextView>(R.id.benefits)
        val status = itemView?.findViewById<TextView>(R.id.status)
        val breakTime = itemView?.findViewById<TextView>(R.id.breakTime)
        val closed = itemView?.findViewById<TextView>(R.id.closed)

        fun bind(storeItem : storeItem, context: Context){
            if(storeItem.img.toString() != ""){
                if (storeImg != null) {
                    Glide.with(context).load(storeItem.img).apply(RequestOptions.bitmapTransform(
                        RoundedCorners(16)
                    )).into(storeImg)
                }
            }

            storeName?.text = storeItem.title
            benefit?.text = storeItem.benefit
            itemView.setOnClickListener{itemClick(storeItem)}

            if(storeItem.isAvailable != "이용 시간을 알 수 없습니다."){
                status?.text = "이용 가능 시간 : " + storeItem.open + " ~ " + storeItem.close
                status?.setTextColor(Color.parseColor("#3F51B5"))
                status?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clock, 0, 0, 0)
                status?.compoundDrawables?.get(0)?.setTint(Color.parseColor("#3F51B5"))
            }

            if(storeItem.isAvailable == "이용 시간을 알 수 없습니다."){
                status?.text = "이용 시간을 알 수 없습니다."
                status?.setTextColor(Color.parseColor("#bf0000"))
                status?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_warning, 0, 0, 0)
                status?.compoundDrawables?.get(0)?.setTint(Color.parseColor("#bf0000"))
            }

            if(storeItem.breakTime != ""){
                breakTime?.text = "브레이크 타임 : " + storeItem.breakTime
                breakTime?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_break, 0, 0, 0)
                Log.d("break", storeItem.breakTime)
            }

            else{
                breakTime?.visibility = View.GONE
            }

            if(storeItem.closed != ""){
                closed?.text = "휴무 : " + storeItem.closed
                closed?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_closed, 0, 0, 0)
            }

            else{
                closed?.visibility = View.GONE
            }
        }
    }
}