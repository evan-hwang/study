

## Jackson

### No serializer found for class 에러

```sh
No serializer found for class com.addpagecorp.taptoon.resource.domain.minigame.data.impl.DrawACardMinigameData$Card and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS)
```

객체를 ObjectMapper를 이용해 JSON 직렬화 하려고하는데 에러가 발생했다.

> 해결법
>
> Nested Class 사용 중 가장 밖의 Class는 @Getter를 붙였으나 내부 클래스는 @Getter를 붙이지 않아서 발생했다.



### Cannot construct instance of 에러

```sh
Cannot construct instance of `com.addpagecorp.taptoon.resource.domain.minigame.data.MinigameData` (no Creators, like default construct, exist): abstract types either need to be mapped to concrete types, have custom deserializer, or contain additional type information
```

interface 를 타입으로 가지고 있는 엔티티를 JPA를 이용해 mySQL에 넣을 때 컨버터를 통해 컨버팅하는 도중 에러가 발생했다.

> 해결법
>
> Jackson은 인터페이스를 기반으로 구현체를 유추할 수 없기 때문에 인터페이스 타입을 역직렬화 할 수 없다. 
>
> 다음의 포스팅을 기반한 방법들이 있다.
>
> - https://andrewtarry.com/posts/deserialising-an-interface-with-jackson/