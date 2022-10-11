package com.financia.business.contentmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.common.MemberShare;


/**
 * @author Yezi
 */
public interface MemberShareService extends IService<MemberShare> {

    /**
     * 插入新的记录
     *
     * @param memberShare
     * @return
     */

    boolean insertShareInfo(MemberShare memberShare);

    /**
     * 修改已有记录
     * @param info
     * @return
     */

    boolean updateShareInfo(MemberShare info);
}
