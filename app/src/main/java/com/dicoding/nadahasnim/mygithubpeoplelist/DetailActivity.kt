package com.dicoding.nadahasnim.mygithubpeoplelist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.dicoding.nadahasnim.mygithubpeoplelist.adapter.DetailSectionsPagerAdapter
import com.dicoding.nadahasnim.mygithubpeoplelist.databinding.ActivityDetailBinding
import com.dicoding.nadahasnim.mygithubpeoplelist.model.People
import com.dicoding.nadahasnim.mygithubpeoplelist.model.ResponseDetailUser
import com.dicoding.nadahasnim.mygithubpeoplelist.service.ResponseCall
import com.dicoding.nadahasnim.mygithubpeoplelist.service.Status
import com.dicoding.nadahasnim.mygithubpeoplelist.viewmodel.DetailViewModel
import com.dicoding.nadahasnim.mygithubpeoplelist.viewmodel.DetailViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private var binding: ActivityDetailBinding? = null
    private lateinit var people: People
    private val detailViewModel: DetailViewModel by viewModels { DetailViewModelFactory(people.username) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        people = intent.getParcelableExtra(EXTRA_PEOPLE)!!

        supportActionBar.let {
            it?.title = resources.getString(R.string.username, people.username)
            it?.setDisplayHomeAsUpEnabled(true)
            it?.setDisplayShowHomeEnabled(true)
        }

        detailViewModel.let {
            it.responseCall.observe(this) { request ->
                val detail = it.detailUser.value
                val followers = it.followers.value
                val following = it.following.value

                binding?.detailContent?.tvFollower?.text = followers?.size.toString()
                binding?.detailContent?.tvFollowing?.text = following?.size.toString()
                binding?.detailContent?.tvRepositories?.text = it.repositories.value.toString()

                if (detail != null) {
                    showContent(detail)
                }

                showLoadingIndicator(request)
            }
        }

    }

    private fun showContent(
        detailUser: ResponseDetailUser
    ) {
        val detailSectionsPagerAdapter =
            DetailSectionsPagerAdapter(this)

        binding?.detailContent?.let {
            Glide.with(this).load(detailUser.avatarUrl).into(it.ivAvatar)
            it.tvName.text = detailUser.name
            it.tvCompany.text = detailUser.company
            it.tvLocation.text = detailUser.location

            it.viewPager.adapter = detailSectionsPagerAdapter
            TabLayoutMediator(it.tabs, it.viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    private fun showLoadingIndicator(request: ResponseCall) {
        if (request.status == Status.LOADING) {
            binding?.errorLayout?.root?.visibility = View.INVISIBLE
            binding?.detailContent?.root?.visibility = View.INVISIBLE
            binding?.progressBar?.visibility = View.VISIBLE
            return
        }

        if (request.status == Status.ERROR) {
            binding?.errorLayout?.tvError?.text = request.message
            binding?.errorLayout?.btnReload?.setOnClickListener {
                detailViewModel.getUser(people.username)
            }
            binding?.detailContent?.root?.visibility = View.INVISIBLE
            binding?.progressBar?.visibility = View.INVISIBLE
            binding?.errorLayout?.root?.visibility = View.VISIBLE
            return
        }

        if (request.status == Status.COMPLETED) {
            binding?.errorLayout?.root?.visibility = View.INVISIBLE
            binding?.progressBar?.visibility = View.INVISIBLE
            binding?.detailContent?.root?.visibility = View.VISIBLE
            return
        }
    }

    private fun shareProfile() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        Log.d(TAG, "SHARE TO: ${people.profileUrl}")
        shareIntent.putExtra(Intent.EXTRA_TEXT, people.profileUrl)

        val chooseIntent = Intent.createChooser(shareIntent, "Share Profile")
        startActivity(chooseIntent)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                shareProfile()
                true
            }
            else -> {
                onBackPressed()
                true
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        if (binding != null) binding = null
    }

    companion object {
        const val TAG = "DetailActivity"
        const val EXTRA_PEOPLE = "extra_pople"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers_tab,
            R.string.following_tab
        )
    }
}