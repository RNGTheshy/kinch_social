package com.chaoshan.data_center.dynamic.comment

object CommentClient {
    private val mCommentDao = CommentDao()
    fun pushData(comment: Comment) {
        mCommentDao.createNewObject(comment)
    }

    fun getData(id: String, listener: GetCommentDataListener) {
        mCommentDao.getAllCommentByDynamicId(id, listener)
    }
}