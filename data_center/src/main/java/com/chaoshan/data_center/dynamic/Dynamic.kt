package com.chaoshan.data_center.dynamic

data class Dynamic(
    var dynamicId: Int,
    var userID: Int? = null,
    var theme: String? = null,
    var text: String? = null,
    var picture: String? = null,
    var releaseTime: String? = null,
    var thumbsNumber: Int? = null,
    var commentNumber: Int? = null,
)