package com.deng.pp.schedule;

import com.deng.pp.entity.ProxyEntity;
import com.deng.pp.fetcher.AbstractFetcher;
import com.deng.pp.fetcher.KuaiDailiFetcher;
import com.deng.pp.fetcher.Www66IPFetcher;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by hcdeng on 2017/6/29.
 */
@Slf4j
public class FetchScheduler extends Scheduler {

    public FetchScheduler(long defaultInterval, TimeUnit defaultUnit) {
        super(defaultInterval, defaultUnit);
    }

    @Override
    public void run() {

        log.info("fetch scheduler running...");

        List<AbstractFetcher<List<ProxyEntity>>> fetchers =
                Arrays.asList(
                        new KuaiDailiFetcher(8),
                        new Www66IPFetcher(8)
                        // 【2022.02.19】已失效的注释掉
//                        new XichiDailiFetcher(8),
//                        new GoubanjiaFetcher(8)
                );


        // 先获取，然后验证
        for (AbstractFetcher<List<ProxyEntity>> fetcher : fetchers) {
            // 访问免费代理网站，解析出代理地址 List
            fetcher.fetchAll((list)->{
                 // 测试验证，访问 url，如果有效，存入 Redis
                ProxyVerifier.verifyAll(list);
            });
        }

        log.info("finish fetch scheduler");
    }
}
