package com.chaoshan.data_center.dynamic.like

object LikeClient {
    private val likeDao = LikeDao()
    fun saveDate(like: Like) {
        likeDao.createNewObject(like)
    }
}