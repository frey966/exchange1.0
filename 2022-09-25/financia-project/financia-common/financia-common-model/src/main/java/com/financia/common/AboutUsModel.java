package com.financia.common;

import lombok.Data;

@Data
public class AboutUsModel {
    /**
     * 隐私政策文本 - 中文
     */
    private String contentZh;

    /**
     * 隐私政策文本 - 英文
     */
    private String contentEn;

    /**
     * 版本号
     */
    private String appVersion;
}
