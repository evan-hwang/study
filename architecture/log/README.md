# 로그 수집, 조회 설계

## in Data

사용자 이력에 대한 데이터를 어떻게 쌓을까하다가 일단 대충 Auto Increase 되는 PK 하나 두고 데이터를 적재하기로 했다.

다음의 고민이 추가적으로 필요

- PK를 어떻게 둘지에 대한 고민



## in SpringBoot

AOP로 어노테이션 기반 로깅을할지, 로그 서비스를 따로 둘지 고민이다. 



### AOP



### by Service



### Envers

어찌할까 고민하다보니 envers라는 라이브러리(?)가 눈에 띈다. 간단하게 얘기하면 hibernate에서 만든 엔티티 변경 이력을 로깅하기 위한 라이브러리다. 

내가 필요한 사용자 이력에 이걸 쓸 수 있을까? 만약 `User` 테이블의 변경 이력이라면 사용할 수 있을 것 같다. 하지만 내가 필요한건 비지니스 로직에 대한 검증을 위한 이력 테이블이기 때문에 해당 라이브러리는 적합하지 않다고 생각한다.

그리고 근래에는 애플리케이션 디비와 로그성 디비를 같이 쓰는 경우가 없기 때문에 해당 라이브러리는 쓰지 않을 예정



## in Architecture









### 시스템

어드민 서비스, 웹툰 플레이 서비스



### 게임 서비스 성능 제약

해당 데이터를 어드민에서 테이블 형식으로 보여줘야하는 요구사항이 있다. 이를 위해선 게임 서비스에 어드민 서버가 직접 요청을 해서 긁어와야하는 상황. 이를 어찌 해결하면 좋을까?

- 메시지 큐 : 카프카
- NoSQL : 레디스
- 직접 긁어오기





## 참고

- [핀터레스트 로깅 아키텍쳐](https://www.youtube.com/watch?v=DphnpWVYeG8&t=238s)
- [쿠버네티스 로깅 아키텍처](https://kubernetes.io/ko/docs/concepts/cluster-administration/logging/)
- [아마존 웹 서비스 로깅 아키텍처](https://docs.aws.amazon.com/solutions/latest/centralized-logging/architecture.html)

