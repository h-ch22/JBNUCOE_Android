package kr.ac.jbnu.coe.ui.more

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.jbnu.coe.R

class deliveryListAdapter(val context : Context, val deliveryList : ArrayList<deliveryItem>) : RecyclerView.Adapter<deliveryListAdapter.Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_deliveryitem, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return deliveryList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(deliveryList[position])
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!){
        val title = itemView?.findViewById<TextView>(R.id.deliveryTitle)
        val waybill = itemView?.findViewById<TextView>(R.id.waybill)
        val status = itemView?.findViewById<TextView>(R.id.status)

        fun bind(deliveryItem : deliveryItem){
            title?.text = deliveryItem.date
            waybill?.text = deliveryItem.title

            if(deliveryItem.status == "true"){
                status?.text = "수령 완료"
                status?.setBackgroundResource(R.drawable.background_status_ok)
            }

            else{
                status?.text = "수령 대기"
                status?.setBackgroundResource(R.drawable.background_status_fail)
            }
        }
    }
}