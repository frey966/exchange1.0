package com.financia.common.core.utils.i18n;


import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * @author Jammy
 * @date 2019年01月30日
 */
@Component
public class LocaleMessageSourceUtil {
    @Resource
    private MessageSource messageSource;

    /**
     * @param code ：对应messages配置的key.
     * @return
     */
    public String getMessage(String code){
        return getMessage(code,null);
    }

    /**
     *
     * @param code ：对应messages配置的key.
     * @param args : 数组参数.
     * @return
     */
    public String getMessage(String code,Object[] args){
        return getMessage(code, args,"");
    }


    /**
     *
     * @param code ：对应messages配置的key.
     * @param args : 数组参数.
     * @param defaultMessage : 没有设置key的时候的默认值.
     * @return
     */
    public String getMessage(String code,Object[] args,String defaultMessage){
        Locale locale = LocaleContextHolder.getLocale();
        String lang = lang(locale);
        String lang1 = lang.split("_")[0];
        String lang2 = lang.substring(3);
        Locale locale1 = new Locale(lang1,lang2);
        String message = messageSource.getMessage(code, args, defaultMessage, locale1);
        return message;
    }

    private String lang (Locale locale){
        String lang = null;
      switch (locale.toString()){
          case "vi": //'越南'
              lang = "vi_VN";
              break;
          case "en":
              lang = "en_US";
              break;
          case "zh":
              return "zh_CN";
          case "es": // '西班牙语'
              return "es_ES";
          case "fr":
              return "fr_FR";
          case "ja":
              return "ja_JA";
          case "ko": //'韩国'
              return "ko_KO";
          default:
              return "en_US";
      }
        return lang;
    }

}
