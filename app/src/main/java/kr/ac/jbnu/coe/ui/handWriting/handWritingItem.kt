package kr.ac.jbnu.coe.ui.handWriting

import com.google.firebase.storage.StorageReference
import java.io.Serializable

class handWritingItem(val title : String, val recommend : String, val read : String, val dateTime : String, val name : String) : Serializable {

}