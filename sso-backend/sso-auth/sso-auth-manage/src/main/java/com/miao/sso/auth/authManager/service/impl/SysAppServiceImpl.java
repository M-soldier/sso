package com.miao.sso.auth.authManager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.miao.sso.auth.authManager.service.SysAppService;
import com.miao.sso.auth.mapper.SysAppMapper;
import com.miao.sso.auth.model.entity.SysApp;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
* @author CX330
* @description 针对表【sys_app(应用表)】的数据库操作Service实现
* @createDate 2025-05-06 21:25:08
*/
@Service
public class SysAppServiceImpl extends ServiceImpl<SysAppMapper, SysApp> implements SysAppService {

    private final AtomicInteger clintIdCounter;
    private final Object lock;

    public SysAppServiceImpl() {
        super();
        clintIdCounter = new AtomicInteger(1000);
        lock = new Object();
    }

    @Override
    public String generateAppId() {
        // 先尝试从计数器获取，避免频繁访问数据库
        int nextId = clintIdCounter.incrementAndGet();

        // 检查数据库中是否存在更大的ID
        LambdaQueryWrapper<SysApp> queryWrapper = new LambdaQueryWrapper<SysApp>();
        queryWrapper.orderByDesc(SysApp::getId);
        SysApp app = this.baseMapper.selectOne(queryWrapper);

        if (app != null) {
            int dbMaxClientId = Integer.parseInt(app.getAppId());
            // 如果数据库中的ID更大，则更新计数器
            if (dbMaxClientId >= nextId) {
                // 双重检查锁定
                synchronized (lock) {
                    if(dbMaxClientId >= clintIdCounter.get()) {
                        clintIdCounter.set(dbMaxClientId + 1);
                        nextId = clintIdCounter.get();
                    }
                }
            }
        }

        return String.valueOf(nextId);
    }
}




