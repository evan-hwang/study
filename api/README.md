## Path Variable VS Query Parameter

웹에서 특정 데이터를 전송하고 받기 위해서는 어디(End-point)에 요청할 것인가는 중요한 문제이다. 우리는 데이터를 전송하기 위해 `GET`, 전송 받기 위해 `POST `방식을 쓰는데 이 때 각각의 경로(End-point)를 어떻게 정하는 것이 좋을까.

이에 대한 아이디어는 REST API라는 개념을 통해서 알 수 있다. 하지만 그 이전에 중요하게 알아둬야 할 개념이 `Path Variable`과 `Query Parameter`이다. 이 각각의 개념은 무엇이고, 어떤 경우에 써야 되는 것일까 알아보자.


#### 1) Query Parameter

```python
/users?id=123 # Fetch a user who has id of 123 
```

위에서 보는 것처럼 ==`?` 뒤에 id란 변수에 값을 담아== 백엔드에 전달하는 방식이 Query Parameter이다. users에 담긴 정보 중 id 123번의 자료를 달라는 요청이다.


#### 2) Path Variable

```python
/users/123 # Fetch a user who has id 123
```

위와 동일한 요청을 ==경로를 지정==하여 요청할 수도 있는데 이것을 Path Variable이라고 한다.


#### 3) Query Parameter과 Path variable은 각각 언제 쓰면 좋은가?

일반적으로 우리가 어떤 자원(데이터)의 위치를 특정해서 보여줘야 할 경우 Path variable을 쓰고, 정렬하거나 필터해서 보여줘야 할 경우에 Query Parameter를 쓴다. 아래가 바로 그렇게 적용한 사례이다.

```python
/users # Fetch a list of users
/users?occupation=programer # Fetch a list of programer user
/users/123 # Fetch a user who has id 123
```

위의 방식으로 우리는 어디에 어떤 데이터(명사)를 요청하는 것인지 명확하게 정의할 수 있다. 하지만, 그 데이터를 가지고 뭘 하자는 것인지 동사는 빠져있다. 그 동사 역할을 하는 것이 GET, POST, PUT, DELETE 메소드이다.

즉, Query Parameter과 Path variable이 이들 메소드와 결합함으로써 특정 데이터에 대한 CRUD 프로세스를 추가의 엔드포인트 없이 완결 지을 수 있게 되는 것인다.
(가령, `users/create` 혹은 `users?action=create`를 굳이 명시해 줄 필요가 없다.)

```python
GET    /users # Fetch a list of users
POST   /users # Create new user
PUT    /users/123 # Update user
DELETE /users/123 # remove user
```

물론 위와 같은 규칙을 지키지 않더라도 잘 돌아가는 API를 만들 수 있다. 하지만 지키지 않을 경우 서비스 엔드포인트는 복잡해지고, 개발자간/외부와 커뮤니케이션 코스트가 높아져 큰 잠재적 손실을 초래할 수 있으니 이 규칙은 잘 지켜서 사용하는 것이 필수라 하겠다.



### 참고

[When Should You Use Path Variable and Query Parameter?](https://medium.com/@fullsour/when-should-you-use-path-variable-and-query-parameter-a346790e8a6d)

