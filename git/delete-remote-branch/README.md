# 원격 브랜치 삭제

git 에서 remote branch delete 하는 방법.

삭제할 브랜치 이름은 *feature/TEST-860* 이다.



## 방법 1

로컬에 해당 브랜치가 있을 때

```bash
# 로컬 브랜치 삭제
git branch -d feature/TEST-860 

# 원격 브랜치 삭제 (방법 1)
git push origin --delete feature/TEST-860 

# 원격 브랜치 삭제 (방법 2)
git push origin :feature/TEST-860
```



### -d 옵션

`-d, --delete`

브랜치를 삭제한다. 해당 브랜치가 업스트림 브랜치와 완전히 머지되거나, HEAD에 `--track` 나           `--set-upstream-to` 로 설정된 업스트림이 없어야한다.



### 에러

#### 에러 1

```bash
error: src refspec '<branch_name>' does not match any
error: failed to push some refs to '<remote_repository_name>'
```

해당 에러는 깃허브에서 pull 없이 push할 경우 기존 내용을 삭제하거나 하는 문제가 생길 수 있기 때문에, 이런 문제를 피하고자 에러 메세지를 발생시키는 것. 해당 에러가 발생하면 아래의 순서대로 다시 명령어를 입력한다. 



#### 에러2

```bash
error: Cannot delete branch '<branch_name>' checked out at '<remote_repository_name>'
```

삭제하고자하는 브랜치가 현재 브랜치이기 때문에 다른 브랜치로 전환 후 재시도



## 방법 2

로컬에 해당 브랜치가 없을 때

```bash
git push origin --delete feature/TEST-860
```



## 참고

- https://remagine.tistory.com/17



