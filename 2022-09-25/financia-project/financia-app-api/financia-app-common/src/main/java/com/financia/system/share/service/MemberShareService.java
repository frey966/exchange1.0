package com.financia.system.share.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.common.MemberShare;


/**
 * @author Yezi
 */
public interface MemberShareService extends IService<MemberShare> {
    /**
     * 通过pic查询对应的分享内容
     *
     * @param picCh,picEn
     * @return MemberShare
     */
    MemberShare queryShareInfoByPic(String picCh,String picEn);


}
