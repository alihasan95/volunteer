package com.teaml.iq.volunteer.ui.main.group

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.signature.ObjectKey
import com.google.firebase.storage.FirebaseStorage
import com.teaml.iq.volunteer.R
import com.teaml.iq.volunteer.data.model.GlideApp
import com.teaml.iq.volunteer.data.model.GroupInfo
import com.teaml.iq.volunteer.ui.base.BaseRecyclerAdapter
import com.teaml.iq.volunteer.ui.base.BaseViewHolder
import com.teaml.iq.volunteer.ui.main.home.CampaignAdapter
import com.teaml.iq.volunteer.utils.AppConstants
import com.teaml.iq.volunteer.utils.toTimestamp
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.find

/**
 * Created by Mahmood Ali on 11/02/2018.
 */
class GroupAdapter(listOfGroupInfo: MutableList<GroupInfo>) : BaseRecyclerAdapter<GroupInfo>(listOfGroupInfo) {

    companion object {
        val TAG: String = CampaignAdapter::class.java.simpleName
    }


    inner class GroupVH(itemView: View) : BaseViewHolder(itemView) {

        private val firebaseStorage = FirebaseStorage.getInstance()

        private val groupNameView = itemView.find<TextView>(R.id.txtGroupName)
        private val memberNumberView = itemView.find<TextView>(R.id.memberNumber)
        private val txtCampaignCount = itemView.find<TextView>(R.id.campaignCount)
        private val groupImgView = itemView.find<CircleImageView>(R.id.groupImg)

        override fun clear() {
            groupImgView.setImageDrawable(null)
            groupNameView.text = ""
            memberNumberView.text = ""
            txtCampaignCount.text = ""
        }


        override fun onBind(position: Int) {
            super.onBind(position)

            val group = mList[position]

            with(group) {
                groupNameView.text = name
                txtCampaignCount.text = itemView.context.getString(R.string.campaigns_number, campaignsNum)

                try {
                    val groupImgRef = firebaseStorage.getReference("${AppConstants.GROUP_LOGO_IMG_FOLDER}/$groupImg")

                    GlideApp.with(itemView.context)
                            .load(groupImgRef)
                            .signature(ObjectKey(lastModificationDate.toTimestamp()))
                            .placeholder(R.drawable.org_placeholder_img)
                            .into(groupImgView)
                } catch (e: Exception) {
                    Log.e(TAG, e.message)
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return GroupVH(LayoutInflater.from(parent.context).inflate(R.layout.group_view, parent, false))
    }

}