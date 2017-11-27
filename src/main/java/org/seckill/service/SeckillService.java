package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillClosedException;

import java.util.List;

/**
 * 业务接口：站在"使用者"角度上设计接口
 * 三个方面：方法定义粒度，参数，返回类型（return 类型/异常）
 */
public interface SeckillService {

    /**
     * 查询全部的秒杀记录
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀记录
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);


    /**
     * 在秒杀开启时输出秒杀接口的地址，否则输出系统时间和秒杀时间
     * @param seckillId 秒杀商品Id
     * @return 根据对应的状态返回对应的状态实体
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作by存储过程
     *
     * @param seckillId 秒杀的商品ID
     * @param userPhone 手机号码
     * @param md5 md5加密值
     * @return 根据不同的结果返回不同的实体信息
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
        throws SecurityException,SeckillClosedException,RepeatKillException;

    SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5);
}
