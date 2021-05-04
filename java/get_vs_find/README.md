# Get vs Find 메소드명 규칙



3년 전에 저는 "데이터 리포지토리에 옵션 사용 안 함"이라는 자극적인 제목으로 블로그에 글을 썼습니다. 그 게시물에는 비판적인 평이 몇 개 올라왔고 저는 제 주장을 분명히 하지 못했다는 느낌이 들었습니다.

이번 주에 저는 같은 주제를 또 다시 다루었지만, 약간 다른 관점에서 보았습니다. 저는 몇 개의 저장소 인터페이스를 디자인하려고 시도하다가 제 방법의 이름을 어떻게 붙여야 하는지에 대한 문제에 봉착했습니다. 제가 생각한 것은 이렇습니다.



# A repository interface

Repository interfaces typically implement the same set of methods. For example the [CRUD repository of Spring Data](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html) has amongst others the following methods:

리포지토리 인터페이스는 일반적으로 동일한 메서드 집합을 구현합니다. 예를 들어 [Spring Data의 CRUD 저장소](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html)에는 다음과 같은 방법이 있습니다.

```java
public interface CrudRepository<T,ID> {
    ...
    Optional<T> findById(ID id);
    Iterable<T> findAll();
    ...
}
```



So is there anything wrong with these methods? – No, not at all. But let’s compare it to another repository interface from Spring Data – [the JPA repository](https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html):

그렇다면 이 방법들에 무슨 문제가 있나요? - 아뇨, 전혀요. 그러나 Spring Data의 [JPA repository](https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html))의 다른 저장소 인터페이스와 비교해 보겠습니다.

```java
public interface JpaRepository<T,ID> {
    ...
    T getOne(ID id);
    List<T> findAll();
    ...
}
```



Also, this interface is perfectly fine. However, there’s a small but important difference between the two repositories: while the CRUD interface expects to find nothing, the JPA interface expects to always find something.

또한 이 인터페이스는 아주 좋습니다. 그러나 두 리포지토리 사이에는 작지만 중요한 차이가 있습니다. CRUD 인터페이스는 아무것도 찾지 못할 것으로 예상하지만 JPA 인터페이스는 항상 무언가를 찾을 것으로 예상합니다.



# find vs. get

The difference between the two interfaces lies in the semantic of their methods. The CRUD repository “finds” something whereas the JPA repository “gets” something. While “find” might lead to no result at all, “get” will always return something – otherwise the JPA repository [throws an exception](https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html#getOne-ID-).

두 인터페이스 간의 차이는 방법의 의미에 있습니다. CRUD 저장소는 무언가를 "찾는" 반면 JPA 저장소는 무언가를 "찾습니다". "find"(찾기)로 인해 아무런 결과가 없을 수도 있지만, "get"(겟)은 항상 무언가를 반환합니다. 그렇지 않으면 JPA 저장소 [exception](https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html#getOne-ID-))가 반환됩니다.



# The rules

This semantic leads to the following rules:

- Return an `Optional` if you try to *find*, *search* or *look-up* something. Finding nothing is an expected outcome in this case. The caller of the method must deal with it.
- Return the entity or throw an exception if it doesn’t exist in case you reference it directly (*get* or *load*). In this case, it’s not a valid state if the entity doesn’t exist – it’s an exception. However, by throwing an exception the caller doesn’t need to handle the inconsistency of the persistence layer.
- Return an (possibly empty) list in case of multiple elements in a result. In this case, it doesn’t matter if you have something or not (== the list is empty). It’s transparent for the caller.
- Never return null. It’s the at least meaningful return value you can choose.

이 시맨틱은 다음과 같은 규칙으로 이어집니다.

- *찾기*, *검색* 또는 *찾기*를 시도하는 경우 '선택사항'을 반환합니다. 이 경우 아무것도 찾지 못하는 것은 예상된 결과입니다. 메소드의 호출자는 해당 메서드를 처리해야 합니다.
- 엔티티를 반환하거나 존재하지 않는 경우 직접 참조할 경우 예외로 합니다. (*get* 또는 *load*) 이 경우, 기업이 존재하지 않는 경우에는 유효한 상태가 아닙니다. 예외입니다. 그러나 예외를 적용함으로써 발신자는 지속성 계층의 불일치를 처리할 필요가 없습니다.
- 결과에서 여러 요소가 있을 경우 목록을 반환합니다. 이 경우, 무엇이 있든 없든 상관없습니다(== 목록이 비어 있음). 전화를 건 사람에게는 투명합니다.
- null을 반환하지 않습니다. 최소한 의미 있는 반환 값을 선택할 수 있습니다.



# Use cases

Having said that, what are suitable use cases for *find* and *get*?

Let’s make an example and assume that we’ve implemented a book shop. In this book shop an user can search for a book by its title. In this case, it’s perfectly fine that we don’t find anything. The user might have made a typo or the book just doesn’t exist. So we would use something similar to `Optional findById(ID id);`.

However, if we go one step further we run into another use case. Let’s assume the user has found a book and made an order. Eventually, we want to send an invoice to the user to get our money. To do so, we load the order and look-up the book the user bought to get its price. In this case, the book must exist! Otherwise something is really broken. So we would use something like `T getOne(ID id);`.

Of course, a real world scenario would be much more complicated. The search would probably use a search index like Solar and the order would possible have a foreign key on the book which ensures the integrity at the database level.

이미 언급한 바와 같이 *찾기* 및 *구입*에 적합한 사용 사례는 무엇입니까?

예를 들어 서점을 구현했다고 가정해 보겠습니다. 이 서점에서는 사용자가 제목으로 책을 검색할 수 있습니다. 이 경우, 아무것도 찾지 못한 것이 완벽하게 괜찮습니다. 사용자가 오타를 냈거나 책이 존재하지 않았을 수 있습니다. 그래서 우리는 '선택적 findById(ID ID)'와 비슷한 것을 사용할 것입니다.

그러나 한 단계 더 나아가면 다른 사용 사례에 직면하게 됩니다. 사용자가 책을 찾아 주문을 했다고 가정해 보겠습니다. 결국, 우리는 우리의 돈을 받기 위해 사용자에게 청구서를 보내고 싶습니다. 그러기 위해, 우리는 주문을 로드하고 사용자가 구입한 책을 찾아 가격을 알아봅니다. 이 경우, 책은 반드시 존재해야 합니다! 그렇지 않으면 무언가가 정말 망가진 것입니다. 그래서 우리는 "T get One"과 같은 것을 사용할 것입니다.

물론, 현실세계의 시나리오는 훨씬 더 복잡할 것입니다. 이 검색에서는 솔라 같은 검색 색인을 사용할 수 있으며 주문서에 데이터베이스 수준에서 무결성을 보장하는 외래 키가 있을 수 있습니다.



## 참고

- https://tuhrig.de/find-vs-get/

- https://softwareengineering.stackexchange.com/questions/182113/how-and-why-to-decide-between-naming-methods-with-get-and-find-prefixes