package com.financia.system.util;

import lombok.Data;

@Data
public class SmsParam {
//    appkey	是	string
//    secretkey	是	string
//    phone	是	string
//    content	是	string
//    source_address	否	string
//    short_link	否	string
//    sys_messageid	否	string
//    linkVerbose	否	string
//    opt_entity_id	否	string
//    opt_template_id	否	string
//    opt_header_id	否	string
    /**
     * 短信应用appkey
     */
    private String appkey;
    /**
     * 短信应用secretkey
     */
    private String secretkey;
    /**
     * 被叫号码(国码+手机号，比如：8615088888888),可以多个并且以","英文逗号隔开
     */
    private String phone;
    /**
     *	短信内容,必须做urlencode(UTF-8)，内容最长1000个字符
     */
    private String content;
    /**
     *	sourceaddress/sender 否
     */
    private String source_address;
    /**
     *	短链，数据来自于短链列表；如果此处赋值，短信内容里面必须包含#1#才能起作用，请注意 否
     */
    private String short_link;
    /**
     *	用户自定义messageid，长度为10-50位之间，类型【0-9a-zA-Z-】否
     */
    private String sys_messageid;
    /**
     * 该参数设定的为是否收集用户点击行为，1(收集,默认),0(不收集)	否
     */
    private String linkVerbose;
    /**
     * 实体编号，当发送91方向的短信时需要填写	 否
     */
    private String opt_entity_id;
    /**
     * 模板编号，当发送91方向的短信时需要填写 	否
     */
    private String opt_template_id;
    /**
     * 发件人编号，当发送91方向的短信时需要填写 	否
     */
    private String opt_header_id;
}
