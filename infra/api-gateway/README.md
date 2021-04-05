## API 게이트웨이에서 수행하는 작업

API 게이트웨이는 클라이언트와 백엔드 서비스 컬렉션 사이에 위치하는 [API 관리](https://www.redhat.com/ko/topics/api/what-is-api-management) 툴입니다.

API 게이트웨이는 모든 [애플리케이션 프로그래밍 인터페이스(Application Programming Interface, API)](https://www.redhat.com/ko/topics/api/what-are-application-programming-interfaces) 호출을 수락하고 호출 이행에 필요한 다양한 서비스를 집계하며 적절한 결과를 반환하는 리버스 프록시 역할을 합니다.

대부분의 엔터프라이즈 API는 API 게이트웨이를 통해 배포됩니다. API 게이트웨이의 경우 주로 사용자 인증, 속도 제한, 통계 등 API 서비스 시스템 전반에서 사용되는 일반적인 태스크를 처리합니다.

[온디맨드 API 관리 및 보안 데모 확인하기](https://www.redhat.com/ko/events/webinar/open-demo-managing-and-securing-apis-3scale-april-17-2019)

### API 게이트웨이를 사용하는 이유

API 서비스는 기본적으로 원격 요청을 수락하고 응답을 반환합니다. 하지만 실제로는 이렇게 간단하게 실행되지는 않습니다. 대규모 API를 호스팅할 때는 다양한 사항을 고려해야 합니다.

- API가 남용되거나 과도하게 사용되지 않도록 보호하기 위해 인증 서비스나 속도 제한을 사용할 수 있습니다. 
- API가 어떻게 사용되고 있는지를 알고 싶은 경우 분석 및 모니터링 툴을 추가할 수 있습니다.
- [수익화 API](https://www.redhat.com/ko/topics/api/what-is-api-monetization)가 있다면 빌링 시스템에 연결할 수 있습니다.
- 단일 요청으로 서로 다른 수십 개의 애플리케이션에 대한 호출을 필요로 하는 경우 [마이크로서비스](https://www.redhat.com/ko/topics/microservices/what-are-microservices) 아키텍처를 채택할 수 있습니다.
- 이렇게 시간이 지남에 따라 새로운 API 서비스를 추가하거나 사용 종료하게 되지만, 고객은 계속해서 모든 서비스를 동일한 장소에서 찾기를 원합니다.

이러한 모든 복잡성을 해결하기 위해서는 고객에게 간단하고 신뢰할 수 있는 환경을 제공하는 것이 과제라고 할 수 있습니다. API 게이트웨이는 클라이언트 인터페이스를 백엔드 구현 환경에서 분리할 수 있는 방법입니다. 클라이언트가 요청을 하면 API 게이트웨이가 이를 여러 개의 요청으로 나누어 적절한 위치로 전달하고, 응답을 생성하며, 모든 상황을 추적합니다.

[다가오는 API 가상 워크숍 등록하기](https://www.redhat.com/ko/demo-in-person-event-patterns)

### API 관리에서 API 게이트웨이의 역할

API 게이트웨이는 API 관리 시스템의 한 부분입니다. API 게이트웨이는 수신되는 모든 요청을 가로채서 API 관리 시스템을 통해 전송하여 필요한 다양한 기능을 처리합니다.

API 게이트웨이의 정확한 역할은 구현 환경마다 달라집니다. 일반적인 기능으로는 인증, 라우팅, 속도 제한, 빌링, 모니터링, 분석, 정책, 알림, 보안이 있습니다.

[API를 관리하는 데 필요한 기술 알아보기](https://www.redhat.com/ko/page-not-found)

### API 게이트웨이가 DevOps 및 서버리스 환경을 지원하는 방법

[DevOps](https://www.redhat.com/ko/topics/devops) 접근 방식을 따르는 조직에서 개발자는 마이크로서비스를 사용해 빠르고 반복적인 방식으로 애플리케이션을 빌드하고 배포합니다. API는 마이크로서비스가 통신하는 가장 일반적인 방식 중 하나입니다.

또한 [서버리스(serverless)](https://www.redhat.com/ko/topics/cloud-native-apps/what-is-serverless) 모델을 비롯한 현대적인 클라우드 개발은 API에 의존해 인프라 프로비저닝을 수행합니다. API 게이트웨이를 사용해 서버리스 기능을 배포하고 관리할 수 있습니다.

전반적으로 통합 및 상호연결성과 마찬가지로 API도 그 중요성이 가중되고 있습니다. 또한 API 복잡성이 증가하고 사용량이 많아짐에 따라, API 게이트웨이의 가치도 높아집니다.