package org.seckill.service.impl;


import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillClosedException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

public class SeckillServiceImpl implements SeckillService {

    // 日志对象
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private SeckillDao seckillDao;

    private SuccessKilledDao successKilledDao;

    //md5盐值字符串，用于混淆md5
    // 加入一个混淆字符串(秒杀接口)的salt，为了避免用户猜出我们的md5值，值任意给，越复杂越好
    private final String salt = "sadfUIsdv23rwkUVdafe";

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }


    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        if (seckill == null) {
            return new Exposer(false, seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        //系统当前时间
        Date nowTime = new Date();

        if (nowTime.getTime() < startTime.getTime()
                || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        //转化特定字符串的过程，不可逆
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SecurityException, SeckillClosedException, RepeatKillException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SecurityException("seckill data rewrite");
        }
        //执行秒杀逻辑：减库存 + 记录购买行为
        Date nowTime = new Date();

        try {
            //减库存
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
            if (updateCount <= 0) {
                //dao没有更新到记录，秒杀结束
                throw new SeckillClosedException("seckill is closed");
            } else {
                //减库存成功，记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                //唯一：seckillId,userPhone
                if (insertCount <= 0) {
                    //重复秒杀
                    throw new RepeatKillException("seckill repeated");
                } else {
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillClosedException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所有编译器异常 转化为运行期异常
            throw new SeckillException("seckill inner error" + e.getMessage());
        }
    }
}
