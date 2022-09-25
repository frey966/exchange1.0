package com.financia.system.util;

import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.Request;
import com.dtflys.forest.http.ForestResponse;

import java.util.Map;

public interface HttpForest {


    /**
     * 发送短信api
     * @param param
     * @return
     */
    @Request(url = "http://api.wftqm.com/api/sms/mtsend",
            headers = {
                    "Content-Type: application/x-www-form-urlencoded; charset=UTF-8"
            },
            type = "POST",
            dataType = "json"
    )
    ForestResponse<Map> sendSms(@Body SmsParam param);
}
