# 일상 속 사물이 알려주는 웹 API 디자인

Restful API니 Web API니 웹 백엔드 개발자로서 이 부분에 대해서 협의할 부분이 많다. 하지만 해당 부분에 대해 논의가 이루어질 때 내 의견을 개진하기에는 부족한 부분이 많아 채우기 위해 해당 서적을 읽고 정리하고자한다.

> 미해결 논의
>
> 1. stories/{storyId}/chapters/{chapterId} 대신 request Body에 다음과 같이 넣으면?
>
> ```json
> {
>   storyId: 1, 
>   chapterId: 2
> }
> ```
>
> 
>
> 2. 특정 스토리에 속하지 않고 운영되는 챕터에 대해서 요청이 필요할 때
>    - stories/{storyId}/chapters/{chapterId}
>      - 여전히 stories 안에 포함하는게 낫지 않을까?
>    - chapters/{chapterId}
>      - 아니다 온전한 챕터이기 때문에 리소스 위치 상 다음과 같이 표현해야한다.
>
> 
>
> 3. 게임 API 에서 리소스 위치가 명확한가?
>
>    - stories/{storyId}/chapters/{chapterId}/start
>
>      - 해당 챕터를 시작한다를 위와 같이 나타내는게 Restful 한가?
>
>    - stories/{storyId}/chapters/{chapterId}
>
>      ```json
>      {
>        action: "start"
>      }
>      ```
>
>      - request Body로 다음과 같이 action을 표현하는게 나을까?
>
>    - stories/{storyId}/chapters/{chapterId}?action=start
>
>      - query string(request param) 으로 남기는게 나을까?



