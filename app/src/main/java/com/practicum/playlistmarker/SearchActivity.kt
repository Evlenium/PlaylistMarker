package com.practicum.playlistmarker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity


class SearchActivity : AppCompatActivity() {
    companion object {
        const val INPUT_TEXT_SEARCH = "INPUT_TEXT_SEARCH"
    }
    lateinit var inputTextFromSearch: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val toolbarSearch = findViewById<Toolbar>(R.id.toolbarSearchActivity)
        toolbarSearch.setNavigationIcon(R.drawable.arrow_back_mode)
        toolbarSearch.setNavigationOnClickListener { finish() }
        toolbarSearch.setTitleTextAppearance(this,R.style.SecondsActivityMediumTextAppearance)

        val inputEditTextSearch = findViewById<EditText>(R.id.inputEditTextSearch)
        val clearTextSearchIcon = findViewById<ImageView>(R.id.clearTextSearchIcon)

        clearTextSearchIcon.setOnClickListener {
            inputEditTextSearch.setText("")
            this.currentFocus?.let { view ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s!=null){
                inputTextFromSearch = s.toString()
                }
                clearTextSearchIcon.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditTextSearch.addTextChangedListener(searchTextWatcher)
    }
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_TEXT_SEARCH,inputTextFromSearch)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputTextFromSearch = savedInstanceState.getString(INPUT_TEXT_SEARCH,"")
    }
}