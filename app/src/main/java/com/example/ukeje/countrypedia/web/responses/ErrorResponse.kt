package com.example.ukeje.countrypedia.web.responses

import lombok.Builder
import java.io.Serializable

/**
 * @author .: Oriaghan Uyi
 * @email ..: uyioriaghan@gmail.com, uyi.oriaghan@cwg-plc.com
 * @created : 2019-05-24 13:26
 */
@Builder
data class ErrorResponse(var status: String, var message: String = "") : Serializable {
    constructor() : this("","An Error Occurred")
}