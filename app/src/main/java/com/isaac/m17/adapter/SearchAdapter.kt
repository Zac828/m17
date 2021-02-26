package com.isaac.m17.adapter

import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.isaac.m17.R
import com.isaac.m17.model.UserInfo
import com.isaac.m17.util.GlideImageLoader
import com.isaac.m17.util.ScreenUtil

class SearchAdapter(data: List<UserInfo>?) : BaseQuickAdapter<UserInfo, BaseViewHolder>(R.layout.item_user, data) {

    override fun convert(helper: BaseViewHolder, item: UserInfo) {
        val width = ScreenUtil.getScreenWidth(mContext) / 2
        helper.itemView.layoutParams = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, width)

        helper.setText(R.id.user_name, item.login)
        val imageView = helper.getView<ImageView>(R.id.user_img)
        GlideImageLoader.loadImage(item.avatar_url, imageView)
    }
}