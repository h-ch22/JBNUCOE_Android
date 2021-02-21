package kr.ac.jbnu.coe.ui.alliance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kr.ac.jbnu.coe.R

class PagerAdapter : PagerAdapter {
    var mContext: Context? = null
    private val imgList = arrayOfNulls<StorageReference>(3)

    constructor() {}
    constructor(context: Context?) {
        mContext = context
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.layout_pager, container, false)
        val imageView: ImageView = view?.findViewById(R.id.ad) as ImageView
        val storageReference = FirebaseStorage.getInstance().reference
        imgList[0] = storageReference.child("ad/ad_1.png")
        imgList[1] = storageReference.child("ad/ad_2.png")
        imgList[2] = storageReference.child("ad/ad_3.png")
        Glide.with(mContext!!).load(imgList[position]).transform(CenterCrop(), RoundedCorners(10)).into(imageView)

        container.addView(view)
        return view
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return 3
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as View
    }
}