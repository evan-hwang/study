## DATETIME 타입 컬럼 LOCALTIME으로 조회하기

```sql
SELECT id as '아이디', user_id as '유저 아이디', api_type as 'API 타입', story_id as '스토리 아이디', story_title as '스토리 제목', content_type as '챕터 타입', content_id as '챕터 아이디', content_title as '챕터 제목', content_sub_title as '챕터 부제목', move_count as '이동 횟수', CONVERT_TZ(log_date,'+00:00','+09:00') as '로그 일시', log_date
FROM user_play_log
WHERE CONVERT_TZ(log_date,'+00:00','+09:00') 
BETWEEN '2021-04-28 10:00:00' AND '2022-04-29 19:00:00'
```



### 참고

- https://goni9071.tistory.com/392

