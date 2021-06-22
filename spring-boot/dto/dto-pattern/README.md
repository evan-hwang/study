### 효율적인 DTO 패턴에 대한 고찰



우리 학급 관리 시스템은 `Class` 데이터를 들고 있다. 이 데이터를 컨슈머에게 제공하기위해 API 를 제공하기로 했다.

`Class.java`

```java
@Entity
public class Class {
  
	private Long id;						// 학급 아이디
  private Integer floor;			// 학급 층
  private String teacher;			// 담당 선생님
  private Long count;					// 학생 수
  private Long fixedCount;		// 학급 정원
  private Long budget;				// 예산
  private String budgetUnit;	// 예산 단위
}
```



### API 구조

다음의 API가 필요하다고 가정해보자.

1. 학급 정보 조회 (학생)
2. 학급 정보 조회 (관리자)
3. 학급 생성 (관리자)



### API 입력과 출력

이 API에 입력과 출력을 추가해보자.

1. 학급 정보 조회 (학생)

학생이 조회한 경우 교육청에서 지정한 학급 정보는 필요 없기 때문에 제한된 정보를 제공한다.

- 입력

- 출력

  - 응답 바디

    ```json
    {
    	"floor": 1,
      "teacher": "황혁진",
      "count": 13
    }
    ```

    

2. 학급 정보 조회 (관리자)

관리자가 조회한 경우 내부 보안 정보도 제공한다.

- 입력

- 출력

  - 응답 바디

    ```json
    {
    	"floor": 1,
      "teacher": "황혁진",
      "count": 13,
      "fixedCount": 30,
      "budget": 300000,
    	"budgetUnit": "원"
    }
    ```



3. 학급 생성(관리자)

새로운 학급을 신설한다.

- 입력 

  - 입력 바디

  ```json
  {
  	"floor": 2,
    "teacher": "김광석",
    "fixedCount": 50,
    "budget": 1000000,
  	"budgetUnit": "원"
  }
  ```

  

- 응답

  - 응답 바디

  ```json
  {
  	"floor": 2,
    "teacher": "김광석",
    "count": 0, 
    "fixedCount": 50,
    "budget": 1000000,
  	"budgetUnit": "원"
  }
  ```



> ✏️ Entity와 DTO 데이터 구조
>
> Entity(도메인 객체)에는 상태, ID 및 비즈니스 논리가있는 반면 DTO에는 상태만 있습니다.



### DTO 생성

이를 기반으로 DTO를 생성한다. 내가 생각하는 방법은 크게 두 가지가 있는 것 같다.



#### 1. DTO별 파일 생성

```json
- ClassGetResponseDto
- ClassAdminGetResponseDto
- ClassAdminCreateRequestDto
- ClassAdminCreateResponseDto
```



#### 2. DTO별 생성하되 Nested Class로 요청, 응답 값 관리

```json
- ClassGetDto
- ClassGetDto.Response

- ClassAdminGetDto
- ClassAdminGetDto.Response

- ClassAdminCreateDto
- ClassAdminCreateDto.Request
- ClassAdminCreateDto.Response
```



#### 2. 하나의 Entity에 대해 한 개의 DTO 생성

```json
- ClassDto

- ClassDto.Get
- ClassDto.Get.Response

- ClassDto.AdminGet
- ClassDto.AdminGet.Response

- ClassDto.AdminCreate
- ClassDto.AdminCreate.Request
- ClassDto.AdminCreate.Response
```



## 참고

- https://www.edgesidesolutions.com/think-before-you-use-the-dto-pattern/
- https://blog.scottlogic.com/2020/01/03/rethinking-the-java-dto.html
- https://dzone.com/articles/dtos