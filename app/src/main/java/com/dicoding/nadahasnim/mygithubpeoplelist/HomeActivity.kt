package com.dicoding.nadahasnim.mygithubpeoplelist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.nadahasnim.mygithubpeoplelist.adapter.ListPeopleAdapter
import com.dicoding.nadahasnim.mygithubpeoplelist.databinding.ActivityHomeBinding
import com.dicoding.nadahasnim.mygithubpeoplelist.model.People

class HomeActivity : AppCompatActivity() {
    private val list = ArrayList<People>()

    companion object {
        const val TAG = "Home Activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github Users"

        binding.rvPeople.setHasFixedSize(true)

        list.addAll(listPeople)
        showRecyclerList(binding)
    }

    private val listPeople: ArrayList<People>
        get() {
            val dataUsername = resources.getStringArray(R.array.username)
            val dataName = resources.getStringArray(R.array.name)
            val dataCompany = resources.getStringArray(R.array.company)
            val dataAvatar = resources.obtainTypedArray(R.array.avatar)
            val dataFollower = resources.getStringArray(R.array.followers)
            val dataFollowing = resources.getStringArray(R.array.following)
            val dataLocation = resources.getStringArray(R.array.location)
            val dataRepository = resources.getStringArray(R.array.repository)

            val listHero = ArrayList<People>()

            for (i in dataName.indices) {
                val hero = People(
                    username = dataUsername[i],
                    name = dataName[i],
                    company = dataCompany[i],
                    avatar = dataAvatar.getResourceId(i, -1),
                    follower = dataFollower[i],
                    following = dataFollowing[i],
                    location = dataLocation[i],
                    repository = dataRepository[i],
                )

                listHero.add(hero)
            }

            dataAvatar.recycle()

            return listHero
        }


    private fun showRecyclerList(binding: ActivityHomeBinding) {
        binding.rvPeople.layoutManager = LinearLayoutManager(this)
        val listPeopleAdapter = ListPeopleAdapter(list)
        binding.rvPeople.adapter = listPeopleAdapter

        listPeopleAdapter.setOnItemClickCallback(object : ListPeopleAdapter.OnItemClickCallback {
            override fun onItemClicked(data: People) = openDetail(data)
        })
    }

    fun openDetail(data: People) {
        Log.d(TAG, data.toString())

        val moveToDetailIntent = Intent(this@HomeActivity, DetailActivity::class.java)
        moveToDetailIntent.putExtra(DetailActivity.EXTRA_PEOPLE, data)
        startActivity(moveToDetailIntent)
    }
}