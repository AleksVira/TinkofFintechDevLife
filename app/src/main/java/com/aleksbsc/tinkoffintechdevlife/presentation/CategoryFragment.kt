package com.aleksbsc.tinkoffintechdevlife.presentation

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.aleksbsc.tinkoffintechdevlife.GlideApp
import com.aleksbsc.tinkoffintechdevlife.data.DevLifePictureData
import com.aleksbsc.tinkoffintechdevlife.data.MemesCategory
import com.aleksbsc.tinkoffintechdevlife.databinding.FragmentPageBinding
import com.aleksbsc.tinkoffintechdevlife.extensions.setOnSingleClick
import com.aleksbsc.tinkoffintechdevlife.helpers.Constants.GLIDE_ERROR
import com.aleksbsc.tinkoffintechdevlife.helpers.Constants.ITEM_TYPE
import com.aleksbsc.tinkoffintechdevlife.helpers.LoadingState
import com.aleksbsc.tinkoffintechdevlife.vm.DevLifeViewModel
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CategoryFragment : Fragment() {

    private var _binding: FragmentPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var itemType: MemesCategory

    val devLifeViewModel: DevLifeViewModel by sharedViewModel()

    companion object {

        fun getInstance(itemType: MemesCategory) = CategoryFragment().apply {
            arguments = bundleOf(ITEM_TYPE to itemType.name)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { bundle ->
            itemType =
                bundle.getString(ITEM_TYPE)?.let { enumValueOf<MemesCategory>(it) }
                    ?: MemesCategory.LATEST
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        devLifeViewModel.loadingState.observe(viewLifecycleOwner, { loadingState ->
            when (loadingState.status) {
                LoadingState.Status.RUNNING -> {
                    binding.progressBar.isVisible = true
                }
                LoadingState.Status.SUCCESS -> {
                    binding.progressBar.isVisible = false
                }
                LoadingState.Status.FAILED -> {
                    binding.progressBar.isVisible = false

                    if (loadingState.msg == GLIDE_ERROR) {
                        binding.ivLoadingError.isVisible = true
                        binding.ivLoadingError.setOnSingleClick {
                            devLifeViewModel.reloadClicked()
                            binding.ivLoadingError.isVisible = false
                        }
                    }
                }
            }
        })


        when (itemType) {
            MemesCategory.LATEST -> {
                devLifeViewModel.currentLatestDevLifePictureData.observe(
                    viewLifecycleOwner,
                    { devLifeData ->
                        bindDevLifeData(devLifeData, view)
                    })
            }
            MemesCategory.RANDOM -> {
                devLifeViewModel.currentRandomDevLifePictureData.observe(
                    viewLifecycleOwner,
                    { devLifeData ->
                        bindDevLifeData(devLifeData, view)
                    })
            }
            MemesCategory.TOP -> {
                devLifeViewModel.currentTopDevLifePictureData.observe(
                    viewLifecycleOwner,
                    { devLifeData ->
                        bindDevLifeData(devLifeData, view)
                    })
            }
        }
    }

    private fun bindDevLifeData(devLifeData: DevLifePictureData?, view: View) {
        devLifeData?.let {
            if (it.pictureUrl.isNotEmpty()) {
                devLifeViewModel.loadingStart()

                GlideApp.with(view)
                    .load(it.pictureUrl)
                    .centerCrop()
                    .listener(object : RequestListener<Drawable?> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable?>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            devLifeViewModel.errorImageLoading()
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable?>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            devLifeViewModel.loadingSuccess()
                            binding.ivLoadingError.isVisible = false
                            return false
                        }
                    })
                    .into(binding.ivDevLife)
            }
            binding.tvDescription.text = it.description
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}