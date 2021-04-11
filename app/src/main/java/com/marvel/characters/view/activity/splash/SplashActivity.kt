package com.marvel.characters.view.activity.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.marvel.characters.R
import com.marvel.characters.databinding.ActivitySplashBinding
import com.marvel.characters.remoteConnection.setup.isInternetAvailable
import com.marvel.characters.util.Preferences
import com.marvel.characters.view.activity.baseActivity.BaseActivity
import com.marvel.characters.view.activity.main.MainActivity

class SplashActivity : BaseActivity(
    R.string.app_name, false, false, false,
    false, false, false, false, false,
) {

    lateinit var binding: ActivitySplashBinding
    override fun doOnCreate(arg0: Bundle?) {
        binding = putContentView(R.layout.activity_splash) as ActivitySplashBinding
        binding.viewModel =
            ViewModelProvider(
                this,
                SplashViewModelFactory(application)
            )
                .get(SplashViewModel::class.java)
        binding.viewModel!!.baseViewModelObserver = baseViewModelObserver
        binding.lifecycleOwner = this
        initializeViews()
        setListener()
    }

    override fun initializeViews() {
    }

    override fun setListener() {
        binding.viewModel!!.timerFinished.removeObservers(this@SplashActivity)
        binding.viewModel!!.timerFinished.observe(this, Observer {
            if (it && binding.viewModel!!.connectionFinished.value!! && lifecycle.currentState == Lifecycle.State.RESUMED) {
                toNextActivity()
            }
        })

        binding.viewModel!!.connectionFinished.removeObservers(this@SplashActivity)
        binding.viewModel!!.connectionFinished.observe(this, Observer {

            if (it && binding.viewModel!!.timerFinished.value!! && lifecycle.currentState == Lifecycle.State.RESUMED) {
                toNextActivity()
            }
        })

    }

    private fun toNextActivity() {
        var intent = Intent()
        if (Preferences.getAPIToken().isEmpty()) {
            intent = Intent(this@SplashActivity, MainActivity::class.java)
        } else {
            intent = Intent(this@SplashActivity, MainActivity::class.java)
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_from_right_to_left, R.anim.slide_in_left)
        finish()
    }

}