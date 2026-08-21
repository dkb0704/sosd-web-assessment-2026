local val = redis.call('HGET', KEYS[1],ARGV[1])
if val then
    redis.call('HDEL',KEYS[1],ARGV[1])
    end
return val
