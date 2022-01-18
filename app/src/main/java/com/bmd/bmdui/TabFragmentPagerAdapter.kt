package com.bmd.bmdui

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

/**
 *   FragmentPagerAdapter 已经过时，推荐使用ViewPager2 + FragmentStateAdapter
 *
 *   在使用 ViewPager +Fragment 实现懒加载时,
 *   可以通过设置 behavior  = FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
 */
class TabFragmentPagerAdapter<T>(
    fm: FragmentManager,
    behavior: Int? = FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
    val viewPager: ViewPager,
    val tabContainer: ViewGroup,
    private val mPages: List<Page<T>>,
    val mTabRes: Int
) : FragmentPagerAdapter(fm, behavior!!), ViewPager.OnPageChangeListener {

    override fun getCount(): Int {
        return mPages.size
    }

    override fun getItem(position: Int): Fragment {
        return mPages[position].fragment
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
    }

    override fun onPageScrollStateChanged(state: Int) {

    }


}

/**
 *  tab 集成
 *  fragment
 *  data  每个tab的数据 Entity
 *  adapter tab 的布局属性，可随意定制
 */
data class Page<T>(
    var fragment: Fragment, var data: T, var adapter: TabAdapter<T>
)

/**
 *   tab对象的属性
 */
interface TabAdapter<T> {
    // tab 绑定数据  首页对应图片
    fun onBindData(view: View, data: T, selected: Boolean)

    // 双击tab - 一般是实现刷新或者 列表滑动到表头
    fun onDoubleTap()
}