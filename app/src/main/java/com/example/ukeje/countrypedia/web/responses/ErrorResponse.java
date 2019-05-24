package com.example.ukeje.countrypedia.web.responses;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * @author .: Oriaghan Uyi
 * @email ..: uyioriaghan@gmail.com, uyi.oriaghan@cwg-plc.com
 * @created : 2019-05-24 13:26
 */
@Data
@Builder
public class ErrorResponse implements Serializable {
    private String status;
    private String message;
}
