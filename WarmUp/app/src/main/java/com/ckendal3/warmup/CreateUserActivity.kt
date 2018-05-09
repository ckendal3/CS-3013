package com.ckendal3.warmup

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast

class CreateUserActivity : AppCompatActivity() {

    private var name = ""
    var gender = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)

        var male = true
        
        val radioGroup = findViewById<View>(R.id.gender) as RadioGroup
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.male -> {
                    Toast.makeText(baseContext, "User is male.", Toast.LENGTH_LONG).show()
                    male = true
                    gender = "Male"
                }
                R.id.female -> {
                    Toast.makeText(baseContext, "User is female.", Toast.LENGTH_LONG).show()
                    male = false
                    gender = "Female"
                }
            }
        }
    }

    fun onClick(view: View) {
        val input = findViewById<View>(R.id.username) as EditText
        name = input.text.toString()
        Toast.makeText(this, "$gender User:  $name created.", Toast.LENGTH_LONG).show()
    }
}