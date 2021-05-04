# 로그 모델링 설계를 위한 고민

현재 서비스에서 유저 지표를 측정하기 위해 로그 데이터를 쌓을 필요가 있어서 어떻게해야 효율적으로 Read, Write 할 수 있을지에 대해서 알아보기로한다.



## 로그 설계 작업

### 타이밍

- 기능 개발 할 때 로깅도 함께

### 목적

- 기능의 목표에 맞춰
  - 가설의 확인
  - 지표의 측정

### 속성

- 필요한 항목을 정해서

### 샘플

- 원하는 아웃풋을 시나리오별로 작성
  - 목적을 만족하는지 설계자의 paper 검토
  - 개발자에게 test case로 제공



## 로그의 특징

1. 시간에 따라 발생한다.
2.  동일한 컬럼의 유형에 발생한다.
3. 시간에 따라 반복적으로 발생 하기 때문에 대량의 데이터가 발생할 가능성이 높다.
4.  성능에 영향을 주는 경우가 많다.



## 로그 유형

로그 테이블 모델링을 위해선 먼저 어떤 요구사항으로 로글

### 발생 방법에 따른 구분

**변경 로그**

![image-20210420153600020](/Users/addpage/Library/Application Support/typora-user-images/image-20210420153600020.png)

마스터 테이블이 변경되면 로깅하는 형태

- 마스터 테이블의 컬럼1, 컬럼2가 변경되면 변경되는 데이터를 로그 테이블에 변경 시간과 변경값을 저장한다.



**발생 로그**

![image-20210420160912274](/Users/addpage/Library/Application Support/typora-user-images/image-20210420160912274.png)

- 마스터 테이블의 PK를 포함하여 전체에 대해 인스턴스 생성

- 엄격하게 구분하면 로그 형식이 아닌 인스턴스 생성이라고 구분할 수 있다.



**진행 로그**

![image-20210420161015479](/Users/addpage/Library/Application Support/typora-user-images/image-20210420161015479.png)

- 업무진행 상태에 따라 업무의 상태정보를 관리

- 상태 정보가 계속 영향을 미치는 상태



### 이력 모델 선택 방법

![img](https://t1.daumcdn.net/cfile/tistory/1720C1384F4D968D1E)





### 컬럼과 로우에 따라 구분



### 테이블 구분에 따라 구분





## 참고

- 이춘식, 『아는만큼 보이는 데이터베이스 설계와 구축』, 한빛미디어(2010), 35p
- [Log Design (슬라이드쉐어)](https://www.slideshare.net/SooKyungChoi/log-design)

- https://purumae.tistory.com/tag/%EB%A1%9C%EA%B7%B8%20%ED%85%8C%EC%9D%B4%EB%B8%94
- [위즈덤 마인드 이력에 대한 고찰](https://dataprofessional.tistory.com/42?category=355354)

