package com.endumedia.fetchcodes

import com.endumedia.fetchcodes.vo.NextPathResult


/**
 * Created by Nino on 12.09.19
 */


fun NextPathResult.endPoint(): String {
    val arr = nextPath.split('/')
    return arr[arr.size - 2] + "/"
}