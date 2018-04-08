package com.teaml.iq.volunteer.ui.main.myaccount

import android.util.Log
import com.teaml.iq.volunteer.R
import com.teaml.iq.volunteer.data.DataManager
import com.teaml.iq.volunteer.data.model.FbUserDetail
import com.teaml.iq.volunteer.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Created by Mahmood Ali on 13/02/2018.
 */
class MyAccountPresenter<V : MyAccountMvpView> @Inject constructor(dataManager: DataManager)
    : BasePresenter<V>(dataManager), MyAccountMvpPresenter<V> {


    companion object {
        val TAG: String = MyAccountPresenter::class.java.simpleName
    }

    private var isLoggOut: Boolean = false

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
        isLoggOut = dataManager.getCurrentUserLoggedInMode() == DataManager.LoggedInMode.LOGGED_OUT.type
    }

    override fun onSignInClick() {
        mvpView?.openSignInActivity()
    }

    override fun onSignOutClick() {
       setUserAsLoggedOut()
    }

    override fun onMyGroupClick() {
        if (!dataManager.hasGroup())
            mvpView?.openGroupActivityWithCreateGroup()
        else{
            val uid = dataManager.getFirebaseUserAuthID()

            if (uid == null) {
                mvpView?.onError(R.string.some_error)
                return
            }

            mvpView?.openGroupActivityWithGroupDetail(uid)
        }
    }

    override fun decideCurrentLayout(): Int {
        return if (isLoggOut)
            R.layout.myaccount_layout_not_sign_in
        else
            R.layout.my_account_fragment
    }

    override fun onResume() {
        if (!isLoggOut) {
            fetchProfileInfo()
        }
    }

    override fun onViewPrepared() {
        if (dataManager.getCurrentUserLoggedInMode() == DataManager.LoggedInMode.LOGGED_OUT.type)
            mvpView?.setupViewWithSignOutStatus()
        else
            mvpView?.setupViewWithSignInStatus()
    }


    override fun fetchProfileInfo() {
        mvpView?.getBaseActivity()?.let { activity ->


            val uid = dataManager.getFirebaseUserAuthID() ?: return
            Log.e(TAG,"on fetch Info  uid: $uid")

            dataManager.getUserDocRef(uid).addSnapshotListener(activity) { documentSnapshot, firebaseFirestoreException ->

                if (firebaseFirestoreException != null) {
                    Log.w(TAG, firebaseFirestoreException.message)
                    return@addSnapshotListener
                }

                if (documentSnapshot.exists()) {
                    val profileInfo = documentSnapshot.toObject(FbUserDetail::class.java)
                    Log.d(TAG, "profileInfo:$profileInfo")

                    mvpView?.showProfileInfo(profileInfo)
                }

            }
        }
    }

    override fun onMyProfileClick() {
        val uid = dataManager.getFirebaseUserAuthID()

        if (uid == null) {
            mvpView?.openSignInActivity()
            return
        }

        mvpView?.openProfileActivity(uid)
    }


}