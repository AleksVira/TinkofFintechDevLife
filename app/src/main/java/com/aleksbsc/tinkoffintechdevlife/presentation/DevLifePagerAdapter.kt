package com.aleksbsc.tinkoffintechdevlife.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aleksbsc.tinkoffintechdevlife.data.MemesCategory

class DevLifePagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val type = when (position) {
            0 -> MemesCategory.LATEST
            1 -> MemesCategory.RANDOM
            else -> MemesCategory.TOP
        }
        return CategoryFragment.getInstance(type)
    }

}
