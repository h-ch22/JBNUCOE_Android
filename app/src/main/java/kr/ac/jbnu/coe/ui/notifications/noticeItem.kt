package kr.ac.jbnu.coe.ui.notifications

import com.google.firebase.storage.StorageReference
import java.io.Serializable

class noticeItem(val img : StorageReference, val title : String, val date : String) : Serializable{

}