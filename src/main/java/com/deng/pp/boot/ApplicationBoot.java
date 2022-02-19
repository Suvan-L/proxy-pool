package com.deng.pp.boot;

import com.deng.pp.schedule.FetchScheduler;
import com.deng.pp.schedule.Scheduler;
import com.deng.pp.schedule.VerifyScheduler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by hcdeng on 2017/6/30.
 */
@SpringBootApplication(scanBasePackages={"com.deng.pp.api"})
public class ApplicationBoot {
    /*
     * 设置 2 个定时任务爬虫，1 小时执行 1 次
     */
    private static final List<Scheduler> schedules = Arrays.asList(
            new FetchScheduler(1, TimeUnit.HOURS),
            new VerifyScheduler(1, TimeUnit.HOURS)
    );

    public static void main(String[] args) {
        SpringApplication.run(ApplicationBoot.class, args);

        for(Scheduler schedule : schedules)
            schedule.schedule();
    }
}
