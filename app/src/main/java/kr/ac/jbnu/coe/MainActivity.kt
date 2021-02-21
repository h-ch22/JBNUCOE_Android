package kr.ac.jbnu.coe

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import kr.ac.jbnu.coe.ui.alliance.AllianceFragment
import kr.ac.jbnu.coe.ui.more.MoreFragment
import kr.ac.jbnu.coe.ui.notifications.NoticeFragment
import kr.ac.jbnu.coe.ui.sports.SportsFragment
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomBar : ChipNavigationBar = findViewById(R.id.bottomBar)

        if (savedInstanceState == null) {
            val fragment = AllianceFragment()
            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.main_fragment, fragment)
                .commit()

            bottomBar.setItemSelected(R.id.navigation_alliance, true)
        }

        bottomBar.setOnItemSelectedListener(object : ChipNavigationBar.OnItemSelectedListener {
            override fun onItemSelected(id: Int) {
                var fragment: Fragment? = null
                val option = when (id) {
                    R.id.navigation_alliance -> fragment = AllianceFragment()
                    R.id.navigation_sports -> fragment = SportsFragment()
                    R.id.navigation_notifications -> fragment = NoticeFragment()
                    R.id.navigation_more -> fragment = MoreFragment()
                    else -> fragment = AllianceFragment()
                }

                if (fragment != null) {
                    var fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, fragment)
                        .commit();
                } else {
                    Log.e("Fragment", "Error in creating fragment");
                }
            }
        })
    }
}
