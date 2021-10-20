# Keys VS Scan

Redis 를 사용 하다 보면, 사용 중 인 cache key 들을 모두 찾아야 하는 경우가 생깁니다.

이 경우 redis 에서는 keys 란 명령어와 scan 명령어를 제공 하는데요,

공식적으로도 keys 명령어는 추천 하지 않고 있습니다.

그 이유는, redis 는 single thread event loop 구조로 명령을 처리 하고 있는데, keys 명령어는 모든 key 를 다 찾을 때 까지 blocking 하기 때문에 다른 client 들의 명령어를 수행 하지 못하고 timeout 발생 할 확률이 높기 때문입니다.

그와 반대로 scan 명령어는 정해진 count 개수 만큼 결과를 가져오고, offset 값을 반환 하기 때문에 keys 명령어 처럼 오랜 시간을 block 하지 않습니다.



## 참고

- [강대명, Kakao Tech](https://tech.kakao.com/2016/03/11/redis-scan/)

- [Scan 성능](https://medium.com/@chlee7746/redis-scan-%EB%AA%85%EB%A0%B9%EC%96%B4-%ED%8D%BC%ED%8F%AC%EB%A8%BC%EC%8A%A4-e29e242b8038)

