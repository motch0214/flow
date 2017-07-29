package com.eighthours.flow.presenter.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.eighthours.flow.presenter.fragment.AssetPositionListFragment
import com.eighthours.flow.presenter.fragment.PLPositionListFragment
import com.eighthours.flow.presenter.utility.Formatter
import io.reactivex.subjects.BehaviorSubject

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 * [FragmentPagerAdapter] will keep every loaded fragment in memory.
 * If this becomes too memory intensive, it
 * may be best to switch to a
 * [android.support.v4.app.FragmentStatePagerAdapter].
 */
class SectionsPagerAdapter(
        fragmentManager: FragmentManager)
    : FragmentPagerAdapter(fragmentManager) {

    val pageChanges: BehaviorSubject<Page> = BehaviorSubject.createDefault(Page.values()[0])

    val listener = object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(index: Int) {
            pageChanges.onNext(Page.values()[index])
        }

        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }
    }

    override fun getCount(): Int {
        return Page.values().size
    }

    override fun getItem(index: Int): Fragment {
        val page = Page.values()[index]
        return when (page) {
            Page.ASSET_LIST -> AssetPositionListFragment()
            Page.PL_LIST -> PLPositionListFragment()
        }
    }

    override fun getPageTitle(index: Int): String {
        val page = Page.values()[index]
        return Formatter.format(page)
    }
}

enum class Page {
    PL_LIST,
    ASSET_LIST
}
