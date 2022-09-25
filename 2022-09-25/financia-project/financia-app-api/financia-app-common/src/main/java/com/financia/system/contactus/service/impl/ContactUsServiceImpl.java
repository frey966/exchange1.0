package com.financia.system.contactus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.common.ContactUs;
import com.financia.system.contactus.mapper.ContactUsMapper;
import com.financia.system.contactus.service.ContactUsService;
import org.springframework.stereotype.Service;

/**
 * @author Yezi
 */
@Service
public class ContactUsServiceImpl extends ServiceImpl<ContactUsMapper, ContactUs> implements ContactUsService {
}
