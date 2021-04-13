# 인증(authentication) 기초

유저가 웹 서비스에 접근하기 위해서는 다음의 과정을 거칩니다.

1. 유저 회원가입

2. 유저 아이디 패스워드 서버 저장 및 관리

3. 유저 로그인



이 과정에서 서버는 어떻게 유저의 아이디 패스워드를 관리할지 간단하게 알아보도록 합시다.



1. 유저 회원가입

다음과 같이 테이블을 데이터베이스에 저장했다고 해보자.

| ID   | Password |
| ---- | -------- |
| evan | abcdefg  |
| jade | 123456   |



디비가 해킹 당하거나 내부 직원이 디비를 유출하게 되면 모든 패스워드가 털리게 된다. 이렇게 하면 안된다.



해싱 펑션을 들어봤을 거다 MD5, SHA256  등이 있다 MD5는 잘 안씀. 



## 해시 펑션이란?



SHA256  해서 해시된 값을 저장하는 것이다. 이렇게하면 그냥 저장하는 것보다는 훨씬 편하겠지?

| ID   | Password |
| ---- | -------- |
| evan |          |
| jade |          |



abcdefg를 입력하고 서버 내에서 SHA256  해싱해보고 디비와 같으면 인증 권한을 주는 것이다. 이게 안전할까? 안전하지 않다 Rainbow Table이라는 게 있다. 자주 쓰는 패스워드들을 해시 펑션을 적용해서 역으로 원래 비번을 찾을 수 있다. 

레인보우 테이블 링크 및 예

사람들이 자주 쓰는 비밀번호를 쓰면 위험하다. 어렵게 해야한다.

컬럼을 하나 추가해서 salt, pepper 등 비번 저장 때 랜덤 값 generate해서 추가한다.

| ID   | Password | salt    |
| ---- | -------- | ------- |
| evan |          | dkf341  |
| jade |          | zkvj024 |



패스워드와 salt를 추가하고 해시펑션 돌려서 저장한다.

이제 비번 들어오면 디비에서 설트 밸류 가져와서 붙이고 해싱 펑션 돌린 결과 값이 패스워드랑 비교하면된다. 너무 비번이 쉽지 않다면은 완벽하진 않지만 좀 나아진다.



이렇게 적용되지 않은 웹 사이트에 똑같은 비번을 입력했다면 해커가 이리저리 돌려서 해킹할 수 있다. 이를 위해 구글, 페이스북 같은 인증 모듈을 사용하는걸 추천한다.
