package com.nahid.meetmax.utils


interface ApplicationCallBack {


    interface AdapterClickListener {
        fun onLikeClick(postId:Long,userId:Long)
        fun onCommentClick(postId:Long,userId:Long)
    }


    interface MainListener {
        fun requestStoragePermission()
    }
}
