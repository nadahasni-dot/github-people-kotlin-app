package com.dicoding.nadahasnim.mygithubpeoplelist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.dicoding.nadahasnim.mygithubpeoplelist.databinding.ActivityDetailBinding
import com.dicoding.nadahasnim.mygithubpeoplelist.model.People
import com.dicoding.nadahasnim.mygithubpeoplelist.model.ResponseDetailUser
import com.dicoding.nadahasnim.mygithubpeoplelist.service.ResponseCall
import com.dicoding.nadahasnim.mygithubpeoplelist.service.Status
import com.dicoding.nadahasnim.mygithubpeoplelist.viewmodel.DetailViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var people: People
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        people = intent.getParcelableExtra(EXTRA_PEOPLE)!!

        detailViewModel.getUser(people.username)

        detailViewModel.let {
            it.detailUser.observe(this) { detailUser ->
                showContent(detailUser)
            }
            it.responseCall.observe(this) { request ->
                showLoadingIndicator(request)
            }
        }

//        binding.ivAvatar.setImageResource(people.avatar)
//        binding.tvName.text = people.name
//        binding.tvUsername.text = people.username
//        binding.tvRepositories.text = people.repository
//        binding.tvFollower.text = people.follower
//        binding.tvFollowing.text = people.following
//        binding.tvLocation.text = people.location
//        binding.tvCompany.text = people.company
    }

    private fun showContent(detailUser: ResponseDetailUser) {
        binding.detailContent.let {
            Glide.with(this).load(detailUser.avatarUrl).into(it.ivAvatar)
            it.tvName.text = detailUser.name
            it.tvUsername.text = resources.getString(R.string.username, detailUser.login)
            it.tvCompany.text = detailUser.company
            it.tvLocation.text = detailUser.location
        }
    }

    private fun showLoadingIndicator(request: ResponseCall) {
        if (request.status == Status.LOADING) {
            binding.detailContent.root.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
            return
        }

        if (request.status == Status.ERROR) {
            return
        }

        if (request.status == Status.COMPLETED) {
            binding.progressBar.visibility = View.INVISIBLE
            binding.detailContent.root.visibility = View.VISIBLE
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

    companion object {
        const val TAG = "DetailActivity"
        const val EXTRA_PEOPLE = "extra_pople"
    }
}