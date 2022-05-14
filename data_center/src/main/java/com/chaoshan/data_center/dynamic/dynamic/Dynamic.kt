package com.chaoshan.data_center.dynamic.dynamic

import java.io.Serializable

data class Dynamic(
    var dynamicId: String? = null,
    var userID: String? = null,
    var theme: String? = null,
    var text: String? = null,
    var picture: String? = null,
    var releaseTime: String? = null,
    var thumbsNumber: String? = null,
    var commentNumber: String? = null,
) : Serializable