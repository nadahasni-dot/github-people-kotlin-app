package com.dicoding.nadahasnim.mygithubpeoplelist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.dicoding.nadahasnim.mygithubpeoplelist.databinding.ActivityDetailBinding
import com.dicoding.nadahasnim.mygithubpeoplelist.model.People

class DetailActivity : AppCompatActivity() {

    private lateinit var people: People

    companion object {
        const val EXTRA_PEOPLE = "extra_people"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        people = intent.getParcelableExtra<People>(EXTRA_PEOPLE) as People

        binding.ivAvatar.setImageResource(people.avatar)
        binding.tvName.text = people.name
        binding.tvUsername.text = people.username
        binding.tvRepositories.text = people.repository
        binding.tvFollower.text = people.follower
        binding.tvFollowing.text = people.following
        binding.tvLocation.text = people.location
        binding.tvCompany.text = people.company
    }

    private fun shareProfile() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, people.toString())

        val chooseIntent = Intent.createChooser(shareIntent, "Share Profile")
        startActivity(chooseIntent)

    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_detail, menu)
//        return true
//    }

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
}