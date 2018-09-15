package com.itheima.acl.controller;

import com.itheima.acl.beans.PageQuery;
import com.itheima.acl.common.JsonData;
import com.itheima.acl.param.SearchLogParam;
import com.itheima.acl.service.SysLogService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sys/log/")
public class SysLogController {

    @Value("#{sysLogService}")
    public SysLogService sysLogService;


    @RequestMapping("log.page")
    public ModelAndView page() {
        return new ModelAndView("log");
    }

    @RequestMapping("page.json")
    @ResponseBody
    public JsonData searchPage (SearchLogParam param , PageQuery page) {
        return JsonData.success(sysLogService.searchPageList(page));
    }

}
