package com.miao.sso.auth.entity.content;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketGrantingTicketContent implements Serializable {
    private static final long serialVersionUID = 2957202051267129445L;

    private Long userId;
    private Long createTime;
}
