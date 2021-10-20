# Deep Dive Into OAuth2.0 and JWT

컴퓨터 기반 애플리케이션의 시작부터 오늘날까지 거의 모든 개발자가 그의 경력 동안 직면해야 하는 가장 흔하지만 복잡한 문제 중 하나는 보안입니다. 즉, 누구에게 어떤 데이터/정보를 제공할 것인지 뿐만 아니라 시간, 검증, 재검증 등과 같은 다른 여러 측면까지  이해하는 것입니다.

보안과 관련된 모든 우려는 두 가지로 나눌 수 있습니다. 인증(Authentication)과 인가(Authorization)입니다.

이 두 용어는 종종 서로 바꿔 사용되지만 기본적으로 다른 기능을 나타냅니다. 기억을 되살리기 위해 이것들을 다시 한번 정의해 보겠습니다.



### 인증(Authentication, AuthN)

*"로그인 자격 증명을 통해 로그인 한 사용자 인식"*

사용자 또는 웹 사이트/응용 프로그램을 인증하는 프로세스로, 확인을 위한 유효한 자격 증명을 제공하여 자신이 누구인지 증명합니다. 인증은 일반적으로 사용자 이름 및 암호를 통해 입증되며, 때때로 사용자만 알고 있는 다른 정보와 함께 인증됩니다. 이러한 정보/요소 집합을 요인(factors)이라고 합니다. 이러한 요인에 따라 인증 메커니즘은 아래의 세 가지 범주로 나뉠 수 있습니다.

- **Single-Factor Authentication**

  - 사용자 이름과 암호(예: 사용자 이름과 암호만 필요한 웹 사이트)에 의존합니다.

- **Two-Factor Authentication**

  - 사용자 이름 및 암호 외에도 기밀 정보(예: 사용자에게만 알려진 PIN을 입력해야 하는 은행 웹 사이트)가 필요합니다.

- **Multi-Factor Authentication (MFA)**

  - 독립적인 범주(예: 사용자 이름 및 암호가 필요한 병원 시스템, 사용자 스마트폰에서 수신한 보안 코드 및 지문)에서 두 개 이상의 보안 요소를 사용합니다.

  

### 인가(Authorization, AuthZ)

*"액세스 제어로 권한(읽기, 수정, 삭제) 부여"*

인가는 사용자가 액세스할 수 있는 항목을 확인하는 프로세스입니다. 권한 부여에서 사용자/응용 프로그램은 허용되는 권한 수준을 결정한 후 특정 API/모듈에 대한 액세스 권한을 부여받습니다. 일반적으로 인증은 인증을 통해 신원을 확인한 후에 이루어집니다.

실제 시나리오에서는 인증과 인증을 모두 사용하여 리소스를 보호해야 합니다. 신원을 증명할 수 있으면 리소스에 액세스할 수 없습니다. 또한 신원을 증명한 경우에도 리소스에 액세스할 수 있는 권한이 없는 경우 액세스가 거부됩니다.



### 토큰 기반 인증

토큰 기반 인증/권한 부여는 사용자가 사용자 이름과 암호를 한 번 입력하고 그 대가로 **고유하게 생성된 암호화된 토큰을 받는 기술**입니다. 그런 다음 이 **토큰은 로그인 자격 증명 대신 보호된 페이지 또는 리소스에 액세스하는 데 사용**됩니다.

토큰 기반 인증확인은 서버에 대한 각 요청이 서명된 토큰과 함께 수행되도록 함으로써 작동하며, 서버는 이 토큰의 신뢰성을 확인한 후 요청에 응답합니다.



### 토큰

"토큰"은 사용자를 고유하게 식별하는 데 필요한 정보가 들어 있는 서버에서 생성되는 데이터 조각입니다. 이것은 임의의 문자와 숫자로 구성된 긴 문자열로 생성됩니다.

cc7112734bbde7708b0284233419 또는 아래와 같이 더 복잡할 수 있습니다.

```
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJtZXNzYWdlIjoiSldUIFJ1bGVzISIsImlhdCI6MTQ1OTQ0ODExOSwiZXhwIjoxNDU5NDU0NTE5fQ.yIVBD5b73C75osbmwwshQNRC7frWUYrqa TjTpza2y4.
```

이 토큰은 그 자체로 의미나 사용은 없지만, 올바른 토큰화 시스템과 결합되면 응용 프로그램 보안에 중요한 측면이 됩니다.



### 왜 토큰을 사용할까?

토큰의 사용은 쿠키와 같은 전통적인 방법에 비해 많은 이점이 있습니다.

- 토큰은 `stateless` 합니다. 
  - 토큰은 `self-contained` 하여 인증에 필요한 모든 정보를 포함합니다. 이것은 서버가 세션 상태를 저장할 필요가 없으므로 확장성에 매우 유용합니다.
- 토큰은 어디에서나 생성할 수 있습니다. 
  - 토큰 생성은 토큰 검증과 분리되므로 별도의 서버 또는 Auth0과 같은 다른 회사를 통해 토큰 서명을 처리할 수 있습니다.
- 세밀한 액세스 제어
  -  토큰 페이로드 내에서 사용자가 액세스할 수 있는 리소스뿐만 아니라 사용자 역할 및 사용 권한을 쉽게 지정할 수 있습니다.



### 토큰 기반 구현

구현은 다양할 수 있지만 기본적 단계는 다음과 같습니다.

1. 사용자가 사용자 이름/암호로 액세스를 요청합니다.
2. 애플리케이션에서 자격 증명을 확인합니다.
3. 애플리케이션은 서명된 토큰을 클라이언트에 제공합니다.
4. 클라이언트는 토큰을 저장하고 모든 요청과 함께 보냅니다.
5. 서버는 토큰을 확인하고 데이터로 응답합니다.

응용 프로그램에서 이 기능을 구현하는 방법에는 제한이 없지만 IETF(Internet Engineering Task Force)에서 정의한 몇 가지 표준이 있습니다. 

가장 인기 있는 두 가지는 다음과 같습니다.

1. OAuth 2.0(RFC 6749 및 RFC 6750)

2. JWT(RFC 7519)

본 글에서는 인증과 인가에 대한 이해를 새롭게 했으며 토큰 기반 인증의 기본 사항도 살펴보았습니다. 다음 글에서는 OAuth2와 JWT에 대해 자세히 알아보겠습니다.



## 참고

- [인증과 인가 포스팅](https://baek.dev/post/24/)
- [DZone - Deep Dive Into OAuth2.0 and JWT](https://dzone.com/articles/deep-dive-to-oauth20-amp-jwt-part-1-setting-the-st?preview=true)
