package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() throws Exception {
        long id = 1001L;
        long userPhone = 13014111111L;
        int result=successKilledDao.insertSuccessKilled(id,userPhone);
        System.out.println(result);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        long id = 1001L;
        long userPhone = 13014111111L;
        SuccessKilled result = successKilledDao.queryByIdWithSeckill(id,userPhone);
        System.out.println(result);
        System.out.println(result.getSeckill());

        /**
         *20:06:44.837 [main] DEBUG org.seckill.dao.SuccessKilledDao.queryByIdWithSeckill - ==>  Preparing: SELECT sk.seckill_id, sk.user_phone, sk.create_time, sk.state, s.seckill_id "seckill.seckill_id", s.name "seckill.name", s.number "seckill.number", s.start_time "seckill.start_time", s.end_time "seckill.end_time", s.create_time "seckill.create_time" FROM success_killed sk INNER JOIN seckill s ON sk.seckill_id=s.seckill_id WHERE sk.seckill_id=? and sk.user_phone=?
         20:06:44.867 [main] DEBUG org.seckill.dao.SuccessKilledDao.queryByIdWithSeckill - ==> Parameters: 1001(Long), 13014111111(Long)
         20:06:44.898 [main] DEBUG org.seckill.dao.SuccessKilledDao.queryByIdWithSeckill - <==      Total: 1
         20:06:44.910 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@4082ba93]
         SuccessKill{seckillId=1001, userPhone=13014111111, state=0, create_time=null}
         Seckill{seckillId=1001, name='800元秒杀ipad', number=200, createTime=Fri Oct 27 16:22:08 CST 2017, startTime=Fri Jan 01 00:00:00 CST 2016, endTime=Sat Jan 02 00:00:00 CST 2016}
         *
         */

    }

}