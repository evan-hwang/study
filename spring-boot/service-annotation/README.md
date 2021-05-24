# @Service Annotation

`@Service` 어노테이션을 붙여서 해당 서비스를 빈으로써 DI 하게 되는데

> Q. `@Service` 를 붙여야하는 이유는?



대체로 `Service` 인터페이스와 `ServiceImpl` 구현체를 통해서 서비스를 구현한다. 이 때 `@Service` 어노테이션은 어디에 붙여야할까?

> Q. Service를 인터페이스가 아닌 바로 클래스로 구현하면 어떨까?



> Q. Service를 매 요청마다 객체화하지 않고 Singletone 방식으로 사용하는 이유?



## 참고

- https://m.blog.naver.com/scw0531/220988401816

