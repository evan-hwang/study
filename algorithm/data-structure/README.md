# 자료구조 with Java

주특기로 선정한 Java 언어를 통해 자료구조를 누구에게든 자신있게 설명하기 위해 기록한다.



## 인터페이스

### 리스트가 두 종류인 이유

JCF를 사용하다보면 종종 ArrayList와 LinkedList 클래스를 혼동한다. 왜 자바는 두 가지 구현을 제공할까? 이에 대해서 알아보도록하자.

> **JCF(Java Collection Framework)란?**
>
> Java에서 데이터를 저장하는 기본적인 자료구조들을 한 곳에 모아 관리하고 편하게 사용하기 위해서 제공하는 것을 의미한다. 즉, 데이터를 담는 그릇들에 대한 정의를 모아놓은 프레임워크를 의미하며, 값의 성격이나 용도에 따라서 활용 방법에 따라 다양한 컨테이너를 제공한다. 다음은 JCF의 상속 구조이며 사용 용도에 따라 List, Set, Map 3가지로 요약할 수 있다. ![img](https://blog.kakaocdn.net/dn/bWPFKb/btqE9dS7CZB/fskG63meM2HyKRu55VCV8k/img.jpg)



### 자바 interface

자바 인터페이스는 해당 인터페이스를 구현하는 클래스가 해당 인터페이스의 메소드 집합을 구현하도록한다. 다음은 java.lang 패키지에 정의된 Comparable interface의 소스코드이다.

```java
public interface Comparable<T> {
	public int compareTo(T o);
}
```

이 인터페이스를 구현하는 클래스는 다음의 조건을 만족시켜야한다.

- T 타입을 명시해야한다.
- T 타입의 객체를 인자로 받고 int를 반환하는 compareTo() 메서드를 제공해야한다.



예를 들어, java.lang.Integer 클래스의 소스 코드는 다음과 같다.

```java
public final class Integer extends Number implements comparable<Integer> {
	public int compareTo(Integer anotherInteger) {
		int thisVal = this.value;
		int anotherVal = anotherInteger.value;
		return (thisVal<anotherVal ? -1 : (thisVal==anotherVal ? 0 : 1));
	}
}
```

클래스가 인터페이스를 구현한다고 선언하면 컴파일러가 해당 클래스가 구현할 인터페이스의 모든 메서드를 제공하는지 확인한다.



### List interface







## 공부 플랜



## B-Tree

### B+-Tree

### B*-Tree



## ArrayList

### SortedList



## 참고

- Think Data Structures
- [Java Collection Framework (JCF)의 이해](https://dodo-factory.tistory.com/6)

