## DTO 안의 Enum

DTO에 특정 ENUM이 필요한 경우 대부분 Domain 에서 사용하는 Enum을 가져다 쓰는 경우가 많았다. 



**폴더 구조**

```
- /domain
	- /goods
		- /enums
			- GoodsType.java	// 재화 타입	
		- Goods.java			  // 재화 Entity
- /service
	- /goods
		- /dtos
			- GoodsUpdateDto.java		// 재화 DTO
		- GoodsService.java // 재화 정보 서비스
```



**GoodsDto.java**

```java
public class GoodsUpdateDto {
  
  /**
   * 지급 회수 타입
   */
	private UpdateType updateType;
  
  /**
   * 지급 타입 (코인, 하트)
   */
	private GoodsType goodsType;
  
  /**
   * 지급 수량
   */
	private Long amount;
  
}
```





### 해결책 1





### 해결책 2



## 참고

