package com.marvel.characters.view.activity.main

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.stfalcon.imageviewer.StfalconImageViewer
import com.marvel.characters.R
import com.marvel.characters.databinding.ActivityMainBinding
import com.marvel.characters.posterUtil.PosterOverlayView
import com.marvel.characters.util.DataProvider
import com.marvel.characters.util.RequestCodeCaptureActivity
import com.marvel.characters.util.ScreenSizeUtils
import com.marvel.characters.view.activity.baseActivity.BaseActivity
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.io.File


class MainActivity : BaseActivity(
    R.string.app_name, true, false, true,
    false, false, false, false,
), MainViewModel.Observer {

    lateinit var binding: ActivityMainBinding
    override fun doOnCreate(arg0: Bundle?) {
        binding = putContentView(R.layout.activity_main) as ActivityMainBinding
        binding.viewModel =
            ViewModelProvider(
                this,
                MainViewModelFactory(application)
            )
                .get(MainViewModel::class.java)
        binding.viewModel!!.baseViewModelObserver = baseViewModelObserver
        binding.viewModel!!.observer = this
        binding.lifecycleOwner = this
        initializeViews()
        setListener()
    }


    override fun initializeViews() {
        var isLandScape = false
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isLandScape = true
            binding.recyclerView.layoutManager = GridLayoutManager(this, 4)
//            var screenWidth: Int = ScreenSizeUtils().getScreenWidth(this)
//            binding.viewModel!!.updateBooksAdapterColumnWidth(screenWidth)
        } else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            isLandScape = false
            binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
//            var screenWidth: Int = ScreenSizeUtils().getScreenWidth(this)
//            binding.viewModel!!.updateBooksAdapterColumnWidth(screenWidth)
        }

        var screenWidth: Int = ScreenSizeUtils().getScreenWidth(this)
        binding.viewModel!!.updateBooksAdapterColumnWidth(screenWidth, isLandScape)
    }


    override fun setListener() {
    }

    private var doubleBackToExitPressedOnce: Boolean = false

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            if (doubleBackToExitPressedOnce) {
                finish_activity()
                return
            }
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(
                this, getString(R.string.press_again_to_exit),
                Toast.LENGTH_SHORT
            ).show()

            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        } else {
            super.onBackPressed()
        }
    }

}