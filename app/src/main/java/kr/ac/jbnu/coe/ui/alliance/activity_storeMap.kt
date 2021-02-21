package kr.ac.jbnu.coe.ui.alliance

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import kr.ac.jbnu.coe.R
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class activity_storeMap : AppCompatActivity(), OnMapReadyCallback{
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var locationSource: FusedLocationSource
    lateinit var storeName : String
    lateinit var benefit : String
    lateinit var markerArray : Array<Marker>
    val LOCATION_PERMISSION_REQUEST_CODE = 1
    lateinit var mapFragment : MapFragment
    private lateinit var mLocationSource : FusedLocationSource
    val db = Firebase.firestore
    val marker = Marker()

    private val PERMISSIONS = arrayOf<String>(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var mNaverMap : NaverMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_storemap)

        marker.map = null
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragmentManager = supportFragmentManager
        mapFragment = fragmentManager.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also{
                fragmentManager.beginTransaction().add(R.id.map, it).commit()
            }

        mLocationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        mapFragment.getMapAsync(this)

        if(intent.getStringExtra("storeName").toString() != "" &&
            intent.getStringExtra("storeName") != null &&
            intent.getStringExtra("benefit").toString() != "" &&
            intent.getStringExtra("benefit") != null){
            storeName = intent.getStringExtra("storeName").toString()
            benefit = intent.getStringExtra("benefit").toString()

            setMarker(storeName = storeName, benefit = benefit)
        }

        else{
            val storeList = intent.getStringArrayListExtra("alliance")
            if (storeList != null) {
                markerArray = Array(storeList.size, {Marker()})
                setMarker(storeList)
            }
        }
    }

    override fun onMapReady(p0: NaverMap) {
        mNaverMap = p0
        mNaverMap.locationSource = mLocationSource
        mNaverMap.locationTrackingMode = LocationTrackingMode.Face
        mNaverMap.isNightModeEnabled = true

        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mNaverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
    }

    fun setMarker(storeName : String, benefit : String){
        val docRef = db.collection("location").document(storeName)
        docRef.get().addOnCompleteListener{task ->
            if(task.isSuccessful){
                val document = task.result

                if(document != null){
                    val location = document.get("location").toString()
                    val loc_split = location.split(", ")
                    val lat = loc_split[0]
                    val lng = loc_split[1]

                    marker.position = LatLng(lat.toDouble(), lng.toDouble())
                    marker.captionText = storeName
                    marker.captionColor = Color.BLUE
                    marker.subCaptionText = benefit
                    marker.isIconPerspectiveEnabled = true

                    marker.map = mNaverMap
                }
            }
        }
    }

    fun setMarker(allianceList : ArrayList<String>){
        for(i in allianceList.indices){
            val docRef = db.collection("location").document(allianceList.get(i))
            docRef.get().addOnCompleteListener{task ->
                if(task.isSuccessful){
                    val document = task.result

                    if(document != null){
                        val location = document.get("location").toString()
                        setMarkers(location = location, i = i, name = allianceList.get(i))
                    }
                }
            }
        }
    }

    fun setMarkers(location : String, i : Int, name : String){
        val loc_split = location.split(", ")
        val lat = loc_split[0]
        val lng = loc_split[1]

        markerArray[i].position = LatLng(lat.toDouble(), lng.toDouble())
        markerArray[i].captionText = name
        markerArray[i].captionColor = Color.BLUE
        markerArray[i].isIconPerspectiveEnabled = true

        markerArray[i].map = mNaverMap
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}