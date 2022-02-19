# proxy-pool 代理IP池

## 项目来源参考：https://github.com/denghuichao/proxy-pool
- 个人进行些改动

## 简介  
- 定期从全球代理网站爬取最新的 ip 代理池
- 每 1 小时重新验证 1 次

## 代理IP来源
- [ ] [goubanjia](http://www.goubanjia.com/) 【2022.02.19 测试网站已失效】
- [x] [kuaidaili](https://www.kuaidaili.com/free/) 
- [x] [66IP](http://www.66ip.cn)
- [ ] [xicidaili](http://www.xicidaili.com) 【2022.02.19 测试网站已失效】
- 这几个网站的代理IP.每个网站IP只爬取了前面10页,后面的基本上不可用.

## 可用性保证
 - 爬回来的IP有大部分都是不能用的,
 - 要保证爬虫拿到的IP质量较高,就需要进行可用性的验证,在代理不可用时将其删除.
 - 每 1 小时定时更新 1 次（保证代理实时有效）

## 接口方式
 - 为了方便客户端(爬虫)获取代理,将接口做成了服务的形式,
 - 客户端通过 restapi 获取代理,并在发现代理不可用时,主动调用 api 将其删除（每小时自动扫描删除）

## proxy-pool设计  

![原理图](https://raw.githubusercontent.com/denghuichao/proxy-pool/master/images/design.png)

整个项目主要分成以下几个模块:
- fetcher 收集网上代理IP的爬虫,目前有`GoubanjiaFetcher`,`KuaiDailiFetcher`,`Www66IPFetcher`,`XichiDailiFetcher`四个爬虫,分别收集四个网站的代理IP.如果需要收集其他网站的IP,可以在该模块下增加相应实现.
- schedule 定时任务,定时从网络上爬取最新IP,定时验证已有代理的可用性.
- db 存储功能, 目前采用redis保存可用代理
- boot 项目的启动类
- api restapi实现

## 项目部署
- 项目基于SpringBoot开发,直接打包成jar并启动jar包即可.
- 需要在本地启动 Redis，默认端口 6379 

### 测试地址
- 获取所有: http://localhost:8080/api/
- 获取单个: http://localhost:8080/api/get
- 获取指定数量代理: http://localhost:8080/api/get_list?num=5
- 获取目前有效代理总数: http://localhost:8080/api/count
- 验证当前代理是否有效: http://localhost:8080/api/verify?proxy=202.55.5.209:8090
- 删除指定代理：http://localhost:8080/api/delete?proxy=