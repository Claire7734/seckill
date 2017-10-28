package org.seckill.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillClosedException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SeckillServiceImplTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={},list");
        /**
         * Closing non transactional SqlSession
         */
    }

    @Test
    public void getById() throws Exception {
        long seckillId = 1000;
        Seckill seckill = seckillService.getById(seckillId);
        logger.info("seckill={}", seckill);
    }


    //继承测试代码完整逻辑，注意可重复执行
    @Test
    public void testSeckillLogic() throws Exception {
        long seckillId = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if(exposer.isExposed()){
            logger.info("exposer={}", exposer);
            long phone = 1235674334L;
            String md5 = exposer.getMd5();

            try {
                SeckillExecution execution = seckillService.executeSeckill(seckillId, phone, md5);
                logger.info("result={}",execution);
            } catch (SeckillClosedException e1) {
                e1.printStackTrace();
            } catch (RepeatKillException e2) {
                e2.printStackTrace();
            }
        }else {
            //秒杀未开启
            logger.warn("exposer={}",exposer);
        }

        /**
         *
         * 失败
         exposer=Exposer{exposed=false, md5='null', seckillId=1000, now=1509174071432, start=1451577600000, end=1451664000000}
         成功
         exposer=Exposer{exposed=true, md5='aa7e9634c1eb940bebfbc1af670b3112', seckillId=1000, now=0, start=0, end=0}
         */
    }



}