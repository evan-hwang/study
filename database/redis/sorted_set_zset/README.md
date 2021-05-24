# Sroted Set (Zset)

Sorted Sets는 key 하나에 여러개의 score와 value로 구성됩니다.
Value는 score로 sort되며 중복되지 않습니다.
score가 같으면 value로 sort됩니다.
Sorted Sets에서는 집합이라는 의미에서 value를 member라 부릅니다.
Sorted Sets은 주로 sort가 필요한 곳에 사용됩니다.



## 구조

![img](/Users/addpage/Dev/my-github/study/database/redis/sorted_set_zset/images/redis-sorted-set-2.png)

우리가 쉽게 접한 단순한 key/value 형태의 자료구조보다 복잡하다. `key`, `member`, `score` 라는 새로운 용어가 등장하기 때문에 더 복잡해 보이기도 하다.
여기서 `key` 는 Redis의 다른 자료구조들과 마찬가지로 단순히 하나의 `ZSET` 에 부여되는 key 이름이다.
`ZSET`은 key/value 형태의 자료구조이고, 여기서 key는 `member`, value는 `score` 라고 부른다. 하나의 `ZSET`에서 `member`는 unique하고, `member` 값을 통해 시간복잡도 O(1)로 해당하는 원소에 바로 접근할 수 있다.
`score` 은 부동 소수점 수만 허용되고, 이 `score` 값을 기준으로 `ZSET` 내의 각 원소들이 순서를 가지게 된다.



## 명령어 요약

- **SET**: ZADD
- **GET**: ZRANGE, ZRANGEBYSCORE, ZRANGEBYLEX, ZREVRANGE, ZREVRANGEBYSCORE, ZREVRANGEBYLEX, ZRANK, ZREVRANK, ZSCORE, ZCARD, ZCOUNT, ZLEXCOUNT, ZSCAN
- **POP**: ZPOPMIN, ZPOPMAX
- **REM**: ZREM, ZREMRANGEBYRANK, ZREMRANGEBYSCORE, ZREMRANGEBYLEX
- **INCR**: ZINCRBY
- **집합연산**: ZUNIONSTORE, ZINTERSTORE

------

| Commands                                                     | Version | Syntax                                                       | Description                                  |
| ------------------------------------------------------------ | ------- | ------------------------------------------------------------ | -------------------------------------------- |
| [ZADD](http://redisgate.kr/redis/command/zadd.php)           | 1.2.0   | key score member [score member ...]                          | 집합에 score와 member를 추가                 |
| [ZCARD](http://redisgate.kr/redis/command/zcard.php)         | 1.2.0   | key                                                          | 집합에 속한 member의 갯수를 조회             |
| [ZINCRBY](http://redisgate.kr/redis/command/zincrby.php)     | 1.2.0   | key increment member                                         | 지정한 만큼 score 증가, 감소                 |
| [ZRANGE](http://redisgate.kr/redis/command/zrange.php)       | 1.2.0   | key start stop [withscores]                                  | index로 범위를 지정해서 조회                 |
| [ZRANGEBYSCORE](http://redisgate.kr/redis/command/zrangebyscore.php) | 1.2.0   | key min max [withscores] [limit offset count]                | score로 범위를 지정해서 조회                 |
| [ZREM](http://redisgate.kr/redis/command/zrem.php)           | 1.2.0   | key member [member ...]                                      | 집합에서 member를 삭제                       |
| [ZREMRANGEBYSCORE](http://redisgate.kr/redis/command/zremrangebyscore.php) | 1.2.0   | key min max                                                  | score로 범위를 지정해서 member를 삭제        |
| [ZREVRANGE](http://redisgate.kr/redis/command/zrevrange.php) | 1.2.0   | key start stop [withscores]                                  | index로 범위를 지정해서 큰 것부터 조회       |
| [ZSCORE](http://redisgate.kr/redis/command/zscore.php)       | 1.2.0   | key member                                                   | member를 지정해서 score를 조회               |
| [ZINCRBY](http://redisgate.kr/redis/command/zincrby.php)     | 1.2.0   | key increment member                                         | 지정한 만큼 score 증가, 감소                 |
| [ZCOUNT](http://redisgate.kr/redis/command/zcount.php)       | 2.0.0   | key min max                                                  | score로 범위를 지정해서 갯수 조회            |
| [ZRANK](http://redisgate.kr/redis/command/zrank.php)         | 2.0.0   | key member                                                   | member를 지정해서 rank(index)를 조회         |
| [ZREVRANK](http://redisgate.kr/redis/command/zrevrank.php)   | 2.0.0   | key member                                                   | member를 지정해서 reverse rank(index)를 조회 |
| [ZREMRANGEBYRANK](http://redisgate.kr/redis/command/zremrangebyrank.php) | 2.0.0   | key start stop                                               | index로 범위를 지정해서 member를 삭제        |
| [ZUNIONSTORE](http://redisgate.kr/redis/command/zunionstore.php) | 2.0.0   | dest_key numkeys src_key [src_key ...] [WEIGHTS weight [weight ...]] [AGGREGATE SUM\|MIN\|MAX] | 합집합을 구해서 새로운 집합에 저장           |
| [ZINTERSTORE](http://redisgate.kr/redis/command/zinterstore.php) | 2.0.0   | dest_key numkeys src_key [src_key ...] [WEIGHTS weight [weight ...]] [AGGREGATE SUM\|MIN\|MAX] | 교집합을 구해서 새로운 집합에 저장           |
| [ZREVRANGEBYSCORE](http://redisgate.kr/redis/command/zrevrangebyscore.php) | 2.2.0   | key max min [withscores] [limit offset count]                | score로 범위를 지정해서 큰 것부터 조회       |
| [ZSCAN](http://redisgate.kr/redis/command/zscan.php)         | 2.8.0   | key cursor [MATCH pattern] [COUNT count]                     | score, member를 일정 단위 갯수 만큼씩 조회   |
| [ZRANGEBYLEX](http://redisgate.kr/redis/command/zrangebylex.php) | 2.8.9   | key min max [limit offset count]                             | member로 범위를 지정해서 조회                |
| [ZLEXCOUNT](http://redisgate.kr/redis/command/zlexcount.php) | 2.8.9   | key min max                                                  | member로 범위를 지정해서 갯수 조회           |
| [ZREMRANGEBYLEX](http://redisgate.kr/redis/command/zremrangebylex.php) | 2.8.9   | key min max                                                  | member로 범위를 지정해서 member를 삭제       |
| [ZREVRANGEBYLEX](http://redisgate.kr/redis/command/zrevrangebylex.php) | 2.8.9   | key max min [limit offset count]                             | member로 범위를 지정해서 큰 것부터 조회      |

Total : 21



## Redis Internal : Sorted Set 내부 데이터 구조

### Sorted Set 데이터 구조

- 스킵 리스트(SKIP LIST): Sorted Set의 메인 데이터 구조인 스킵 리스트를 알면,
  - 이제 우리는 눈을 감고도 ZADD가 어떻게 동작하는지,
  - ZRANGE는 수백만 건의 데이터에서 어떻게 그렇게 빨리 조회할 수 있는지 알 수 있게 될 것이다.
  - 더불어 그동안 Sorted Set에 가지고 있던 의문들이 풀릴 것이다.
- 짚 리스트(ZIP LIST)
  - 짚 리스트의 탄생 배경, 데이터 구조와 기본 동작,
  - Sorted Set에서 사용될 때 성능과 메모리를 얼마나 절약하는지를 알게 된다.





## 참고

- https://jupiny.com/2020/03/28/redis-sorted-set/
- https://meetup.toast.com/posts/225

