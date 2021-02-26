package com.isaac.m17.net

import android.text.TextUtils
import android.util.Log
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.HttpHeaders
import com.lzy.okgo.model.Progress
import okhttp3.Response
import java.io.IOException
import java.util.*

class OK(val url: String?, val data: HashMap<String?, Any?>?, private val method: Int) {

    private val TAG = "HTTP_OK"
    private val TOKEN = "4bc9c93cf5d08604d21904aa15c47a60a951e0dd"

    private var callback: OKCallBack? = null

    companion object{
        fun get(url: String?, map: HashMap<String?, Any?>?): OK {
            return OK(url, map, 0)
        }

        fun get(url: String): OK {
            return OK(url, null, 0)
        }
    }

    fun start(callback: OKCallBack) {
        this.callback = callback
        start()
    }

    private fun start(): String? {
        return when (method) {
            1 -> {
                post()
            }
            0 -> {
                get()
            }
            else -> {
                ""
            }
        }
    }
    private fun post(): String? {
        return ""
    }

    private fun get(): String? {
        //get请求
        val getRequest = OkGo.get<String>(url)

        getRequest.headers(getHeader())

        if (data != null) {
            for ((key, value) in data.entries) {
                if (value is Int) {
                    getRequest.params(key, value as Int)
                } else getRequest.params(key, value.toString())
            }
        }

        Log.d(TAG, "==>GET" + getRequest.tag + "==" + getRequest.url)
        if (callback == null) {
            try {
                val response: Response = getRequest.execute()
                val httpres = response.body()!!.string()
                Log.d(TAG, "GET" + getRequest.tag + "==" + httpres)
                return httpres
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            getRequest.execute(object : StringCallback() {
                override fun onSuccess(response: com.lzy.okgo.model.Response<String>?) {
                    Log.d(TAG, "GET onSuccess:" + getRequest.tag + "==" + response?.body())

                    success(response?.body())
                }

                override fun onError(response: com.lzy.okgo.model.Response<String>?) {
                    super.onError(response)
                    error(response, response?.exception)
                }
            })
        }
        return ""
    }

    private fun success(responseData: String?) {
        val json = JSONObject()
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSON.parseObject(responseData)
            Log.d(TAG, "返回==》$url")
            Log.d(TAG, "返回==》" + jsonObject.toJSONString())
        } catch (e: java.lang.Exception) {
            Log.d(TAG, "返回==》$url")
            Log.d(TAG, "返回==》$responseData")
        }
        if (jsonObject != null && !jsonObject.isEmpty()) {
            json["data"] = jsonObject
        } else {
            json["data"] = responseData
        }
        try {
            callback!!.requestSuccess(false, json)
        } catch (e: java.lang.Exception) {
            val err = JSONObject()
            callback!!.requestError(err, e.message)
        }
    }

    private fun error(
        response: com.lzy.okgo.model.Response<String>?,
        e: Throwable?
    ) {
        val err = e?.message
        if (TextUtils.isEmpty(err)) {
            try {
                val responseData = response?.body()
                if (TextUtils.isEmpty(responseData)) {
                    callback!!.requestError(null, "net_no_data")
                } else {
                    val jsonObject = JSON.parseObject(responseData)
                    val msg = jsonObject.getString("msg")
                    callback!!.requestError(null, msg)
                }
            } catch (e1: Exception) {
                e1.printStackTrace()
                callback!!.requestError(null, "data_parse_error")
            }
        } else {
            callback!!.requestError(null, err)
        }
    }

    private fun getHeader() : HttpHeaders {
        val headers = HttpHeaders()

        headers.put("Authorization", "bearer $TOKEN")

        return headers
    }

    abstract class OKCallBack : ResponseCallBack {
        override fun requestSuccess(
            isFromCatch: Boolean,
            jsonObject: JSONObject?
        ) {
        }

        override fun requestError(
            errorjson: JSONObject?,
            error: String?
        ) {
        }
    }

    private interface ResponseCallBack {
        /**
         * 成功回調
         */
        fun requestSuccess(isFromCatch: Boolean, jsonObject: JSONObject?)

        /**
         * 失敗回調
         */
        fun requestError(errorjson: JSONObject?, error: String?)
    }
}