package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.netty.util.internal.StringUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
    @Override
    public List<ShopType> listShops() {
        // 1.从redis页面获取商铺类型
        String shops = stringRedisTemplate.opsForValue().get("shops:");

        // 2.存在，直接返回
        if (StrUtil.isNotBlank(shops)) {
            List<ShopType> list = JSONUtil.toList(shops, ShopType.class);
            return list;
        }

        // 3.不存在，调用数据库获取
        List<ShopType> list = query().orderByAsc("sort").list();

        // 4.将商铺类型存储到redis中
        if (!list.isEmpty()) {
            stringRedisTemplate.opsForValue().set("shops:", JSONUtil.toJsonStr(list));
        }

        // 5.返回商铺类型
        return list;
    }
}
