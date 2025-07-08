package com.miao.sso.auth.authCenter.controller;

import com.miao.sso.auth.manager.AbstractTicketGrantingTicketManager;
import com.miao.sso.base.constants.BaseConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping(BaseConstant.LOGOUT_PATH)
public class SSOLogoutController {
    private final AbstractTicketGrantingTicketManager tgtManager;
    public SSOLogoutController(AbstractTicketGrantingTicketManager tgtManager) {
        this.tgtManager = tgtManager;
    }

    @GetMapping
    public void logout(
            @RequestParam(required = false) String redirectUri,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("logout");
        tgtManager.invalidateTGT(request, response);
        if (redirectUri != null) {
            response.sendRedirect(redirectUri);
        }
    }
}
