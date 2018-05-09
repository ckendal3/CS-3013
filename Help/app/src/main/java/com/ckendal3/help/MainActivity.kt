package com.ckendal3.help

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    var helpButtonIsClicked: Boolean? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        helpButtonIsClicked = false
    }

    fun toggleVisibility(view: View) {
        if(helpButtonIsClicked == false)
        {
            Toast.makeText(applicationContext, "Help Button Hasn't Been Clicked", Toast.LENGTH_SHORT).show()
            return
        }
        if(textVisibility.isChecked())
            description.visibility = View.INVISIBLE
        else
            description.visibility = View.VISIBLE
    }

    fun toggleBackColor(view: View) {
        if(helpButtonIsClicked == false)
        {
            Toast.makeText(applicationContext, "Help Button Hasn't Been Clicked", Toast.LENGTH_SHORT).show()
            return
        }
        if(backgroundColor.isChecked())
            description.setBackgroundColor(getResources().getColor(R.color.colorPrimary))
        else
            description.setBackgroundColor(getResources().getColor(R.color.colorAccent))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (helpButtonIsClicked == true)
        {
            Toast.makeText(applicationContext, "Help Button Already Clicked", Toast.LENGTH_SHORT).show()
            return true
        }
        description.visibility = View.VISIBLE
        helpButtonIsClicked = true
        return when (item.itemId) {
            R.id.action_help -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}