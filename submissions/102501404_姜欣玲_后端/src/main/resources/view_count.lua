    -- KEYS[1]: 限流Key (如 limit:video:1001:user:2001)
    -- KEYS[2]: 计数Key (如 view:video:1001)
    -- ARGV[1]: 占坑值
    -- ARGV[2]: 过期时间，传入 1800 (30分钟)
    -- ARGV[4]: 时间戳字段
    -- ARGV[3]: 浏览量字段

    -- 尝试占坑：值固定为 '1'，不存在才设置，30分钟后自动删除
    local res = redis.call('SET', KEYS[1], ARGV[1], 'NX', 'EX', ARGV[2])

    if res then
        -- 占坑成功：首次浏览，浏览量 +1
        local newViews = redis.call('HINCRBY', KEYS[2],ARGV[3],1)
        redis.call('HSET',KEYS[2],'timestamp',ARGV[4])
         return {1,newViews}
    end
        local currviews = tonumber(redis.call('HGET', KEYS[2],ARGV[3]))or 0
        return {0,currviews}


