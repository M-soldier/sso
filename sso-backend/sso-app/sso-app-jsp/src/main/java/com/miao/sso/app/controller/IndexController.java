package com.miao.sso.app.controller;


import com.miao.sso.client.ClientProperties;
import com.miao.sso.client.common.ClientContextHolder;
import com.miao.sso.base.constants.BaseConstant;
import com.miao.sso.base.model.rpc.TokenUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping("/")
public class IndexController extends BaseController {
    private final ClientProperties clientProperties;

    public IndexController(ClientProperties clientProperties) {
        this.clientProperties = clientProperties;
    }

    @GetMapping("/index")
    public String index(Model model, HttpServletRequest request){
        TokenUser user = ClientContextHolder.getUser();
        // 登录用户名
        assert user != null;
        model.addAttribute("userName", user.getUserName());
        // 当前服务端口号
        model.addAttribute("serverPort", 8088);
        // 当前sessionId
        model.addAttribute("sessionId", request.getSession().getId());
        // 单点退出地址
        model.addAttribute("logoutUrl", clientProperties.getServer().getSsoServerUrl()
                + BaseConstant.LOGOUT_PATH + "?" + BaseConstant.REDIRECT_URI + "=" + getLocalUrl(request));
        return "index-view";
    }
}
