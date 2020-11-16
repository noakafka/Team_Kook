package com.example.teamkook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    fun settingPermission(){
        var permis = object  : PermissionListener {
            //            어떠한 형식을 상속받는 익명 클래스의 객체를 생성하기 위해 다음과 같이 작성
            override fun onPermissionGranted() {
                Toast.makeText(this@MainActivity, "권한 허가", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(this@MainActivity, "권한 거부", Toast.LENGTH_SHORT)
                    .show()
                ActivityCompat.finishAffinity(this@MainActivity) // 권한 거부시 앱 종료
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permis)
            .setRationaleMessage("카메라 사진 권한 필요")
            .setDeniedMessage("카메라 권한 요청 거부")
            .setPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA)
            .check()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        settingPermission()
        //my name is noa
        //new master
        //branch
        //b_test

        //setSupportActionBar(toolbar)
        //supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        //supportActionBar!!.setHomeAsUpIndicator(R.mipmap.menu_icon)

        var main_adapter = Main_pager_Adapter(supportFragmentManager)

        VP.adapter = main_adapter
        VP.offscreenPageLimit = 3

        tabs_main.setupWithViewPager(VP)
        tabs_main.getTabAt(0)?.setIcon(resources.getDrawable(R.mipmap.menu_icon))
        tabs_main.getTabAt(1)?.setIcon(resources.getDrawable(R.mipmap.search))
        tabs_main.getTabAt(2)?.setIcon(resources.getDrawable(R.mipmap.menu_icon))
        tabs_main.getTabAt(3)?.setIcon(resources.getDrawable(R.mipmap.menu_icon))

        tabs_main.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
            }
        })


    }

    inner class Main_pager_Adapter : FragmentPagerAdapter {

        var data1 : Fragment = Fragment_A()
        var data2 : Fragment = Fragment_B(applicationContext)
        var data3 : Fragment = Fragment_C(applicationContext)
        var data4 : Fragment = Fragment_D(applicationContext)


        var tab_data : ArrayList<Fragment> = arrayListOf(data1,data2,data3,data4)

        constructor(fm : FragmentManager) : super(fm){

        }

        override fun getItem(position: Int): Fragment {
            return tab_data.get(position)
        }

        override fun getCount(): Int {
            return tab_data.size
        }
    }
}
