package kr.ac.jbnu.coe.ui.more

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.royrodriguez.transitionbutton.TransitionButton
import kr.ac.jbnu.coe.R

class activity_campusMap : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {
    lateinit var btn_all : TransitionButton
    lateinit var btn_1 : TransitionButton
    lateinit var btn_2 : TransitionButton
    lateinit var btn_3 : TransitionButton
    lateinit var btn_4 : TransitionButton
    lateinit var btn_5 : TransitionButton
    lateinit var btn_6 : TransitionButton
    lateinit var btn_7 : TransitionButton
    lateinit var btn_8 : TransitionButton
    lateinit var btn_9 : TransitionButton
    lateinit var btn_cafe : TransitionButton
    lateinit var btn_restaurant : TransitionButton
    lateinit var btn_convenienceStore : TransitionButton
    lateinit var markerArray : Array<Marker>
    lateinit var convenienceArray : Array<Marker>
    lateinit var restaurantArray : Array<Marker>
    lateinit var cafeArray : Array<Marker>
    val LOCATION_PERMISSION_REQUEST_CODE = 1
    lateinit var mapFragment : MapFragment
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var locationSource: FusedLocationSource
    lateinit var btn_showInside : TransitionButton
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
        setContentView(R.layout.layout_campusmap)

        markerArray = Array(9, {Marker()})
        convenienceArray = Array(6, {Marker()})
        cafeArray = Array(5, {Marker()})
        restaurantArray = Array(4, {Marker()})
        btn_all = findViewById(R.id.btn_all)
        btn_1 = findViewById(R.id.btn_build1)
        btn_2 = findViewById(R.id.btn_build2)
        btn_3 = findViewById(R.id.btn_build3)
        btn_4 = findViewById(R.id.btn_build4)
        btn_5 = findViewById(R.id.btn_build5)
        btn_6 = findViewById(R.id.btn_build6)
        btn_7 = findViewById(R.id.btn_build7)
        btn_8 = findViewById(R.id.btn_build8)
        btn_9 = findViewById(R.id.btn_build9)
        btn_showInside = findViewById(R.id.btn_showInside)
        btn_cafe = findViewById(R.id.btn_cafe)
        btn_restaurant = findViewById(R.id.btn_restaurant)
        btn_convenienceStore = findViewById(R.id.btn_convenience)

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

        btn_1.setOnClickListener(this)
        btn_2.setOnClickListener(this)
        btn_3.setOnClickListener(this)
        btn_4.setOnClickListener(this)
        btn_5.setOnClickListener(this)
        btn_6.setOnClickListener(this)
        btn_7.setOnClickListener(this)
        btn_8.setOnClickListener(this)
        btn_9.setOnClickListener(this)
        btn_all.setOnClickListener(this)
        btn_convenienceStore.setOnClickListener(this)
        btn_restaurant.setOnClickListener(this)
        btn_cafe.setOnClickListener(this)
    }

    override fun onMapReady(p0: NaverMap) {
        mNaverMap = p0
        mNaverMap.locationSource = mLocationSource
        mNaverMap.locationTrackingMode = LocationTrackingMode.Face
        mNaverMap.isNightModeEnabled = true

        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)

        setMarkers()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun setMarkers(){
        markerArray[0].position = LatLng(35.84659343991913, 127.13246492944916)
        markerArray[0].captionText = "공대 1호관"
        markerArray[0].captionColor = Color.parseColor("#ff756b")
        markerArray[0].iconTintColor = Color.parseColor("#ff756b")
        markerArray[0].isIconPerspectiveEnabled = true
        markerArray[0].map = mNaverMap

        markerArray[1].position = LatLng(35.8469108675225, 127.13159321153508)
        markerArray[1].captionText = "공대 2호관"
        markerArray[1].captionColor = Color.parseColor("#ffb969")
        markerArray[1].iconTintColor = Color.parseColor("#ffb969")
        markerArray[1].isIconPerspectiveEnabled = true
        markerArray[1].map = mNaverMap

        markerArray[2].position = LatLng(35.84687825515616, 127.13355122409637)
        markerArray[2].captionText = "공대 3호관"
        markerArray[2].captionColor = Color.parseColor("#dbba00")
        markerArray[2].iconTintColor = Color.parseColor("#dbba00")
        markerArray[2].isIconPerspectiveEnabled = true
        markerArray[2].map = mNaverMap

        markerArray[3].position = LatLng(35.84746092741513, 127.13248102271213)
        markerArray[3].captionText = "공대 4호관"
        markerArray[3].captionColor = Color.parseColor("#75a300")
        markerArray[3].iconTintColor = Color.parseColor("#75a300")
        markerArray[3].isIconPerspectiveEnabled = true
        markerArray[3].map = mNaverMap

        markerArray[4].position = LatLng(35.84752397751492, 127.13129816855798)
        markerArray[4].captionText = "공대 5호관"
        markerArray[4].captionColor = Color.parseColor("#2bc23a")
        markerArray[4].iconTintColor = Color.parseColor("#2bc23a")
        markerArray[4].isIconPerspectiveEnabled = true
        markerArray[4].map = mNaverMap

        markerArray[5].position = LatLng(35.84704131685122, 127.13438807329769)
        markerArray[5].captionText = "공대 6호관"
        markerArray[5].captionColor = Color.parseColor("#42d6b1")
        markerArray[5].iconTintColor = Color.parseColor("#42d6b1")
        markerArray[5].isIconPerspectiveEnabled = true
        markerArray[5].map = mNaverMap

        markerArray[6].position = LatLng(35.846039025631754, 127.13438539110072)
        markerArray[6].captionText = "공대 7호관"
        markerArray[6].captionColor = Color.parseColor("#ff756b")
        markerArray[6].iconTintColor = Color.parseColor("#ff756b")
        markerArray[6].isIconPerspectiveEnabled = true
        markerArray[6].map = mNaverMap

        markerArray[7].position = LatLng(35.84824157127009, 127.13332022855558)
        markerArray[7].captionText = "공대 8호관"
        markerArray[7].captionColor = Color.parseColor("#4ac9ff")
        markerArray[7].iconTintColor = Color.parseColor("#4ac9ff")
        markerArray[7].isIconPerspectiveEnabled = true
        markerArray[7].map = mNaverMap

        markerArray[8].position = LatLng(35.84759802889204, 127.13363136479568)
        markerArray[8].captionText = "공대 9호관"
        markerArray[8].captionColor = Color.parseColor("#ff756b")
        markerArray[8].iconTintColor = Color.parseColor("#ff756b")
        markerArray[8].isIconPerspectiveEnabled = true
        markerArray[8].map = mNaverMap

        convenienceArray[0].position = LatLng(35.847069711477204, 127.13254775238075)
        convenienceArray[0].captionText = "공대 CU"
        convenienceArray[0].captionColor = Color.parseColor("#001c82")
        convenienceArray[0].iconTintColor = Color.parseColor("#001c82")
        convenienceArray[0].isIconPerspectiveEnabled = true
        convenienceArray[0].map = mNaverMap

        convenienceArray[1].position = LatLng(35.84547855919743, 127.13077103313783)
        convenienceArray[1].captionText = "법대 CU"
        convenienceArray[1].captionColor = Color.parseColor("#001c82")
        convenienceArray[1].iconTintColor = Color.parseColor("#001c82")
        convenienceArray[1].isIconPerspectiveEnabled = true
        convenienceArray[1].map = mNaverMap

        convenienceArray[2].position = LatLng(35.84574598492688, 127.1283329051472)
        convenienceArray[2].captionText = "학생회관 CU"
        convenienceArray[2].captionColor = Color.parseColor("#001c82")
        convenienceArray[2].iconTintColor = Color.parseColor("#001c82")
        convenienceArray[2].isIconPerspectiveEnabled = true
        convenienceArray[2].map = mNaverMap

        convenienceArray[3].position = LatLng(35.84813898718133, 127.13233541868455)
        convenienceArray[3].captionText = "중앙도서관 CU"
        convenienceArray[3].captionColor = Color.parseColor("#001c82")
        convenienceArray[3].iconTintColor = Color.parseColor("#001c82")
        convenienceArray[3].isIconPerspectiveEnabled = true
        convenienceArray[3].map = mNaverMap

        convenienceArray[4].position = LatLng(35.84576958488431, 127.12797274094895)
        convenienceArray[4].captionText = "교보문고"
        convenienceArray[4].captionColor = Color.parseColor("#001c82")
        convenienceArray[4].iconTintColor = Color.parseColor("#001c82")
        convenienceArray[4].isIconPerspectiveEnabled = true
        convenienceArray[4].map = mNaverMap

        convenienceArray[5].position = LatLng(35.846276169210995, 127.12851722936914)
        convenienceArray[5].captionText = "우체국"
        convenienceArray[5].captionColor = Color.parseColor("#001c82")
        convenienceArray[5].iconTintColor = Color.parseColor("#001c82")
        convenienceArray[5].isIconPerspectiveEnabled = true
        convenienceArray[5].map = mNaverMap

        cafeArray[0].position = LatLng(35.844482026828615, 127.1302620711385)
        cafeArray[0].captionText = "카페베네"
        cafeArray[0].captionColor = Color.parseColor("#c52fd6")
        cafeArray[0].iconTintColor = Color.parseColor("#c52fd6")
        cafeArray[0].isIconPerspectiveEnabled = true
        cafeArray[0].map = mNaverMap

        cafeArray[1].position = LatLng(35.844690752047576, 127.13020306254326)
        cafeArray[1].captionText = "뉴실크로드센터 카페"
        cafeArray[1].captionColor = Color.parseColor("#c52fd6")
        cafeArray[1].iconTintColor = Color.parseColor("#c52fd6")
        cafeArray[1].isIconPerspectiveEnabled = true
        cafeArray[1].map = mNaverMap

        cafeArray[2].position = LatLng(35.84539954401292, 127.13132959031765)
        cafeArray[2].captionText = "법대 카페"
        cafeArray[2].captionColor = Color.parseColor("#c52fd6")
        cafeArray[2].iconTintColor = Color.parseColor("#c52fd6")
        cafeArray[2].isIconPerspectiveEnabled = true
        cafeArray[2].map = mNaverMap

        cafeArray[3].position = LatLng(35.846804491923734, 127.12855478031484)
        cafeArray[3].captionText = "건지광장 카페"
        cafeArray[3].captionColor = Color.parseColor("#c52fd6")
        cafeArray[3].iconTintColor = Color.parseColor("#c52fd6")
        cafeArray[3].isIconPerspectiveEnabled = true
        cafeArray[3].map = mNaverMap

        cafeArray[4].position = LatLng(35.84583481051558, 127.12824096184558)
        cafeArray[4].captionText = "학생회관 카페"
        cafeArray[4].captionColor = Color.parseColor("#c52fd6")
        cafeArray[4].iconTintColor = Color.parseColor("#c52fd6")
        cafeArray[4].isIconPerspectiveEnabled = true
        cafeArray[4].map = mNaverMap

        restaurantArray[0].position = LatLng(35.844403007581974, 127.13039122491996)
        restaurantArray[0].captionText = "정담원"
        restaurantArray[0].captionColor = Color.parseColor("#8254ff")
        restaurantArray[0].iconTintColor = Color.parseColor("#8254ff")
        restaurantArray[0].isIconPerspectiveEnabled = true
        restaurantArray[0].map = mNaverMap

        restaurantArray[1].position = LatLng(35.845083537433574, 127.13138096003564)
        restaurantArray[1].captionText = "진수당"
        restaurantArray[1].captionColor = Color.parseColor("#8254ff")
        restaurantArray[1].iconTintColor = Color.parseColor("#8254ff")
        restaurantArray[1].isIconPerspectiveEnabled = true
        restaurantArray[1].map = mNaverMap

        restaurantArray[2].position = LatLng(35.84623585899951, 127.12846808106733)
        restaurantArray[2].captionText = "학생회관"
        restaurantArray[2].captionColor = Color.parseColor("#8254ff")
        restaurantArray[2].iconTintColor = Color.parseColor("#8254ff")
        restaurantArray[2].isIconPerspectiveEnabled = true
        restaurantArray[2].map = mNaverMap

        restaurantArray[3].position = LatLng(35.84762427790041, 127.13435721008604)
        restaurantArray[3].captionText = "후생관"
        restaurantArray[3].captionColor = Color.parseColor("#8254ff")
        restaurantArray[3].iconTintColor = Color.parseColor("#8254ff")
        restaurantArray[3].isIconPerspectiveEnabled = true
        restaurantArray[3].map = mNaverMap
    }

    override fun onClick(v: View?) {
        if (v != null){
            for (i in 0..markerArray.size-1){
                markerArray.get(i).map = null
            }

            for (i in 0..restaurantArray.size-1){
                restaurantArray.get(i).map = null
            }

            for (i in 0..cafeArray.size-1){
                cafeArray.get(i).map = null
            }

            for (i in 0..convenienceArray.size-1){
                convenienceArray.get(i).map = null
            }

            if(v.id == R.id.btn_all){
                setMarkers()
                btn_showInside.visibility = View.INVISIBLE
            }

            if(v.id == R.id.btn_build1){
                markerArray[0].map = mNaverMap
                btn_showInside.visibility = View.VISIBLE
                btn_showInside.setOnClickListener(View.OnClickListener {
                    val intent = Intent(this, activity_showInside :: class.java)
                    intent.putExtra("dept", "공과대학 1호관")
                    startActivity(intent)
                    finish()
                })
            }

            if(v.id == R.id.btn_build2){
                markerArray[1].map = mNaverMap
                btn_showInside.visibility = View.VISIBLE
                btn_showInside.setOnClickListener(View.OnClickListener {
                    val intent = Intent(this, activity_showInside :: class.java)
                    intent.putExtra("dept", "공과대학 2호관")
                    startActivity(intent)
                    finish()
                })
            }

            if(v.id == R.id.btn_build3){
                markerArray[2].map = mNaverMap
                btn_showInside.visibility = View.VISIBLE
                btn_showInside.setOnClickListener(View.OnClickListener {
                    val intent = Intent(this, activity_showInside :: class.java)
                    intent.putExtra("dept", "공과대학 3호관")
                    startActivity(intent)
                    finish()
                })
            }

            if(v.id == R.id.btn_build4){
                markerArray[3].map = mNaverMap
                btn_showInside.visibility = View.VISIBLE
                btn_showInside.setOnClickListener(View.OnClickListener {
                    val intent = Intent(this, activity_showInside :: class.java)
                    intent.putExtra("dept", "공과대학 4호관")
                    startActivity(intent)
                    finish()
                })
            }

            if(v.id == R.id.btn_build5){
                markerArray[4].map = mNaverMap
                btn_showInside.visibility = View.VISIBLE
                btn_showInside.setOnClickListener(View.OnClickListener {
                    val intent = Intent(this, activity_showInside :: class.java)
                    intent.putExtra("dept", "공과대학 5호관")
                    startActivity(intent)
                    finish()
                })
            }

            if(v.id == R.id.btn_build6){
                markerArray[5].map = mNaverMap
                btn_showInside.visibility = View.VISIBLE
                btn_showInside.setOnClickListener(View.OnClickListener {
                    val intent = Intent(this, activity_showInside :: class.java)
                    intent.putExtra("dept", "공과대학 6호관")
                    startActivity(intent)
                    finish()
                })
            }

            if(v.id == R.id.btn_build7){
                markerArray[6].map = mNaverMap
                btn_showInside.visibility = View.VISIBLE
                btn_showInside.setOnClickListener(View.OnClickListener {
                    val intent = Intent(this, activity_showInside :: class.java)
                    intent.putExtra("dept", "공과대학 7호관")
                    startActivity(intent)
                    finish()
                })
            }

            if(v.id == R.id.btn_build8){
                markerArray[7].map = mNaverMap
                btn_showInside.visibility = View.VISIBLE
                btn_showInside.setOnClickListener(View.OnClickListener {
                    val intent = Intent(this, activity_showInside :: class.java)
                    intent.putExtra("dept", "공과대학 8호관")
                    startActivity(intent)
                    finish()
                })
            }

            if(v.id == R.id.btn_build9){
                markerArray[8].map = mNaverMap
                btn_showInside.visibility = View.VISIBLE
                btn_showInside.setOnClickListener(View.OnClickListener {
                    val intent = Intent(this, activity_showInside :: class.java)
                    intent.putExtra("dept", "공과대학 9호관")
                    startActivity(intent)
                    finish()
                })
            }

            if(v.id == R.id.btn_convenience){
                btn_showInside.visibility = View.INVISIBLE
                for(i in 0..convenienceArray.size - 1){
                    convenienceArray.get(i).map = mNaverMap
                }
            }

            if(v.id == R.id.btn_cafe){
                btn_showInside.visibility = View.INVISIBLE
                for(i in 0..cafeArray.size - 1){
                    cafeArray.get(i).map = mNaverMap
                }
            }

            if(v.id == R.id.btn_restaurant){
                btn_showInside.visibility = View.INVISIBLE
                for(i in 0..restaurantArray.size - 1){
                    restaurantArray.get(i).map = mNaverMap
                }
            }
        }
    }
}