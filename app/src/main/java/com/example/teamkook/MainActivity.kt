package com.example.teamkook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        
        //test
        //my name is noa
        //new master
        //master test

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.mipmap.menu_icon)

        var main_adapter = Main_pager_Adapter(supportFragmentManager)

        VP.adapter = main_adapter
        VP.offscreenPageLimit = 3

        tabs_main.setupWithViewPager(VP)
        tabs_main.getTabAt(0)?.setIcon(resources.getDrawable(R.mipmap.menu_icon))
        tabs_main.getTabAt(1)?.setIcon(resources.getDrawable(R.mipmap.menu_icon))
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
