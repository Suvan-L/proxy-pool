package com.deng.pp.schedule;

import com.deng.pp.db.repositor.ProxyRepository;
import com.deng.pp.entity.ProxyEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by hcdeng on 2017/6/29.
 */
@Slf4j
public class VerifyScheduler extends Scheduler {

    public VerifyScheduler(long defaultInterval, TimeUnit defaultUnit) {
        super(defaultInterval, defaultUnit);
    }

    @Override
    public void run() {
        List<ProxyEntity> proxys = ProxyRepository.getInstance().getAll();
        log.info("verify scheduler running, proxys:"+proxys.size());
        ProxyVerifier.refreshAll(proxys);
    }
}
