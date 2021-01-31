package com.aleksbsc.tinkoffintechdevlife.presentation

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.TextViewCompat
import com.aleksbsc.tinkoffintechdevlife.R
import com.aleksbsc.tinkoffintechdevlife.data.MemesCategory
import com.aleksbsc.tinkoffintechdevlife.databinding.ActivityMainBinding
import com.aleksbsc.tinkoffintechdevlife.extensions.setOnSingleClick
import com.aleksbsc.tinkoffintechdevlife.vm.DevLifeViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val devLifePagerAdapter = DevLifePagerAdapter(this)
    private var currentTab: Int = -1
    private val devLifeViewModel: DevLifeViewModel by viewModel()
    private val bottomSheetNetworkError by lazy { ErrorBottomDialogFragment() }

    companion object {
        const val BOTTOM_SHEET_ERROR = "bottom_sheet_error"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        initListeners()
    }

    private fun initUI() {
        with(binding) {
            viewPager.apply {
                adapter = devLifePagerAdapter
                offscreenPageLimit = 1
            }

            pagerTabs.addOnTabSelectedListener(tabListener)
            TabLayoutMediator(pagerTabs, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.category_latest)
                    1 -> getString(R.string.category_random)
                    else -> getString(R.string.category_top)
                }
            }.attach()
        }
        setButtonBackState(false)
    }

    private fun initListeners() {
        binding.btForward.setOnSingleClick {
            val type = selectPicturesCategory(currentTab)
            devLifeViewModel.fetchNextPicture(type)
        }

        binding.btBack.setOnSingleClick {
            val type = selectPicturesCategory(currentTab)
            devLifeViewModel.backIconClicked(type)
        }

        devLifeViewModel.backButtonState.observe(this, { event ->
            event?.handle { pictureIndex ->
                setButtonBackState(pictureIndex > 0)
            }
        })

        devLifeViewModel.showErrorWarning.observe(this, { event ->
            event?.handle { itemType ->
                if (itemType == devLifeViewModel.getCurrentType()) {
                    Timber.d("MyTAG_MainActivity_initListeners(): BINGO !!!")
                    bottomSheetNetworkError.show(supportFragmentManager, BOTTOM_SHEET_ERROR)
                }
            }
        })

    }


    private fun setButtonBackState(isActive: Boolean) {
        with(binding.btBack) {
            isClickable = isActive
            if (isActive) {
                setIconTintResource(R.color.yellow)
            } else {
                setIconTintResource(R.color.gray)
            }
        }
    }

    private fun selectPicturesCategory(tab: Int): MemesCategory {
        return when (tab) {
            0 -> MemesCategory.LATEST
            1 -> MemesCategory.RANDOM
            else -> MemesCategory.TOP
        }
    }


    private val tabListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab) {
            Timber.e("Reselect tab")
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
            val views = arrayListOf<View>()
            tab.view.findViewsWithText(views, tab.text, View.FIND_VIEWS_WITH_TEXT)
            views.forEach { view ->
                if (view is TextView) {
                    TextViewCompat.setTextAppearance(view, R.style.tab_unselected_text)
                }
            }
        }

        override fun onTabSelected(tab: TabLayout.Tab) {
            currentTab = tab.position
            devLifeViewModel.saveCurrentType(selectPicturesCategory(currentTab))
            val views = arrayListOf<View>()
            tab.view.findViewsWithText(views, tab.text, View.FIND_VIEWS_WITH_TEXT)
            views.forEach { view ->
                if (view is TextView) {
                    TextViewCompat.setTextAppearance(view, R.style.tab_selected_text)
                }
            }
        }
    }


}