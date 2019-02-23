package br.com.govote.android.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

internal class ViewPagerAdapter(fm: FragmentManager, private val fragmentList: List<Fragment>) :
  FragmentStatePagerAdapter(fm) {

  override fun getItem(position: Int): Fragment = fragmentList[position]
  override fun getCount(): Int = fragmentList.size
}
