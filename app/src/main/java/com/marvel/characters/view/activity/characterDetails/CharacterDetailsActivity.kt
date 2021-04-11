package com.marvel.characters.view.activity.characterDetails

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.marvel.characters.R
import com.marvel.characters.databinding.ActivityCharacterDetailsBinding
import com.marvel.characters.model.CharacterModel
import com.marvel.characters.remoteConnection.setup.isInternetAvailable
import com.marvel.characters.util.ActivityUtils.collapsingPercentage
import com.marvel.characters.util.ScreenSizeUtils
import com.marvel.characters.view.activity.baseActivity.BaseActivity
import com.stfalcon.imageviewer.StfalconImageViewer
import jp.wasabeef.glide.transformations.BlurTransformation


class CharacterDetailsActivity : BaseActivity(
    R.string.app_name, false, false, false,
    false, false, false, false, false,
), CharacterDetailsViewModel.Observer {

    lateinit var binding: ActivityCharacterDetailsBinding
    override fun doOnCreate(arg0: Bundle?) {
        binding =
            putContentView(R.layout.activity_character_details) as ActivityCharacterDetailsBinding
        binding.viewModel =
            ViewModelProvider(
                this,
                CharacterDetailsViewModelFactory(application)
            )
                .get(CharacterDetailsViewModel::class.java)
        binding.viewModel!!.baseViewModelObserver = baseViewModelObserver
        binding.viewModel!!.observer = this
        binding.lifecycleOwner = this
        binding.viewModel?.characterModel = intent.extras?.get("CharacterModel") as CharacterModel
        initializeViews()
        setListener()
    }


    override fun initializeViews() {
        var screenWidth: Int = ScreenSizeUtils().getScreenWidth(this)
        binding.viewModel!!.updateBooksAdapterColumnWidth(screenWidth)

        setSupportActionBar(binding.toolbarBookDetailsActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        binding.collapsingToolbarBookDetailsActivity.setExpandedTitleColor(Color.TRANSPARENT)
        binding.collapsingToolbarBookDetailsActivity.setCollapsedTitleTextColor(Color.TRANSPARENT)
        binding.appbar.setExpanded(true, false)

        binding.viewModel?.getHomeDataApi()
    }

    override fun setListener() {
        binding.viewModel!!.isShowError.removeObservers(this)
        binding.viewModel!!.isShowError.observe(this, Observer {
            if (it && lifecycle.currentState == Lifecycle.State.RESUMED) {
                binding.layoutError.ivError.setImageResource(
                    if (isInternetAvailable(this))
                        R.drawable.error_ice_creame_icon else R.drawable.error_router_connection_icon
                )
                binding.layoutError.tvErrorTitleConnection.text =
                    if (isInternetAvailable(this))
                        getString(R.string.oh_no) else getString(R.string.you_are_offline)
                binding.layoutError.tvErrorBodyConnection.text = if (isInternetAvailable(this))
                    binding.viewModel!!.connectionErrorMessage else getString(R.string.no_internet_connection)
            }
        })

        binding.layoutError.tvRetry.setOnClickListener {
            binding.viewModel!!.getHomeDataApi()
        }

        binding.scrlviewFragmentHome.viewTreeObserver.addOnScrollChangedListener {
            val scrollY = binding.scrlviewFragmentHome.scrollY
            binding.swipeRefreshHomeFragment.isEnabled =
                scrollY == 0 && binding.appbar.collapsingPercentage() < 0.5f
        }

        binding.scrlviewFragmentHome.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_CANCEL
                || motionEvent.action == MotionEvent.ACTION_UP
            ) {

                val scrollY = binding.scrlviewFragmentHome.scrollY
                binding.swipeRefreshHomeFragment.isEnabled =
                    scrollY == 0 && binding.appbar.collapsingPercentage() < 0.5f
            }
            false
        }
    }

    private var viewer: StfalconImageViewer<String>? = null
    override fun openImageViewer() {
        var list = ArrayList<String>()
        list.add(binding.viewModel!!.image.value!!)
        viewer = StfalconImageViewer.Builder(
            this, list
        ) { iv: ImageView, url: String ->
            Glide.with(this).load(url)
                .into(iv)
        }
            .withStartPosition(0)
            .withTransitionFrom(binding.imgviewImageBookDetailsActivity)
            .withImageChangeListener {
                viewer?.updateTransitionImage(binding.imgviewImageBookDetailsActivity)
            }.withDismissListener {
                //do any thing when dismiss
            }.withHiddenStatusBar(false)
            .withImagesMargin(R.dimen.padding_10)
            .withContainerPadding(R.dimen.padding_0)
            .allowZooming(true)
            .allowSwipeToDismiss(true)
            .show()
    }

}