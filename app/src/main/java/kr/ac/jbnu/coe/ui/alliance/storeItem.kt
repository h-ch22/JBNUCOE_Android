package kr.ac.jbnu.coe.ui.alliance

import com.google.firebase.storage.StorageReference
import java.io.Serializable

class storeItem(val img : StorageReference, val title : String, val benefit : String, val isAvailable : String) : Serializable{

}