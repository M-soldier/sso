package com.miao.sso.base.scheduler;

import com.miao.sso.base.model.entity.ExpirationPolicy;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
@AllArgsConstructor
public class ExpirationScheduler {
    private List<ExpirationPolicy> expirationPolicyList;

    @Scheduled(cron = ExpirationPolicy.SCHEDULED_CRON)
    public void verifyExpired() {
        if (CollectionUtils.isEmpty(expirationPolicyList)) {
            return;
        }
        expirationPolicyList.forEach(ExpirationPolicy::verifyExpired);
    }
}
