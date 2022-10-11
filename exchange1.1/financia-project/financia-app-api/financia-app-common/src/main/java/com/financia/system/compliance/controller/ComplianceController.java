package com.financia.system.compliance.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financia.common.PCompliance;
import com.financia.common.PComplienceModel;
import com.financia.common.PComplienceTitleModel;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.system.compliance.service.ComplianceSercice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yezi
 */
@Api(tags = "个人中心 - 经营合规")
@RestController
@RequestMapping("/compliance")
public class ComplianceController extends BaseController {

    @Resource
    private ComplianceSercice complianceSercice;

    @GetMapping("info")
    public AjaxResult Info(@ApiParam("type") Integer type) {
        List<PCompliance> one = complianceSercice.list(new QueryWrapper<PCompliance>().lambda().eq(PCompliance::getType, type));

        if (ObjectUtils.isEmpty(one) || one.size() < 1) {
            return AjaxResult.error("no such result");
        }


        PComplienceModel pComplienceModel = new PComplienceModel();
        List<PComplienceTitleModel> pComplienceTitleModels = new ArrayList<>();
        one.forEach(item -> {
            PComplienceTitleModel pComplienceTitleModel = new PComplienceTitleModel();
            pComplienceTitleModel.setTitle(item.getTitle());
            pComplienceTitleModel.setContentstr(item.getContentstr());
            pComplienceTitleModels.add(pComplienceTitleModel);
        });
        pComplienceModel.setType(one.get(0).getType());
        pComplienceModel.setContent(pComplienceTitleModels);
        pComplienceModel.setName1(one.get(0).getName1());
        pComplienceModel.setName2(one.get(0).getName2());
        pComplienceModel.setImgurl1(one.get(0).getImgurl1());
        pComplienceModel.setImgurl2(one.get(0).getImgurl2());
        return AjaxResult.success(pComplienceModel);

    }
}
