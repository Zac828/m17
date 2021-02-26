package com.isaac.m17

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.chad.library.adapter.base.BaseQuickAdapter
import com.isaac.m17.adapter.SearchAdapter
import com.isaac.m17.model.UserInfo
import com.isaac.m17.net.OK
import com.isaac.m17.util.KeyBoardUtil
import com.isaac.m17.widget.dialog.ProgressDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener, TextView.OnEditorActionListener {

    private val dataList = mutableListOf<UserInfo>()
    private var adapter: SearchAdapter? = null

    private var currentPage = 1
    private var lastPage = 1
    private var isSending = false

    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        search_icon.setOnClickListener(this)
        query_string.setOnEditorActionListener(this)

        progressDialog = ProgressDialog(this)

        refresh_layout.setOnRefreshListener {
            if (isSending) return@setOnRefreshListener
            isSending = true
            if (currentPage > 1) {
                search(--currentPage)
            } else {
                search(1)
            }
            refresh_layout.isLoadMoreEnabled = true
        }

        refresh_layout.setOnLoadMoreListener {
            if (isSending) return@setOnLoadMoreListener
            isSending = true
            search(++currentPage)
        }

        swipe_target.layoutManager = GridLayoutManager(this, 2)
        adapter = SearchAdapter(dataList)
        adapter!!.openLoadAnimation(BaseQuickAdapter.ALPHAIN)
        adapter!!.bindToRecyclerView(swipe_target)
    }

    private fun search(page: Int) {
        val query = query_string.text
        val map = HashMap<String?, Any?>()
        if (!query.isNullOrEmpty()) {
            map["q"] = query
        } else {
            Toast.makeText(this, "Please enter a keyword", Toast.LENGTH_SHORT).show()
            isSending = false
            refresh_layout.isRefreshing = false
            refresh_layout.isLoadingMore = false
            return
        }
        map["page"] = page

        progressDialog!!.show()

        OK.get("https://api.github.com/search/users", map).start(object : OK.OKCallBack() {

            override fun requestSuccess(isFromCatch: Boolean, jsonObject: JSONObject?) {
                super.requestSuccess(isFromCatch, jsonObject)
                if (isFinishing || isDestroyed) return

                isSending = false
                progressDialog!!.dismiss()
                refresh_layout.isRefreshing = false
                refresh_layout.isLoadingMore = false

                if (!jsonObject.isNullOrEmpty() && jsonObject.contains("data")) {
                    val data: JSONObject = jsonObject["data"] as JSONObject
                    if (data.contains("items")) {
                        val items = data.getJSONArray("items").toJSONString()
                        dataList.clear()
                        dataList.addAll(JSON.parseArray(items, UserInfo::class.java))

                        if (dataList.isEmpty()) {
                            Toast.makeText(this@MainActivity, "No Data", Toast.LENGTH_LONG).show()
                            refresh_layout.isLoadMoreEnabled = false
                        }
                        adapter?.notifyDataSetChanged()
                    }
                }
            }

            override fun requestError(errorjson: JSONObject?, error: String?) {
                super.requestError(errorjson, error)
                if (isFinishing || isDestroyed) return
                isSending = false
                progressDialog!!.dismiss()
                refresh_layout.isRefreshing = false
                refresh_layout.isLoadingMore = false

                Log.e("", "error: $error")
            }
        })
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.search_icon) {
            KeyBoardUtil.closeKeyboard(query_string, this)

            currentPage = 1
            lastPage = 1
            isSending = true
            search(currentPage)
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
        var handler = false
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            handler = true

            search_icon.performClick()
        }
        return handler
    }
}