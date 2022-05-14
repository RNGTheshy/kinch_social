package com.chaoshan.data_center.dynamic.like

object LikeClient {
    private val likeDao = LikeDao()
    fun saveDate(like: Like) {
        likeDao.createNewObject(like)
    }

    fun getLikeCount(dynamicId: String, likeCountCallBack: GetLikeCountCallBack) {
        likeDao.getAllCountByDynamicId(dynamicId, likeCountCallBack)
    }

}