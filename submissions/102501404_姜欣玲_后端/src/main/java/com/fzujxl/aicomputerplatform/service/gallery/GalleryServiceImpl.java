package com.fzujxl.aicomputerplatform.service.gallery;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fzujxl.aicomputerplatform.dto.PageResultResponse;
import com.fzujxl.aicomputerplatform.dto.gallery.ArtworkDetailResponse;
import com.fzujxl.aicomputerplatform.dto.gallery.ArtworkQueryRequest;
import com.fzujxl.aicomputerplatform.dto.gallery.GalleryItemResponse;
import com.fzujxl.aicomputerplatform.entity.ArtWork;
import com.fzujxl.aicomputerplatform.mapper.gallery.GalleryMapper;
import com.fzujxl.aicomputerplatform.utils.CacheClient;
import com.fzujxl.aicomputerplatform.utils.HotScoreUtil;
import com.fzujxl.aicomputerplatform.utils.RedisKeys;
import com.fzujxl.aicomputerplatform.utils.RedisTTL;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import static com.fzujxl.aicomputerplatform.utils.RedisKeys.*;
import static com.fzujxl.aicomputerplatform.utils.RedisTTL.*;

@Service
public class GalleryServiceImpl implements GalleryService {

    private final StringRedisTemplate stringRedisTemplate;
    private final CacheClient CacheClient;
    private final GalleryMapper galleryMapper;

    public GalleryServiceImpl(StringRedisTemplate stringRedisTemplate, CacheClient CacheClient, GalleryMapper galleryMapper) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.CacheClient = CacheClient;
        this.galleryMapper = galleryMapper;
    }


    @Override
    public PageResultResponse<GalleryItemResponse> getArtWorksList(ArtworkQueryRequest request) {
        String category = request.getCategory() == null ? "all" : request.getCategory();
        String workType = request.getWorkType() == null ? "all" : request.getWorkType();
        String sort = request.getSort() == null ? "default" : request.getSort();
        String key = String.format(RedisKeys.GALLERY_LIST_CACHE_KEY,
                category, workType, sort, request.getPageNum(), request.getPageSize());

        if ("hot".equalsIgnoreCase(sort)) {
            long ttl = CACHE_HOT_SCORE_TTL + ThreadLocalRandom.current().nextInt(0, 60);
            return queryListWithPassThrough(key, request, ttl);
        }

        long ttl = CACHE_HIDE_SCORE_TTL + ThreadLocalRandom.current().nextInt(0, 120);
        return queryListWithPassThrough(key, request, ttl);
    }

    private PageResultResponse<GalleryItemResponse> queryListWithPassThrough(
            String key, ArtworkQueryRequest request, Long expireTime) {

        //1.从redis查询缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        //2.判断是否存在
        if (StrUtil.isNotBlank(json)) {
            //存在,直接返回
            return JSONUtil.parse(json).toBean(new TypeReference<>() {
            });
        }
        //判断命中的是否是空值
        if (json != null) {
            //返回一个错误信息
            return null;
        }

        //不存在，根据id查询数据库
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<ArtWork> list = galleryMapper.selectByCondition(request);
        List<GalleryItemResponse> dtoList = BeanUtil.copyToList(list, GalleryItemResponse.class);
        PageInfo<GalleryItemResponse> pageInfo = new PageInfo<>(dtoList);
        PageResultResponse<GalleryItemResponse> result = PageResultResponse.of(pageInfo);

        //5.不存在，返回错误
        if (list.isEmpty()) {
            //将空值写入redis
            stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
            //返回一个错误信息
            return null;
        }
        //6.存在，写入redis
        CacheClient.set(key, result, expireTime, TimeUnit.MINUTES);

        return result;
    }

    public static final Long HOT_KEY_ID = 1L;

    @Override
    public ArtworkDetailResponse getArtWorkDetail(Long id, String ip) {
        //没有实现实时检测热key，假设全部为热key
        String hotKey = RedisKeys.HOT_KEY_PREFIX + HOT_KEY_ID;
        CacheClient.setWithLogicalExpire(hotKey, id, RedisTTL.HOT_KEY_TTL, TimeUnit.MINUTES);
        ArtWork artWork = CacheClient.queryWithLogicalExpire(
                ARTWORK_DETAIL_KEY_PREFIX, ARTWORK_DETAIL_LOCK_KEY_PREFIX, id, ArtWork.class, galleryMapper::selectById
                , HOT_KEY_TTL, TimeUnit.MINUTES);

        Long count = recordView(id, ip);
        long hot = HotScoreUtil.calculate(count, LocalDateTime.now());
        artWork.setViewCount(count);
        artWork.setHotScore(hot);
        return BeanUtil.copyProperties(artWork, ArtworkDetailResponse.class);
    }

    //只记录在redis
    private Long recordView(Long id, String ip) {
        String isViewedKey = String.format(GALLERY_IS_VIEWED_KEY, id, ip);
        Boolean existed = stringRedisTemplate.opsForValue().setIfAbsent(isViewedKey, "true", 30, TimeUnit.MINUTES);
        String viewsKey = String.format(GALLERY_VIEW_COUNT_KEY, id);
        if (Boolean.TRUE.equals(existed)) {
            stringRedisTemplate.opsForValue().increment(viewsKey, 1);
        }
        String countStr = stringRedisTemplate.opsForValue().get(viewsKey);
        return Long.parseLong(countStr==null?"0":countStr);
    }

}