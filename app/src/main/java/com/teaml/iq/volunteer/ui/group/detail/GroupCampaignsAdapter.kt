package com.teaml.iq.volunteer.ui.group.detail

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import com.google.firebase.storage.FirebaseStorage
import com.teaml.iq.volunteer.R
import com.teaml.iq.volunteer.data.model.GlideApp
import com.teaml.iq.volunteer.data.model.GroupCampaigns
import com.teaml.iq.volunteer.ui.base.BaseRecyclerAdapter
import com.teaml.iq.volunteer.ui.base.BaseViewHolder
import com.teaml.iq.volunteer.utils.AppConstants
import com.teaml.iq.volunteer.utils.CommonUtils
import com.teaml.iq.volunteer.utils.visible
import org.jetbrains.anko.find
import org.jetbrains.anko.toast

/**
 * Created by Mahmood Ali on 18/02/2018.
 */
class GroupCampaignsAdapter(list: MutableList<GroupCampaigns>, val isGroupOwner: Boolean = false) : BaseRecyclerAdapter<GroupCampaigns>(list) {

    companion object {
        val TAG: String = GroupCampaignsAdapter::class.java.simpleName
    }


    var onItemClick: ((String) -> Unit)? = null

    inner class GroupCampaignsVH(view: View) : BaseViewHolder(view) {

        private val imgView = view.find<ImageView>(R.id.campaignCoverImgView)
        private val txtTitle = view.find<TextView>(R.id.txtCampaignTitle)
        private val txtUploadDate = view.find<TextView>(R.id.txtUploadDate)
        private val imgMore = view.find<ImageView>(R.id.imgMore)

        private val mContext = imgView.context

        init {


            view.setOnClickListener {
                Log.i("click", "item is clicked on the position $currentPosition")
                // send the id to groupDetailFragment
                onItemClick?.invoke(mList[currentPosition].campaignId)
            }


            val popupMenu = PopupMenu(mContext, imgMore)
            popupMenu.inflate(R.menu.campaign_owner_menu)

            imgMore.setOnClickListener {

                popupMenu.setOnMenuItemClickListener { menuItem ->

                    when (menuItem.itemId) {
                        R.id.action_edit -> {
                            val campaignId = mList[adapterPosition].campaignId
                            mContext.toast("campaign id : $campaignId")
                        }
                    }

                    true
                }

            }
        }

        override fun clear() {
            imgView.setImageDrawable(null)
            //imgMore.gone
            txtTitle.text = ""
            txtUploadDate.text = ""
        }

        override fun onBind(position: Int) {
            super.onBind(position)

            val item = mList[position]

            if (isGroupOwner)
                imgMore.visible

            txtTitle.text = item.title
            txtUploadDate.text = CommonUtils.getHumanReadableElapseTime(item.uploadDate, mContext)

            try {

                val imgRef = FirebaseStorage.getInstance().getReference("${AppConstants.CAMPAIGN_IMG_FOLDER}/${item.imgName}")
                GlideApp.with(txtTitle.context)
                        .load(imgRef)
                        .placeholder(R.drawable.campaign_placeholder_img)
                        .into(imgView)
            } catch (e: Exception) {
                Log.d(TAG, "on loading campaign image", e)
            }
        }
    }


    fun setOnViewItemClick(ref: (String) -> Unit) {
        onItemClick = ref
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.group_campaigns_view, parent, false)
        return GroupCampaignsVH(view)
    }

}