package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 配置spring和junit整合，junit启动时加载springIoC容器
 * spring-test,junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    //注入Dao实现类依赖
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void queryById() throws Exception {
        long id=1000;
        Seckill seckill=seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
        /**
         * 1000元秒杀iphone6
         Seckill
         {seckillId=1000,
         name='1000元秒杀iphone6',
         number=100,
         createTime=Fri Oct 27 16:22:08 CST 2017,
         startTime=Fri Jan 01 00:00:00 CST 2016,
         endTime=Sat Jan 02 00:00:00 CST 2016}
         */
    }

    @Test
    public void queryAll() throws Exception {
        /**
         * Caused by: org.apache.ibatis.binding.BindingException:
         * Parameter 'offset' not found. Available parameters are [arg1, arg0, param1, param2]
         * java没有保存形参的记录
         * queryAll(int offset, int limit) -> queryAll(arg0, arg1)
         */

        /**
         *
         Seckill{seckillId=1000, name='1000元秒杀iphone6', number=100, createTime=Fri Oct 27 16:22:08 CST 2017, startTime=Fri Jan 01 00:00:00 CST 2016, endTime=Sat Jan 02 00:00:00 CST 2016}
         Seckill{seckillId=1001, name='800元秒杀ipad', number=200, createTime=Fri Oct 27 16:22:08 CST 2017, startTime=Fri Jan 01 00:00:00 CST 2016, endTime=Sat Jan 02 00:00:00 CST 2016}
         Seckill{seckillId=1002, name='6600元秒杀mac book pro', number=300, createTime=Fri Oct 27 16:22:08 CST 2017, startTime=Fri Jan 01 00:00:00 CST 2016, endTime=Sat Jan 02 00:00:00 CST 2016}
         Seckill{seckillId=1003, name='7000元秒杀iMac', number=400, createTime=Fri Oct 27 16:22:08 CST 2017, startTime=Fri Jan 01 00:00:00 CST 2016, endTime=Sat Jan 02 00:00:00 CST 2016}
         */

        List<Seckill>list=seckillDao.queryAll(0,100);
        for (Seckill seckill:list) {
            System.out.println(seckill);
        }
    }

    @Test
    public void reduceNumber() throws Exception {
        /**
         * JDBC Connection [com.mchange.v2.c3p0.impl.NewProxyConnection@60d84f61] will not be managed by Spring
         Preparing: UPDATE seckill SET number = number-1 WHERE seckill_id = ? AND start_time <= ? AND end_time >= ? AND number > 0;
         Parameters: 1000(Long), 2017-10-27 19:25:54.396(Timestamp), 2017-10-27 19:25:54.396(Timestamp)
         19:25:54.974 [main] DEBUG org.seckill.dao.SeckillDao.reduceNumber - <==    Updates: 0
         19:25:54.975 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@62da83ed]
         */
        Date killTime = new Date();
        int updateCount = seckillDao.reduceNumber(1000L,killTime);
        System.out.println(updateCount);
    }


}