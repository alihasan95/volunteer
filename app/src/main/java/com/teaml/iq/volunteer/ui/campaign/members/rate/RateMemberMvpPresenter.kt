package com.teaml.iq.volunteer.ui.campaign.members.rate

import com.teaml.iq.volunteer.ui.base.loadata.BaseLoadDataMvpPresenter

/**
 * Created by ali on 3/16/2018.
 */
interface RateMemberMvpPresenter<V:RateMemberMvpView>: BaseLoadDataMvpPresenter<V> {

    fun onViewPrepared(campaignId: String)

    fun onHelpfulClick(campaignId: String, userId: String, position: Int)

    fun onUnhelpfulClick(campaignId: String, userId: String, position: Int)

    fun onNotAttendClick(campaignId: String, userId: String, position: Int)

}