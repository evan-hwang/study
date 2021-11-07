## Rate Limit Algorithm

서비스를 보호해주는 수단으로 Rate Limit 알고리즘 적용하는데, 이를 효과적으로 적용하기 위해서는 알고리즘에 대한 이해도를 높일 필요가 있고 또, 서비스의 트래픽 특성도 파악해 둘 필요가 있습니다. 여기에서는 Rate Limit 알고리즘 정리하는 것을 목표로 하고 간단한 알고리즘을 구현함으로써 이해도를 높이도록 하겠습니다.



### 1. Leaky Bucket (누수 버킷)

네트워크로의 데이터 주입 속도의 상한을 정해 제어하고 네트워크에서 트래픽 체증을 일정하게 유지한다. 일정한 유출 속도(유출 속도는 고정된 값)를 제한하여 버스트 유입 속도를 부드럽게 한다.

[![Leaky Bucket](https://www.mimul.com/static/53e202f8b985d2acb8fd7081248688ce/fd7a0/rate_leakybucket.png)](https://www.mimul.com/static/53e202f8b985d2acb8fd7081248688ce/fd7a0/rate_leakybucket.png)

- 고정 용량의 버킷에 다양한 유량의 물이 들어오면 버킷에 담기고 그 담긴물은 일정량 비율로 떨어진다.
- 들어오는 물의 양이 많아 버킷의 용량을 초과하게 되면 그 물은 버린다.
- 입력 속도가 출력 속도보다 크면 버킷에서 누적이 발생하고 누적이 버킷 용량보다 큰 경우 오버플로가 발생하여 데이터 패킷 손실이 발생할 수 있다.



#### 1.1 장/단점

- 장점
  - 큐의 크기가 제한되어 있어 메모리 사용량 측면에서 효율적이다.
  - 고정된 처리율을 갖고 있기 때문에 안정적인 출력이 필요한 경우 적합하다.
- 단점
  - 단시간에 많은 트래픽이 몰리는 경우 큐에는 오래된 요청이 쌓이게 되고, 그 요청들을 제때 처리 못하면 최신 요청들은 버려지게 된다.
  - 두 개 인자를 갖고 있는데, 이들을 올바르게 튜닝하기가 까다로울 수 있다.



#### **1.2 샘플 소스**

```java
public class LeakyBucket extends RateLimiter {
  private final long capacity; // 버킷 용량
  private long used; // 버킷 잔여 용량
  private final long leakInterval; // 누수가 실행될 간격
  private long lastLeakTime; // 마지막 누수 시간

  protected LeakyBucket(int maxRequestPerSec) {
    super(maxRequestPerSec);
    this.capacity = maxRequestPerSec;
    this.used = 0;
    this.leakInterval = 1000 / maxRequestPerSec;
    this.lastLeakTime = System.currentTimeMillis();
  }

  @Override
  boolean allow() {
    leak();
    synchronized (this) {
      this.used++;
      if (this.used >= this.capacity) {
        return false;
      }
      return true;
    }
  }
  
  private void leak() {
    final long now = System.currentTimeMillis();
    if (now > this.lastLeakTime) {
      long millisSinceLastLeak = now - this.lastLeakTime;
      long leaks = millisSinceLastLeak / this.leakInterval;
      if(leaks > 0) {
        if(this.used <= leaks){
          this.used = 0;
        } else {
          this.used -= (int) leaks;
        }
        this.lastLeakTime = now;
      }
    }
  }
}
```



#### **1.3 채용 플랫폼**

- [Amazon MWS(Maketplace WEb Service)](https://docs.developer.amazonservices.com/en_IT/dev_guide/DG_Throttling.html)
- [NGINX](https://www.nginx.com/blog/rate-limiting-nginx/)
- [Uber-go rate limiter](https://github.com/uber-go/ratelimit)
- [Shopify](https://help.shopify.com/en/api/reference/rest-admin-api-rate-limits)
- [Guava RateLimiter](https://github.com/google/guava/blob/master/guava/src/com/google/common/util/concurrent/SmoothRateLimiter.java)



### 2. Token Bucket (토큰 버킷)

일시적으로 많은 트래픽이 와도 토큰이 있다면 처리가 가능하면서 토큰 손실 처리를 통해 평균 처리 속도를 제한할 수 있다. 즉, **평균 유입 속도를 제한**하고 처리 패킷 손실없이 특정 수준의 버스트 요청 허용할 수 있다.

[![Token Bucket](https://www.mimul.com/static/34b0a436c691568360a9e46dc7baca1d/cbf7c/rate_tokenbucket.png)](https://www.mimul.com/static/34b0a436c691568360a9e46dc7baca1d/cbf7c/rate_tokenbucket.png)

- 토큰은 정해진 비율로 토큰 버킷에 넣는다.
- 버킷은 최대 n개의 토큰을 저장하며, 버킷이 가득차면 새로 추가된 토큰은 삭제되거나 거부된다.
- 요청이 들어오면 큐에 들어가며 요청을 처리하기 전에 토큰 버킷의 토큰을 획득해야 하며, 토큰을 보유한 후에 요청이 처리되며 처리된 후에는 토큰을 삭제한다.
- 토큰 버킷은 토큰이 배치되는 속도를 기반으로 액세스 속도를 제어한다.
- 전송 횟수를 누적할 수 있으며, 버킷이 가득차면 패킷 손실 없이 토큰이 손실된다.



#### 2.1 장/단점

- 장점
  - 구현이 쉬움
  - 메모리 사용 측면에서 효율적
  - 버스트(짧은 시간에 집중되는 트래픽)도 처리 가능
- 단점
  - 버킷 크기와 토큰 공급률이라는 두 개 인자를 적절히 튜닝하는게 어려움



#### **2.2 샘플 소스**

```java
public class TokenBucket extends RateLimiter {
  private int tokens;
  private int capacity;
  private long lastRefillTime;

  public TokenBucket(int maxRequestPerSec) {
    super(maxRequestPerSec);
    this.tokens = maxRequestPerSec;
    this.capacity = maxRequestPerSec;
    this.lastRefillTime = scaledTime();
  }

  @Override
  public boolean allow() {
    synchronized (this) {
      refillTokens();
      if (this.tokens == 0) {
        return false;
      }
      this.tokens--;
      return true;
    }
  }

  private void refillTokens() {
    final long now = scaledTime();
    if (now > this.lastRefillTime) {
      final double elapsedTime = (now - this.lastRefillTime);
      int refill = (int) (elapsedTime * this.maxRequestPerSec);
      this.tokens = Math.min(this.tokens + refill, this.capacity);
      this.lastRefillTime = now;
    }
  }

  private long scaledTime() {
    return System.currentTimeMillis() / 1000;
  }
}
```



#### **2.3 채용 플랫폼**

- AWS : [API Gateway](https://aws.amazon.com/ko/blogs/aws/new-usage-plans-for-amazon-api-gateway/), [EC2](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/throttling.html#throttling-limits), [EBS](https://aws.amazon.com/ko/blogs/aws/new-ssd-backed-elastic-block-storage/), CPU Credit
- [Spring Cloud Netflix Zuul](https://github.com/marcosbarbero/spring-cloud-zuul-ratelimit)
- [Bucket4j](https://github.com/vladimir-bukhtoyarov/bucket4j)



### 3. Fixed Window Counter (고정 윈도 카운터)

정해진 시간 단위로 window가 만들어지고 요청 건수가 기록되어 해당 window의 요청 건수가 정해진 건수보다 크면 해당 요청은 처리가 거부된다. 이 알고리즘을 사용하면 경계의 시간대(12:59, 13:01초에 몰리면)에 요청이 오면 두배의 부하를 받게 된다. 즉, 구현은 쉽지만, 기간 경계의 트래픽 편향 문제가 발생된다.

[![Fixed Window Counter](https://www.mimul.com/static/fc309200b26de4d6322f48fd3719518a/34e8a/rate_fixed_window_counter.png)](https://www.mimul.com/static/fc309200b26de4d6322f48fd3719518a/34e8a/rate_fixed_window_counter.png)

- 버킷은 타임라인에 따라 고정된 window로 나누고, 각 window 마다 카운터를 붙인다.
- 요청이 접수될 때마다 이 카운터의 값이 1씩 증가한다.
- 카운터 값이 임계치(threshold)에 도달하면 새로운 요청은 새 window가 열릴 때까지 버려진다.



#### 3.1 장/단점

- 장점
  - 메모리 효율이 좋음
  - 이해하기 쉬움
  - window가 닫히는 시점에 카운터를 초기화하는 방식은 특정 트래픽 패턴을 처리하기 적합
- 단점
  - window 경계 부근에서 일시적으로 많은 트래픽이 몰려드는 경우, 기대했던 시스템의 처리 한도보다 많은 양의 요청을 처리한다.



#### **3.2 샘플 소스**

```java
public class FixedWindowCounter extends RateLimiter {
  private final ConcurrentMap<Long, AtomicInteger> windows = new ConcurrentHashMap<>(); // window<시간, 카운터> 맵
  private final int windowSizeInMs; // ms 당 window 개수

  protected FixedWindowCounter(int maxRequestPerSec, int windowSizeInMs) {
    super(maxRequestPerSec);
    this.windowSizeInMs = windowSizeInMs;
  }

  @Override
  boolean allow() {
    long windowKey = System.currentTimeMillis() / windowSizeInMs;
    windows.putIfAbsent(windowKey, new AtomicInteger(0));
    return windows.get(windowKey).incrementAndGet() <= maxRequestPerSec;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder("");
    for(Map.Entry<Long, AtomicInteger> entry:  windows.entrySet()) {
      sb.append(entry.getKey());
      sb.append(" --> ");
      sb.append(entry.getValue());
      sb.append("\n");
    }
    return sb.toString();
  }
}
```



### 4. Sliding Window Log (이동 윈도 로깅)

> TCP의 Sliding Window Protocol

Fixed window counter의 단점인 기간 경계의 편향에 대응하기 위한 알고리즘이다. 타임스탬프 데이터는 보통 레디스의 정렬 집합(sorted set)과 같은 캐시에 보관한다.

[![Sliding Window Log](https://www.mimul.com/static/75653188f6e1eb96a9e9bf861f538f6e/a49a1/rate_sliding-window-log.png)](https://www.mimul.com/static/75653188f6e1eb96a9e9bf861f538f6e/a49a1/rate_sliding-window-log.png)

- 한도가 2개이다를 가정
- 요청이 12초에 도착했을 때
  - 해당 타임 스탬프가 로그에 추가된다. 
  - 로그는 비어 있는 상태이다. 따라서 요청은 허용된다.
- 요청이 24초에 도착했을 때
  - 해당 타임 스탬프가 로그에 추가된다. 
  - 추가 직후 로그의 크기는 2이며, 허용 한도보다 크지 않은 값이다.
  - 요청은 시스템에 전달된다.
- 요청이 36초에 도착했을 때
  - 해당 타임 스탬프가 로그에 추가된다.
  - 추가 직후 로그의 크기는 3이므로 허용 한도보다 큰 값이다.
  - 타임 스탬프는 로그에 남지만 요청은 거부된다.
- 요청이 1분 25초에 도착했을 때
  - 해당 타임 스탬프가 로그에 추가된다. 
  - [00:00:25 ~ 00:01:25] 범위 안에 요청은 1분 window 안에 있는 요청이지만, 00:00:25 이전의 타임스탬프는 모두 만료된 값이다. 
  - 만료된 12초, 24초에 대한 타임스탬프를 로그에서 삭제한다.
  - 삭제 직후 로그의 크기는 2이다. 따라서 1분 25초 요청은 시스템에 전달된다.



#### 4.1 장/단점

- 장점
  - 매우 정교하다. 
  - 어느 순간의 window를 보더라도 허용되는 요청의 개수는 시스템의 처리율 한도를 넘지 않음
- 단점
  - 거부된 요청이 타임 스탬프도 보관하기 때문에, 다량의 메모리 비용이 사용됨



#### **4.2 샘플소스**

```java
public class SlidingWindowLog extends RateLimiter {
  private final Queue<Long> windowLog = new LinkedList<>();

  protected SlidingWindowLog(int maxRequestPerSec) {
    super(maxRequestPerSec);
  }

  @Override
  boolean allow() {
    long now = System.currentTimeMillis();
    long boundary = now - 1000; // 윈도우 범위
    
    synchronized (windowLog) {
      // 요청 범위 이전의 만료된 타임스탬프 제거
      while (!windowLog.isEmpty() && windowLog.element() <= boundary) {
        windowLog.poll();
      }
      
      // 신규 요청 타임스탬프 추가
      windowLog.add(now);
      log.info("current time={}, log size ={}", now, windowLog.size());
      
      // 허용 한도내의 요청인지 판단
      return windowLog.size() <= maxRequestPerSec;
    }
  }
}
```



### 5. Sliding Window Counter (이동 윈도 카운터)

Fixed window counter의 경계 문제와 Sliding window log의 로그 보관 비용 등의 문제점을 보완할 수 있는 알고리즘이다.

[![Sliding Window Counter](https://www.mimul.com/static/1823b61524cd87ef9438cdca3d395a92/e681d/rate_sliding-window.png)](https://www.mimul.com/static/1823b61524cd87ef9438cdca3d395a92/e681d/rate_sliding-window.png)

- rate limter이 처리 한도는 분당 10개이다.
- 이전 1분 동안 9건의 요청이 왔고, 현재 1분 동안 5건의 요청이 왔다고 가정하자.
- 현재 1분의 25% 지점에 도착한 새 요청의 경우
  - 현재 1분간의 요청 수 + 직전 1분간의 요청 수 x 이동 window와 직전 1분이 겹치는 비율
  - 5 + 9  x 75% = 11.75개
  - 분당 10개보다 초과했기 때문에 요청은 거부된다. 
- 현재 1분의 50% 지점에 도착한 새 요청의 경우
  - 현재 1분간의 요청 수 + 직전 1분간의 요청 수 x 이동 window와 직전 1분이 겹치는 비율
  - 5 + 9  x 50% = 9.5개
  - 분당 10개 한도를 넘지 않았기 때문에 요청은 처리된다.

Sliding Window Counter는 window의 비율이 소수점이 나오게 되면 정확성이 떨어질 수는 있으나, Fixed window counter의 경계 문제와 Sliding window log의 로그 보관 비용 등의 문제점을 개선하게 된다.



#### 5.1 장/단점

- 장점
  - 이전 시간대의 평균 처리율에 따라 현재 윈도우의 상태를 계산하므로 짧은 시간에 몰리는 트래픽에도 잘 대응
  - 메모리 효율이 좋음
- 단점
  - 직전 시간대에 도착한 요청이 균등하게 분포되어 있다고 가정한 상태에서 추정치를 계산하기 때문에 다소 느슨하다.
  - 생각만큼 심각하진 않다. Cloudflare가 실시했던 실험에 따르면 40억개의 요청 가운데 시스템의 실제 상태와 맞지 않게 허용되거나 버려진 요청은 0.003%에 불과했다.



#### **5.2 샘플 소스**

```java
public class SlidingWindow extends RateLimiter {
  private final ConcurrentMap<Long, AtomicInteger> windows = new ConcurrentHashMap<>();
  private final int windowSizeInMs;

  protected SlidingWindow(int maxRequestPerSec, int windowSizeInMs) {
    super(maxRequestPerSec);
    this.windowSizeInMs = windowSizeInMs;
  }

  @Override
  boolean allow() {
    long now = System.currentTimeMillis();
    long curWindowKey = now / windowSizeInMs;
    windows.putIfAbsent(curWindowKey, new AtomicInteger(0));
    long preWindowKey = curWindowKey - 1000;
    AtomicInteger preCount = windows.get(preWindowKey);
    if (preCount == null) {
      return windows.get(curWindowKey).incrementAndGet() <= maxRequestPerSec;
    }
    double preWeight = 1 - (now - curWindowKey) / 1000.0;
    long count = (long) (preCount.get() * preWeight + windows.get(curWindowKey).incrementAndGet());
    return count <= maxRequestPerSec;
  }
}
```



#### **5.3 채용 플랫폼**

- [RateLimitJ](https://github.com/mokies/ratelimitj)