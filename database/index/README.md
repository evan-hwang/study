#### index naming convention

**dont**

![img](https://i2.wp.com/sqlespresso.com/wp-content/uploads/2018/01/Idx1.jpg?resize=551%2C140)



**do**

![img](https://i0.wp.com/sqlespresso.com/wp-content/uploads/2018/01/idx2.jpg?resize=551%2C123)



PK_ for primary keys

UK_ for unique keys

IX_ for non clustered non unique indexes

UX_ for unique indexes



Also recommended naming conventions

- PKC_ Primary Key, Clustered
- PKNC_ Primary Key, Non Clusterd
- NCAK_ Non Clustered, Unique
- CAK_ Clustered, Unique
- NC_ Non Clustered



###  index와 constraint는 descriptive하게 작성한다.

\- 예를 들어 index의 경우 테이블명, 속성명, 인덱스 유형이 포함되어야 한다.

```
ex) user_ix (X) -> user_ix_email_lower
```





###  다중 컬럼일 때 더 큰 스코프의 인덱스만 있어도 되는가?

```sql
# 1
CREATE INDEX `idx_student_school_id_class_id` ON `student` (`school_id` ASC, `class_id` ASC);

# 2
CREATE INDEX `idx_student_school_id_class_id_student_id` ON `user_content` (`school_id` ASC, `class_id` ASC, `student_id` ASC);
```



school_id, class_id로 조회 시 아래의 인덱스 만으로도 인덱스를 탈 수 있을까?



**참고**

Real MySQL, 이성욱, 위키북스, 231p