# 2. Git의 기초

이 장에서 다루는 내용

- 자주 사용하는 명령어
- 저장소를 만들고 설정하는 방법
- 파일을 추적(Track)하고 그만두는(UnTrack) 방법
- 실수를 쉽고 빠르게 만회하는 방법
- 프로젝트 히스토리 조회하고 커밋 비교하는 방법
- 리모트 저장소에 Push하고 Pull 하는 방법



## 2.1 Git 저장소 만들기

주로 다음 두 가지 중 한 가지 방법으로 Git 저장소를 쓰기 시작한다.

1. 아직 버전관리를 하지 않는 로컬 디렉토리 하나를 선택해서 Git 저장소를 적용하는 방법 (`git init`)
2. 다른 어딘가에서 Git 저장소를 *Clone* 하는 방법 (`git clone`)

어떤 방법을 사용하든 로컬 디렉토리에 Git 저장소가 준비되면 이제 뭔가 해볼 수 있다.



### 기존 디렉토리를 Git 저장소로 만들기

버전관리를 하지 아니하는 기존 프로젝트를 Git으로 관리하고 싶은 경우 우선 프로젝트의 디렉토리로 이동한다.

Mac:

```sh
$ cd /Users/user/my_project
```

그리고 아래와 같은 명령을 실행한다:

```sh
$ git init
```

이 명령은 **`.git` 이라는 하위 디렉토리를 만든다.** `.git` 디렉토리에는 저장소에 필요한 뼈대 파일(Skeleton)이 들어 있다. 이 명령만으로는 아직 프로젝트의 어떤 파일도 관리하지 않는다. (`.git` 디렉토리가 막 만들어진 직후에 정확히 어떤 파일이 있는지에 대한 내용은 [Git의 내부](https://git-scm.com/book/ko/v2/ch00/ch10-git-internals)에서 다룬다)

Git이 파일을 관리하게 하려면 저장소에 파일을 추가하고 커밋해야 한다. `git add` 명령으로 파일을 추가하고 `git commit` 명령으로 커밋한다:

```sh
$ git add *.c
$ git add LICENSE
$ git commit -m 'initial project version'
```

명령어 몇 개로 순식간에 Git 저장소를 만들고 파일 버전 관리를 시작했다.



### 기존 저장소를 Clone 하기

다른 프로젝트에 참여하려거나(Contribute) Git 저장소를 복사하고 싶을 때 `git clone` 명령을 사용한다. 이미 Subversion 같은 VCS에 익숙한 사용자에게는 "checkout" 이 아니라 "clone" 이라는 점이 도드라져 보일 것이다. Git이 Subversion과 다른 가장 큰 차이점은 **서버에 있는 거의 모든 데이터를 복사한다는 것**이다. `git clone` 을 실행하면 프로젝트 히스토리를 전부 받아온다. 실제로 서버의 디스크가 망가져도 클라이언트 저장소 중에서 아무거나 하나 가져다가 복구하면 된다(서버에만 적용했던 설정은 복구하지 못하지만 **모든 데이터는 복구된다**).

`git clone <url>` 명령으로 저장소를 Clone 한다. `libgit2` 라이브러리 소스코드를 Clone 하려면 아래과 같이 실행한다.

```sh
$ git clone https://github.com/libgit2/libgit2
```

이 명령은 “libgit2” 라는 디렉토리를 만들고 그 안에 `.git` 디렉토리를 만든다. 그리고 저장소의 데이터를 모두 가져와서 **자동으로 가장 최신 버전을 Checkout** 해 놓는다. `libgit2` 디렉토리로 이동하면 Checkout으로 생성한 파일을 볼 수 있고 당장 하고자 하는 일을 시작할 수 있다.

아래과 같은 명령을 사용하여 저장소를 Clone 하면 `libgit2`이 아니라 다른 디렉토리 이름으로 Clone 할 수 있다.

```console
$ git clone https://github.com/libgit2/libgit2 mylibgit
```

디렉토리 이름이 `mylibgit` 이라는 것만 빼면 이 명령의 결과와 앞선 명령의 결과는 같다.

Git은 다양한 프로토콜을 지원한다. 이제까지는 `https://` 프로토콜을 사용했지만 `git://` 를 사용할 수도 있고 `user@server:path/to/repo.git` 처럼 SSH 프로토콜을 사용할 수도 있다. 자세한 내용은 [서버에 Git 설치하기](https://git-scm.com/book/ko/v2/ch00/_getting_git_on_a_server)에서 다루며 각 프로토콜의 장단점과 Git 저장소에 접근하는 방법을 설명한다.



## 2.2 수정하고 저장소에 저장하기

만질 수 있는 Git 저장소를 하나 만들었고 워킹 디렉토리에 Checkout도 했다. 이제는 파일을 수정하고 파일의 스냅샷을 커밋해 보자. **파일을 수정하다가 저장하고 싶으면 스냅샷을 커밋**한다.

워킹 디렉토리의 모든 파일은 크게 `Tracked(관리대상임)`와 `Untracked(관리대상이 아님)`로 나눈다. **`Tracked 파일`은 이미 스냅샷에 포함돼 있던 파일**이다. Tracked 파일은 또 Unmodified(수정하지 않음)와 Modified(수정함) 그리고 Staged(커밋으로 저장소에 기록할) 상태 중 하나이다. 간단히 말하자면 **Git이 알고 있는 파일**이라는 것이다.

그리고 나머지 파일은 모두 `Untracked 파일`이다. `Untracked 파일`은 워킹 디렉토리에 있는 파일 중 스냅샷에도 Staging Area에도 포함되지 않은 파일이다. **처음 저장소를 Clone 하면 모든 파일은 Tracked이면서 Unmodified 상태**이다. 파일을 Checkout 하고 나서 아무것도 수정하지 않았기 때문에 그렇다.

마지막 커밋 이후 아직 아무것도 수정하지 않은 상태에서 어떤 파일을 수정하면 Git은 그 파일을 **Modified** 상태로 인식한다. 실제로 커밋을 하기 위해서는 이 수정한 파일을 Staged 상태로 만들고, Staged 상태의 파일을 커밋한다. 이런 라이프사이클을 계속 반복한다.

![파일의 라이프사이클.](./images/lifecycle.png)

그림 8. 파일의 라이프사이클.



### 파일의 상태 확인하기

파일의 상태를 확인하려면 보통 `git status` 명령을 사용한다. Clone 한 후에 바로 이 명령을 실행하면 아래과 같은 메시지를 볼 수 있다.

```sh
$ git status
On branch master
Your branch is up-to-date with 'origin/master'.
nothing to commit, working directory clean
```

위의 내용은 파일을 하나도 수정하지 않았다는 것을 말해준다. T**racked 파일은 하나도 수정되지 않았다는 의미**다. Untracked 파일은 아직 없어서 목록에 나타나지 않는다. 그리고 현재 작업 중인 브랜치를 알려주며 서버의 같은 브랜치로부터 진행된 작업이 없는 것을 나타낸다. 기본 브랜치가 master이기 때문에 현재 브랜치 이름이 “master” 로 나온다. 브랜치 관련 내용은 차차 알아가자. [Git 브랜치](https://git-scm.com/book/ko/v2/ch00/ch03-git-branching) 에서 브랜치와 Refs에 대해 자세히 다룬다.

프로젝트에 `README` 파일을 만들어보자. `README` 파일은 새로 만든 파일이기 때문에 `git status` 를 실행하면 'Untracked files’에 들어 있다:

```sh
$ echo 'My Project' > README
$ git status
On branch master
Your branch is up-to-date with 'origin/master'.
Untracked files: // 새로 만든 파일이기 때문에 Untracked 상태
  (use "git add <file>..." to include in what will be committed)

    README

nothing added to commit but untracked files present (use "git add" to track)
```

`README` 파일은 “Untracked files” 부분에 속해 있는데 이것은 `README` 파일이 Untracked 상태라는 것을 말한다. Git은 Untracked 파일을 아직 스냅샷(커밋)에 넣어지지 않은 파일이라고 본다. **파일이 Tracked 상태가 되기 전까지는 Git은 절대 그 파일을 커밋하지 않는다 (정확히는 `Staged` 상태). ** 그래서 일하면서 생성하는 바이너리 파일 같은 것을 커밋하는 실수는 하지 않게 된다. `README` 파일을 추가해서 직접 Tracked 상태로 만들어 보자.



### 파일을 새로 추적하기

`git add` 명령으로 파일을 새로 추적할 수 있다. 아래 명령을 실행하면 Git은 `README` 파일을 추적한다.

```sh
$ git add README
```

`git status` 명령을 다시 실행하면 `README` 파일이 **Tracked 상태이면서 커밋에 추가될 Staged 상태**라는 것을 확인할 수 있다.

```sh
$ git status
On branch master
Your branch is up-to-date with 'origin/master'.
Changes to be committed: // Staged 상태라는 것을 의미
  (use "git reset HEAD <file>..." to unstage)

    new file:   README
```

“Changes to be committed” 에 들어 있는 파일은 **Staged 상태라는 것을 의미**한다. 커밋하면 `git add` 를 실행한 시점의 파일이 커밋되어 저장소 히스토리에 남는다. 앞에서 `git init` 명령을 실행한 후, `git add (files)` 명령을 실행했던 걸 기억할 것이다. 이 명령을 통해 디렉토리에 있는 파일을 추적하고 관리하도록 한다. `git add` 명령은 파일 또는 디렉토리의 경로를 아규먼트로 받는다. **디렉토리면 아래에 있는 모든 파일들까지 재귀적으로 추가한다.**



### Modified 상태의 파일을 Stage 하기

이미 Tracked 상태인 파일을 수정하는 법을 알아보자. `CONTRIBUTING.md` 라는 파일을 수정하고 나서 `git status` 명령을 다시 실행하면 결과는 아래와 같다.

```sh
$ git status
On branch master
Your branch is up-to-date with 'origin/master'.
Changes to be committed:
  (use "git reset HEAD <file>..." to unstage)

    new file:   README

Changes not staged for commit:
  (use "git add <file>..." to update what will be committed)
  (use "git checkout -- <file>..." to discard changes in working directory)

    modified:   CONTRIBUTING.md
```

이 `CONTRIBUTING.md` 파일은 “Changes not staged for commit” 에 있다. 이것은 **수정한 파일이 Tracked 상태이지만 아직 Staged 상태는 아니라는 것**이다. Staged 상태로 만들려면 `git add` 명령을 실행해야 한다. **`git add` 명령은 파일을 새로 추적할 때도 사용하고 수정한 파일을 Staged 상태로 만들 때도 사용**한다. Merge 할 때 충돌난 상태의 파일을 Resolve 상태로 만들때도 사용한다. **add의 의미는 프로젝트에 파일을 추가한다기 보다는 다음 커밋에 추가한다고 받아들이는게 좋다.** 

`git add` 명령을 실행하여 `CONTRIBUTING.md` 파일을 Staged 상태로 만들고 `git status` 명령으로 결과를 확인해보자.

```sh
$ git add CONTRIBUTING.md
$ git status
On branch master
Your branch is up-to-date with 'origin/master'.
Changes to be committed:
  (use "git reset HEAD <file>..." to unstage)

    new file:   README
    modified:   CONTRIBUTING.md
```

두 파일 모두 Staged 상태이므로 다음 커밋에 포함된다. 

하지만 아직 더 수정해야 한다는 것을 알게 되어 바로 커밋하지 못하는 상황이 되었다고 생각해보자. 이 상황에서 `CONTRIBUTING.md` 파일을 열고 수정한다. 이제 커밋할 준비가 다 됐다고 생각할 테지만, Git은 그렇지 않다. `git status` 명령으로 파일의 상태를 다시 확인해보자.

```sh
$ vim CONTRIBUTING.md
$ git status
On branch master
Your branch is up-to-date with 'origin/master'.
Changes to be committed:
  (use "git reset HEAD <file>..." to unstage)

    new file:   README
    modified:   CONTRIBUTING.md

Changes not staged for commit:
  (use "git add <file>..." to update what will be committed)
  (use "git checkout -- <file>..." to discard changes in working directory)

    modified:   CONTRIBUTING.md
```

**헉! `CONTRIBUTING.md` 가 Staged 상태이면서 *동시에* Unstaged 상태로 나온다.** 어떻게 이런 일이 가능할까? `git add` 명령을 실행하면 Git은 파일을 바로 Staged 상태로 만든다. 지금 이 시점에서 커밋을 하면 `git commit` 명령을 실행하는 시점의 버전이 커밋되는 것이 아니라 마지막으로 `git add` 명령을 실행했을 때의 버전이 커밋된다. 그러니까 `git add` 명령을 실행한 후에 또 파일을 수정하면 `git add` 명령을 다시 실행해서 최신 버전을 Staged 상태로 만들어야 한다.

```sh
$ git add CONTRIBUTING.md
$ git status
On branch master
Your branch is up-to-date with 'origin/master'.
Changes to be committed:
  (use "git reset HEAD <file>..." to unstage)

    new file:   README
    modified:   CONTRIBUTING.md
```



### 파일 상태를 짤막하게 확인하기

`git status` 명령으로 확인할 수 있는 내용이 좀 많아 보일 수 있다. 사실 그렇다. 좀 더 간단하게 변경 내용을 보여주는 옵션이 있다. `git status -s` 또는 `git status --short` 처럼 옵션을 주면 현재 변경한 상태를 짤막하게 보여준다.

```sh
$ git status -s
 M README
MM Rakefile
A  lib/git.rb
M  lib/simplegit.rb
?? LICENSE.txt
```

아직 추적하지 않는 새 파일 앞에는 `??` 표시가 붙는다. Staged 상태로 추가한 파일 중 새로 생성한 파일 앞에는 `A` 표시가, 수정한 파일 앞에는 `M` 표시가 붙는다. 위 명령의 결과에서 상태정보 컬럼에는 두 가지 정보를 보여준다. 왼쪽에는 Staging Area에서의 상태를, 오른쪽에는 Working Tree에서의 상태를 표시한다. `README` 파일 같은 경우 내용을 변경했지만 아직 Staged 상태로 추가하지는 않았다. `lib/simplegit.rb` 파일은 내용을 변경하고 Staged 상태로 추가까지 한 상태이다. 위 결과에서 차이점을 비교해보자. `Rakefile` 은 변경하고 Staged 상태로 추가한 후 또 내용을 변경해서 Staged 이면서 Unstaged 상태인 파일이다.



### 파일 무시하기

어떤 파일은 Git이 관리할 필요가 없다. 보통 로그 파일이나 빌드 시스템이 자동으로 생성한 파일이 그렇다. 그런 파일을 무시하려면 `.gitignore` 파일을 만들고 그 안에 무시할 파일 패턴을 적는다. 아래는 `.gitignore` 파일의 예이다.

```console
$ cat .gitignore
*.[oa]
*~
```

첫번째 라인은 확장자가 “.o” 나 “.a” 인 파일을 Git이 무시하라는 것이고 둘째 라인은 `~` 로 끝나는 모든 파일을 무시하라는 것이다. 보통 대부분의 텍스트 편집기에서 임시파일로 사용하는 파일 이름이기 때문이다. “.o” 와 “.a” 는 각각 빌드 시스템이 만들어내는 오브젝트와 아카이브 파일이고 `~` 로 끝나는 파일은 Emacs나 VI 같은 텍스트 편집기가 임시로 만들어내는 파일이다. 또 log, tmp, pid 같은 디렉토리나, 자동으로 생성하는 문서 같은 것들도 추가할 수 있다. `.gitignore` 파일은 보통 처음에 만들어 두는 것이 편리하다. 그래서 **Git 저장소에 커밋하고 싶지 않은 파일을 실수로 커밋하는 일을 방지**할 수 있다.

`.gitignore` 파일에 입력하는 패턴은 아래 규칙을 따른다.

- 아무것도 없는 라인이나, `#`로 시작하는 라인은 무시한다.
- 표준 Glob 패턴을 사용한다. 이는 프로젝트 전체에 적용된다.
- 슬래시(`/`)로 시작하면 하위 디렉토리에 적용되지(Recursivity) 않는다.
- 디렉토리는 슬래시(`/`)를 끝에 사용하는 것으로 표현한다.
- 느낌표(`!`)로 시작하는 패턴의 파일은 무시하지 않는다.

**Glob 패턴은 정규표현식을 단순하게 만든 것으로 생각하면 되고 보통 쉘에서 많이 사용**한다. 애스터리스크(`*`)는 문자가 하나도 없거나 하나 이상을 의미하고, `[abc]` 는 중괄호 안에 있는 문자 중 하나를 의미한다(그러니까 이 경우에는 a, b, c). 물음표(`?`)는 문자 하나를 말하고, `[0-9]` 처럼 중괄호 안의 캐릭터 사이에 하이픈(`-`)을 사용하면 그 캐릭터 사이에 있는 문자 하나를 말한다. 애스터리스크 2개를 사용하여 디렉토리 안의 디렉토리 까지 지정할 수 있다. `a/**/z` 패턴은 `a/z`, `a/b/z`, `a/b/c/z` 디렉토리에 사용할 수 있다.

아래는 `.gitignore` 파일의 예이다.

```sh
# 확장자가 .a인 파일 무시
*.a

# 윗 라인에서 확장자가 .a인 파일은 무시하게 했지만 lib.a는 무시하지 않음
!lib.a

# 현재 디렉토리에 있는 TODO파일은 무시하고 subdir/TODO처럼 하위디렉토리에 있는 파일은 무시하지 않음
/TODO

# build/ 디렉토리에 있는 모든 파일은 무시
build/

# doc/notes.txt 파일은 무시하고 doc/server/arch.txt 파일은 무시하지 않음
doc/*.txt

# doc 디렉토리 아래의 모든 .pdf 파일을 무시
doc/**/*.pdf
```

> ✏️ 힌트
>
> GitHub은 다양한 프로젝트에서 자주 사용하는 `.gitignore` 예제를 관리하고 있다. 어떤 내용을 넣을지 막막하다면 https://github.com/github/gitignore 사이트에서 적당한 예제를 찾을 수 있다.

> ⚠️ 주의
>
> `.gitignore`를 사용하는 간단한 방식은 하나의 `.gitignore` 파일을 최상위 디렉토리에 하나 두고 모든 하위 디렉토리에까지 적용시키는 방식이다. 물론 `.gitignore` 파일을 하나만 두는 것이 아니라 하위 디렉토리에도 추가로 둘 수도 있다. `.gitignore` 정책은 현재 `.gitignore` 파일이 위치한 디렉토리와 그 하위 디렉토리에 적용된다. (리눅스 커널 소스 저장소에는 `.gitignore` 파일이 206개나 있음)다수의 `.gitignore` 파일을 두고 정책을 적용하는 부분은 이 책에서 다루는 범위를 벗어난다. 자세한 내용은 `man gitignore`에서 확인할 수 있다.



### Staged와 Unstaged 상태의 변경 내용을 보기

단순히 파일이 변경됐다는 사실이 아니라 어떤 내용이 변경됐는지 살펴보려면 `git status` 명령이 아니라 `git diff` 명령을 사용해야 한다. 보통 우리는 **'수정했지만, 아직 Staged 파일이 아닌 것?'과 '어떤 파일이 Staged 상태인지?'**가 궁금하기 때문에 `git status` 명령으로도 충분하다. 더 자세하게 볼 때는 `git diff` 명령을 사용하는데 Patch처럼 어떤 라인을 추가했고 삭제했는지가 궁금할 때 사용한다. `git diff` 는 나중에 더 자세히 다룬다.

`README` 파일을 수정해서 Staged 상태로 만들고 `CONTRIBUTING.md` 파일은 그냥 수정만 해둔다. 이 상태에서 `git status` 명령을 실행하면 아래와 같은 메시지를 볼 수 있다.

```sh
$ git status
On branch master
Your branch is up-to-date with 'origin/master'.
Changes to be committed:
  (use "git reset HEAD <file>..." to unstage)

    modified:   README

Changes not staged for commit:
  (use "git add <file>..." to update what will be committed)
  (use "git checkout -- <file>..." to discard changes in working directory)

    modified:   CONTRIBUTING.md
```



`git diff` 명령을 실행하면 수정했지만 아직 staged 상태가 아닌 파일을 비교해 볼 수 있다.

```sh
$ git diff
diff --git a/CONTRIBUTING.md b/CONTRIBUTING.md
index 8ebb991..643e24f 100644
--- a/CONTRIBUTING.md
+++ b/CONTRIBUTING.md
@@ -65,7 +65,8 @@ branch directly, things can get messy.
 Please include a nice description of your changes when you submit your PR;
 if we have to read the whole diff to figure out why you're contributing
 in the first place, you're less likely to get feedback and have your change
-merged in.
+merged in. Also, split your changes into comprehensive chunks if your patch is
+longer than a dozen lines.

 If you are starting to work on a particular area, feel free to submit a PR
 that highlights your work in progress (and note in the PR title that it's
```

이 명령은 **워킹 디렉토리에 있는 것과 Staging Area에 있는 것을 비교**한다. 그래서 수정하고 아직 Stage 하지 않은 것을 보여준다.

만약 커밋하려고 Staging Area에 넣은 파일의 변경 부분을 보고 싶으면 `git diff --staged` 옵션을 사용한다. 이 명령은 저장소에 커밋한 것과 Staging Area에 있는 것을 비교한다.

```console
$ git diff --staged
diff --git a/README b/README
new file mode 100644
index 0000000..03902a1
--- /dev/null
+++ b/README
@@ -0,0 +1 @@
+My Project
```



꼭 잊지 말아야 할 것이 있는데 `git diff` 명령은 마지막으로 커밋한 후에 수정한 것들 전부를 보여주지 않는다. **`git diff` 는 Unstaged 상태인 것들만 보여준다.** 수정한 파일을 모두 Staging Area에 넣었다면 `git diff` 명령은 아무것도 출력하지 않는다.

`CONTRIBUTING.md` 파일을 Stage 한 후에 다시 수정해도 `git diff` 명령을 사용할 수 있다. 이때는 Staged 상태인 것과 Unstaged 상태인 것을 비교한다.

```sh
$ git add CONTRIBUTING.md
$ echo '# test line' >> CONTRIBUTING.md
$ git status
On branch master
Your branch is up-to-date with 'origin/master'.
Changes to be committed:
  (use "git reset HEAD <file>..." to unstage)

    modified:   CONTRIBUTING.md

Changes not staged for commit:
  (use "git add <file>..." to update what will be committed)
  (use "git checkout -- <file>..." to discard changes in working directory)

    modified:   CONTRIBUTING.md
```



`git diff` 명령으로 Unstaged 상태인 변경 부분을 확인할 수 있다.

```sh
$ git diff
diff --git a/CONTRIBUTING.md b/CONTRIBUTING.md
index 643e24f..87f08c8 100644
--- a/CONTRIBUTING.md
+++ b/CONTRIBUTING.md
@@ -119,3 +119,4 @@ at the
 ## Starter Projects

 See our [projects list](https://github.com/libgit2/libgit2/blob/development/PROJECTS.md).
+# test line
```



Staged 상태인 파일은 `git diff --cached` 옵션으로 확인한다. `--staged` 와 `--cached` 는 같은 옵션이다.

```sh
$ git diff --cached
diff --git a/CONTRIBUTING.md b/CONTRIBUTING.md
index 8ebb991..643e24f 100644
--- a/CONTRIBUTING.md
+++ b/CONTRIBUTING.md
@@ -65,7 +65,8 @@ branch directly, things can get messy.
 Please include a nice description of your changes when you submit your PR;
 if we have to read the whole diff to figure out why you're contributing
 in the first place, you're less likely to get feedback and have your change
-merged in.
+merged in. Also, split your changes into comprehensive chunks if your patch is
+longer than a dozen lines.

 If you are starting to work on a particular area, feel free to submit a PR
 that highlights your work in progress (and note in the PR title that it's
```

> ⚠️ 외부 도구로 비교하기
>
> 이 책에서는 계속 `git diff` 명령으로 여기저기서 써 먹는다. 즐겨 쓰거나 결과를 아름답게 보여주는 Diff 도구가 있으면 사용할 수 있다. `git diff` 대신 `git difftool` 명령을 사용해서 emerge, vimdiff 같은 도구로 비교할 수 있다. 상용 제품도 사용할 수 있다. `git difftool --tool-help` 라는 명령은 사용가능한 도구를 보여준다.



### 변경사항 커밋하기

수정한 것을 커밋하기 위해 Staging Area에 파일을 정리했다. **Unstaged 상태의 파일은 커밋되지 않는다는 것을 기억해야 한다.** Git은 생성하거나 수정하고 나서 `git add` 명령으로 추가하지 않은 파일은 커밋하지 않는다. 그 파일은 여전히 Modified 상태로 남아 있다. 커밋하기 전에 `git status` 명령으로 모든 것이 Staged 상태인지 확인할 수 있다. 그 후에 `git commit` 을 실행하여 커밋한다.

```sh
$ git commit
```

Git 설정에 지정된 편집기가 실행되고, 아래와 같은 텍스트가 자동으로 포함된다 (아래 예제는 Vim 편집기의 화면이다. 이 편집기는 쉘의 `EDITOR` 환경 변수에 등록된 편집기이고 보통은 Vim이나 Emacs을 사용한다. 또 [시작하기](https://git-scm.com/book/ko/v2/ch00/ch01-getting-started) 에서 설명했듯이 `git config --global core.editor` 명령으로 어떤 편집기를 사용할지 설정할 수 있다).

편집기는 아래와 같은 내용을 표시한다(아래 예제는 Vim 편집기).

```sh
# Please enter the commit message for your changes. Lines starting
# with '#' will be ignored, and an empty message aborts the commit.
# On branch master
# Your branch is up-to-date with 'origin/master'.
#
# Changes to be committed:
#	new file:   README
#	modified:   CONTRIBUTING.md
#
~
~
~
".git/COMMIT_EDITMSG" 9L, 283C
```

자동으로 생성되는 커밋 메시지의 첫 라인은 비어 있고 둘째 라인부터 `git status` 명령의 결과가 채워진다. 커밋한 내용을 쉽게 기억할 수 있도록 이 메시지를 포함할 수도 있고 메시지를 전부 지우고 새로 작성할 수 있다 (정확히 뭘 수정했는지도 보여줄 수 있는데, `git commit` 에 -v 옵션을 추가하면 편집기에 diff 메시지도 추가된다). 내용을 저장하고 편집기를 종료하면 Git은 입력된 내용(#로 시작하는 내용을 제외한)으로 새 커밋을 하나 완성한다.

메시지를 인라인으로 첨부할 수도 있다. `commit` 명령을 실행할 때 아래와 같이 **`-m` 옵션을 사용**한다.

```sh
$ git commit -m "Story 182: Fix benchmarks for speed"
[master 463dc4f] Story 182: Fix benchmarks for speed
 2 files changed, 2 insertions(+)
 create mode 100644 README
```

이렇게 첫번째 커밋을 작성해보았다. `commit` 명령은 몇 가지 정보를 출력하는데 위 예제는 (`master`) 브랜치에 커밋했고 체크섬은 (`463dc4f`)이라고 알려준다. 그리고 수정한 파일이 몇 개이고 삭제됐거나 추가된 라인이 몇 라인인지 알려준다.

**Git은 Staging Area에 속한 스냅샷을 커밋한다는 것을 기억**해야 한다. 수정은 했지만, 아직 Staging Area에 넣지 않은 것은 다음에 커밋할 수 있다. 커밋할 때마다 프로젝트의 스냅샷을 기록하기 때문에 나중에 스냅샷끼리 비교하거나 예전 스냅샷으로 되돌릴 수 있다.



### Staging Area 생략하기

Staging Area는 커밋할 파일을 정리한다는 점에서 매우 유용하지만 복잡하기만 하고 필요하지 않은 때도 있다. 아주 쉽게 Staging Area를 생략할 수 있다. `git commit` 명령을 실행할 때 `-a` 옵션을 추가하면 Git은 Tracked 상태의 파일을 자동으로 Staging Area에 넣는다. 그래서 `git add` 명령을 실행하는 수고를 덜 수 있다.

```sh
$ git status
On branch master
Your branch is up-to-date with 'origin/master'.
Changes not staged for commit:
  (use "git add <file>..." to update what will be committed)
  (use "git checkout -- <file>..." to discard changes in working directory)

    modified:   CONTRIBUTING.md

no changes added to commit (use "git add" and/or "git commit -a")
$ git commit -a -m 'added new benchmarks'
[master 83e38c7] added new benchmarks
 1 file changed, 5 insertions(+), 0 deletions(-)
```

이 예제에서는 커밋하기 전에 `git add` 명령으로 `CONTRIBUTING.md` 파일을 추가하지 않았다는 점을 눈여겨보자. `-a` 옵션을 사용하면 모든 파일이 자동으로 추가된다. **편리한 옵션이긴 하지만 주의 깊게 사용해야 한다.** 생각 없이 이 옵션을 사용하다 보면 추가하지 말아야 할 변경사항도 추가될 수 있기 때문이다.



### 파일 삭제하기

Git에서 파일을 제거하려면 `git rm` 명령으로 Tracked 상태의 파일을 삭제한 후에(정확하게는 Staging Area에서 삭제하는 것) 커밋해야 한다. 이 명령은 워킹 디렉토리에 있는 파일도 삭제하기 때문에 실제로 파일도 지워진다.

Git 명령을 사용하지 않고 단순히 워킹 디렉터리에서 파일을 삭제하고 `git status` 명령으로 상태를 확인하면 Git은 현재 “Changes not staged for commit” (즉, *Unstaged* 상태)라고 표시해준다.

```sh
$ rm PROJECTS.md
$ git status
On branch master
Your branch is up-to-date with 'origin/master'.
Changes not staged for commit:
  (use "git add/rm <file>..." to update what will be committed)
  (use "git checkout -- <file>..." to discard changes in working directory)

        deleted:    PROJECTS.md

no changes added to commit (use "git add" and/or "git commit -a")
```

그리고 `**git rm` 명령을 실행하면 삭제한 파일은 Staged 상태가 된다.**

```sh
$ git rm PROJECTS.md
rm 'PROJECTS.md'
$ git status
On branch master
Your branch is up-to-date with 'origin/master'.
Changes to be committed:
  (use "git reset HEAD <file>..." to unstage)

    deleted:    PROJECTS.md
```

커밋하면 파일은 삭제되고 Git은 이 파일을 더는 추적하지 않는다. 이미 파일을 수정했거나 Staging Area에(역주 - Git Index라고도 부른다) 추가했다면 `-f` 옵션을 주어 강제로 삭제해야 한다. 이 점은 실수로 데이터를 삭제하지 못하도록 하는 안전장치다. **커밋 하지 않고 수정한 데이터는 Git으로 복구할 수 없기 때문이다.**

또 Staging Area에서만 제거하고 워킹 디렉토리에 있는 파일은 지우지 않고 남겨둘 수 있다. 다시 말해서 하드디스크에 있는 파일은 그대로 두고 Git만 추적하지 않게 한다. 이것은 `.gitignore` 파일에 추가하는 것을 빼먹었거나 대용량 로그 파일이나 컴파일된 파일인 `.a` 파일 같은 것을 실수로 추가했을 때 쓴다. **`--cached` 옵션을 사용하여 명령을 실행한다.**

```sh
$ git rm --cached README
```

여러 개의 파일이나 디렉토리를 한꺼번에 삭제할 수도 있다. 아래와 같이 `git rm` 명령에 file-glob 패턴을 사용한다.

```sh
$ git rm log/\*.log
```

`*` 앞에 `\` 을 사용한 것을 기억하자. 파일명 확장 기능은 쉘에만 있는 것이 아니라 Git 자체에도 있기 때문에 필요하다. 이 명령은 `log/` 디렉토리에 있는 `.log` 파일을 모두 삭제한다. 아래의 예제처럼 할 수도 있다.

```sh
$ git rm \*~
```

이 명령은 `~` 로 끝나는 파일을 모두 삭제한다.



### 파일 이름 변경하기

Git은 다른 VCS 시스템과는 달리 **파일 이름의 변경이나 파일의 이동을 명시적으로 관리하지 않는다.** 다시 말해서 파일 이름이 변경됐다는 별도의 정보를 저장하지 않는다. Git은 똑똑해서 굳이 파일 이름이 변경되었다는 것을 추적하지 않아도 아는 방법이 있다. 파일의 이름이 변경된 것을 Git이 어떻게 알아내는지 살펴보자.

이렇게 말하고 Git에 `mv` 명령이 있는 게 좀 이상하겠지만, 아래와 같이 파일 이름을 변경할 수 있다.

```sh
$ git mv file_from file_to
```

잘 동작한다. 이 명령을 실행하고 Git의 상태를 확인해보면 Git은 이름이 바뀐 사실을 알고 있다.

```sh
$ git mv README.md README
$ git status
On branch master
Your branch is up-to-date with 'origin/master'.
Changes to be committed:
  (use "git reset HEAD <file>..." to unstage)

    renamed:    README.md -> README
```

사실 `git mv` 명령은 아래 명령어를 수행한 것과 완전 똑같다.

```sh
$ mv README.md README
$ git rm README.md
$ git add README
```

`**git mv` 명령은 일종의 단축 명령어**이다. 이 명령으로 파일 이름을 바꿔도 되고 `mv` 명령으로 파일 이름을 직접 바꿔도 된다. 단지 `git mv` 명령은 편리하게 명령을 세 번 실행해주는 것 뿐이다. 어떤 도구로 이름을 바꿔도 상관없다. 중요한 것은 이름을 변경하고 나서 꼭 rm/add 명령을 실행해야 한다는 것 뿐이다.



## 2.3 커밋 히스토리 조회하기

새로 저장소를 만들어서 몇 번 커밋을 했을 수도 있고, 커밋 히스토리가 있는 저장소를 Clone 했을 수도 있다. 어쨌든 가끔 **저장소의 히스토리**를 보고 싶을 때가 있다. Git에는 히스토리를 조회하는 명령어인 `git log` 가 있다.

이 예제에서는 “simplegit” 이라는 매우 단순한 프로젝트를 사용한다. 아래와 같이 이 프로젝트를 Clone 한다.

```sh
$ git clone https://github.com/schacon/simplegit-progit
```


이 프로젝트 디렉토리에서 `git log` 명령을 실행하면 아래와 같이 출력된다.

```sh
$ git log
commit ca82a6dff817ec66f44342007202690a93763949 // SHA-1 체크섬
Author: Scott Chacon <schacon@gee-mail.com>	    // 저자 정보
Date:   Mon Mar 17 21:52:11 2008 -0700     	    // 커밋 날짜

    changed the version number									// 커밋 메시지

commit 085bb3bcb608e1e8451d4b2432f8ecbe6306e7e7
Author: Scott Chacon <schacon@gee-mail.com>
Date:   Sat Mar 15 16:40:33 2008 -0700

    removed unnecessary test

commit a11bef06a3f659402fe7563abf99ad00de2209e6
Author: Scott Chacon <schacon@gee-mail.com>
Date:   Sat Mar 15 10:31:28 2008 -0700

    first commit
```

특별한 아규먼트 없이 `git log` 명령을 실행하면 저장소의 커밋 히스토리를 시간순으로 보여준다. 즉, 가장 최근의 커밋이 가장 먼저 나온다. 그리고 이어서 각 커밋의 SHA-1 체크섬, 저자 이름, 저자 이메일, 커밋한 날짜, 커밋 메시지를 보여준다.

원하는 히스토리를 검색할 수 있도록 `git log` 명령은 매우 다양한 옵션을 지원한다. 여기에서는 자주 사용하는 옵션을 설명한다.

여러 옵션 중 `-p`, `--patch` 는 굉장히 유용한 옵션이다. **`-p` 는 각 커밋의 diff 결**과를 보여준다. 다른 유용한 옵션으로 `-2`가 있는데 최근 두 개의 결과만 보여주는 옵션이다:

```sh
$ git log -p -2
commit ca82a6dff817ec66f44342007202690a93763949
Author: Scott Chacon <schacon@gee-mail.com>
Date:   Mon Mar 17 21:52:11 2008 -0700

    changed the version number

diff --git a/Rakefile b/Rakefile
index a874b73..8f94139 100644
--- a/Rakefile
+++ b/Rakefile
@@ -5,7 +5,7 @@ require 'rake/gempackagetask'
 spec = Gem::Specification.new do |s|
     s.platform  =   Gem::Platform::RUBY
     s.name      =   "simplegit"
-    s.version   =   "0.1.0"
+    s.version   =   "0.1.1"
     s.author    =   "Scott Chacon"
     s.email     =   "schacon@gee-mail.com"
     s.summary   =   "A simple gem for using Git in Ruby code."

commit 085bb3bcb608e1e8451d4b2432f8ecbe6306e7e7
Author: Scott Chacon <schacon@gee-mail.com>
Date:   Sat Mar 15 16:40:33 2008 -0700

    removed unnecessary test

diff --git a/lib/simplegit.rb b/lib/simplegit.rb
index a0a60ae..47c6340 100644
--- a/lib/simplegit.rb
+++ b/lib/simplegit.rb
@@ -18,8 +18,3 @@ class SimpleGit
     end

 end
-
-if $0 == __FILE__
-  git = SimpleGit.new
-  puts git.show
-end
```

이 옵션은 직접 diff를 실행한 것과 같은 결과를 출력하기 때문에 동료가 무엇을 커밋했는지 리뷰하고 빨리 조회하는데 유용하다. 또 `git log` 명령에는 히스토리의 통계를 보여주는 옵션도 있다. `--stat` 옵션으로 각 커밋의 통계 정보를 조회할 수 있다.

```sh
$ git log --stat
commit ca82a6dff817ec66f44342007202690a93763949
Author: Scott Chacon <schacon@gee-mail.com>
Date:   Mon Mar 17 21:52:11 2008 -0700

    changed the version number

 Rakefile | 2 +-
 1 file changed, 1 insertion(+), 1 deletion(-)

commit 085bb3bcb608e1e8451d4b2432f8ecbe6306e7e7
Author: Scott Chacon <schacon@gee-mail.com>
Date:   Sat Mar 15 16:40:33 2008 -0700

    removed unnecessary test

 lib/simplegit.rb | 5 -----
 1 file changed, 5 deletions(-)

commit a11bef06a3f659402fe7563abf99ad00de2209e6
Author: Scott Chacon <schacon@gee-mail.com>
Date:   Sat Mar 15 10:31:28 2008 -0700

    first commit

 README           |  6 ++++++
 Rakefile         | 23 +++++++++++++++++++++++
 lib/simplegit.rb | 25 +++++++++++++++++++++++++
 3 files changed, 54 insertions(+)
```

이 결과에서 `--stat` 옵션은 어떤 파일이 수정됐는지, 얼마나 많은 파일이 변경됐는지, 또 얼마나 많은 라인을 추가하거나 삭제했는지 보여준다. 요약정보는 가장 뒤쪽에 보여준다.

다른 또 유용한 옵션은 `--pretty` 옵션이다. 이 옵션을 통해 히스토리 내용을 보여줄 때 기본 형식 이외에 여러 가지 중에 하나를 선택할 수 있다. 몇개 선택할 수 있는 옵션의 값이 있다. `oneline` 옵션은 각 커밋을 한 라인으로 보여준다. 이 옵션은 많은 커밋을 한 번에 조회할 때 유용하다. 추가로 `short`, `full`, `fuller` 옵션도 있는데 이것은 정보를 조금씩 가감해서 보여준다.

```console
$ git log --pretty=oneline
ca82a6dff817ec66f44342007202690a93763949 changed the version number
085bb3bcb608e1e8451d4b2432f8ecbe6306e7e7 removed unnecessary test
a11bef06a3f659402fe7563abf99ad00de2209e6 first commit
```

가장 재밌는 옵션은 `format` 옵션이다. 나만의 포맷으로 결과를 출력하고 싶을 때 사용한다. 특히 결과를 다른 프로그램으로 파싱하고자 할 때 유용하다. 이 옵션을 사용하면 포맷을 정확하게 일치시킬 수 있기 때문에 Git을 새 버전으로 바꿔도 결과 포맷이 바뀌지 않는다.

```sh
$ git log --pretty=format:"%h - %an, %ar : %s"
ca82a6d - Scott Chacon, 6 years ago : changed the version number
085bb3b - Scott Chacon, 6 years ago : removed unnecessary test
a11bef0 - Scott Chacon, 6 years ago : first commit
```


[git log --pretty=format` 에 쓸 몇가지 유용한 옵션](https://git-scm.com/book/ko/v2/ch00/pretty_format) 은 다음과 같다.

| 옵션  | 설명                                |
| :---- | :---------------------------------- |
| `%H`  | 커밋 해시                           |
| `%h`  | 짧은 길이 커밋 해시                 |
| `%T`  | 트리 해시                           |
| `%t`  | 짧은 길이 트리 해시                 |
| `%P`  | 부모 해시                           |
| `%p`  | 짧은 길이 부모 해시                 |
| `%an` | 저자 이름                           |
| `%ae` | 저자 메일                           |
| `%ad` | 저자 시각 (형식은 –-date=옵션 참고) |
| `%ar` | 저자 상대적 시각                    |
| `%cn` | 커미터 이름                         |
| `%ce` | 커미터 메일                         |
| `%cd` | 커미터 시각                         |
| `%cr` | 커미터 상대적 시각                  |
| `%s`  | 요약                                |

*저자(Author)* 와 *커미터(Committer)* 를 구분하는 것이 조금 이상해 보일 수 있다. 저자는 원래 작업을 수행한 원작자이고 커밋터는 마지막으로 이 작업을 적용한(저장소에 포함시킨) 사람이다. 만약 당신이 어떤 프로젝트에 패치를 보냈고 그 프로젝트의 담당자가 패치를 적용했다면 두 명의 정보를 모두 알 필요가 있다. 그래서 이 경우 당신이 저자고 그 담당자가 커미터다. [분산 환경에서의 Git](https://git-scm.com/book/ko/v2/ch00/ch05-distributed-git) 에서 이 주제에 대해 자세히 다룰 것이다.

`oneline` 옵션과 `format` 옵션은 `--graph` 옵션과 함께 사용할 때 더 빛난다. 이 명령은 브랜치와 머지 히스토리를 보여주는 아스키 그래프를 출력한다.

```sh
$ git log --pretty=format:"%h %s" --graph
* 2d3acf9 ignore errors from SIGCHLD on trap
*  5e3ee11 Merge branch 'master' of git://github.com/dustin/grit
|\
| * 420eac9 Added a method for getting the current branch.
* | 30e367c timeout code and tests
* | 5a09431 add timeout protection to grit
* | e1193f8 support for heads with slashes in them
|/
* d6016bc require time for xmlschema
*  11d191e Merge branch 'defunkt' into local
```

다음 장에서 살펴볼 브랜치나 Merge 결과의 히스토리를 이런 식으로 살펴보면 훨씬 흥미롭다.

`git log` 명령의 기본적인 옵션과 출력물의 형식에 관련된 옵션을 살펴보았다. `git log` 명령은 앞서 살펴본 것보다 더 많은 옵션을 지원한다. [`git log` 주요 옵션](https://git-scm.com/book/ko/v2/ch00/log_options) 는 지금 설명한 것과 함께 유용하게 사용할 수 있는 옵션이다. 각 옵션으로 어떻게 `log` 명령을 제어할 수 있는지 보여준다.

| 옵션              | 설명                                                         |
| :---------------- | :----------------------------------------------------------- |
| `-p`              | 각 커밋에 적용된 **패치**(diff 결과)를 보여준다.             |
| `--stat`          | 각 커밋에서 수정된 파일의 통계정보를 보여준다.               |
| `--shortstat`     | `--stat` 명령의 결과 중에서 수정한 파일, 추가된 라인, 삭제된 라인만 보여준다. |
| `--name-only`     | 커밋 정보중에서 수정된 파일의 목록만 보여준다.               |
| `--name-status`   | 수정된 파일의 목록을 보여줄 뿐만 아니라 파일을 추가한 것인지, 수정한 것인지, 삭제한 것인지도 보여준다. |
| `--abbrev-commit` | 40자 짜리 SHA-1 체크섬을 전부 보여주는 것이 아니라 처음 몇 자만 보여준다. |
| `--relative-date` | 정확한 시간을 보여주는 것이 아니라 “2 weeks ago” 처럼 상대적인 형식으로 보여준다. |
| `--graph`         | 브랜치와 머지 히스토리 정보까지 아스키 그래프로 보여준다.    |
| `--pretty`        | 지정한 형식으로 보여준다. 이 옵션에는 oneline, short, full, fuller, format이 있다. format은 원하는 형식으로 출력하고자 할 때 사용한다. |
| `--oneline`       | `--pretty=oneline --abbrev-commit` 두 옵션을 함께 사용한 것과 같다. |



### 조회 제한조건

출력 형식과 관련된 옵션을 살펴봤지만 `git log` 명령은 조회 범위를 제한하는 옵션들도 있다. 히스토리 전부가 아니라 부분만 조회한다. 이미 최근 두 개만 조회하는 `-2` 옵션은 살펴봤다. 실제 사용법은 `-<n>`이고 n은 최근 n개의 커밋을 의미한다. 사실 이 옵션을 자주 쓰진 않는다. **Git은 기본적으로 출력을 pager류의 프로그램을 거쳐서 내보내므로 한 번에 한 페이지씩 보여준다.**

반면 `--since` 나 `--until` 같은 **시간을 기준으로 조회**하는 옵션은 매우 유용하다. 지난 2주 동안 만들어진 커밋들만 조회하는 명령은 아래와 같다.

```sh
$ git log --since=2.weeks
```

이 옵션은 다양한 형식을 지원한다.`"2008-01-15"` 같이 정확한 날짜도 사용할 수 있고 `"2 years 1 day 3 minutes ago"` 같이 상대적인 기간을 사용할 수도 있다.

또 다른 기준도 있다. `--author` 옵션으로 저자를 지정하여 검색할 수도 있고 `--grep` 옵션으로 커밋 메시지에서 키워드를 검색할 수도 있다.

> ⚠️ 주의
>
> `--author`와 `--grep` 옵션을 함께 사용하여 모두 만족하는 커밋을 찾으려면 `--all-match` 옵션도 반드시 함께 사용해야 한다.

진짜 유용한 옵션으로 `-S` 가 있는데 이 옵션은 **코드에서 추가되거나 제거된 내용 중에 특정 텍스트가 포함되어 있는지를 검색**한다. 예를 들어 어떤 함수가 추가되거나 제거된 커밋만을 찾아보려면 아래와 같은 명령을 사용한다.

```sh
$ git log -S function_name
```


마지막으로 파일 경로로 검색하는 옵션이 있는데 이것도 정말 유용하다. 디렉토리나 파일 이름을 사용하여 그 파일이 변경된 log의 결과를 검색할 수 있다. 이 옵션은 `--` 와 함께 경로 이름을 사용하는데 명령어 끝 부분에 쓴다(역주 - `git log -- path1 path2`).

[`git log` 조회 범위를 제한하는 옵션](https://git-scm.com/book/ko/v2/ch00/limit_options) 은 조회 범위를 제한하는 옵션들이다.

| 옵션                  | 설명                                              |
| :-------------------- | :------------------------------------------------ |
| `-(n)`                | 최근 n 개의 커밋만 조회한다.                      |
| `--since`, `--after`  | 명시한 날짜 이후의 커밋만 검색한다.               |
| `--until`, `--before` | 명시한 날짜 이전의 커밋만 조회한다.               |
| `--author`            | 입력한 저자의 커밋만 보여준다.                    |
| `--committer`         | 입력한 커미터의 커밋만 보여준다.                  |
| `--grep`              | 커밋 메시지 안의 텍스트를 검색한다.               |
| `-S`                  | 커밋 변경(추가/삭제) 내용 안의 텍스트를 검색한다. |

이제 살펴볼 예제는 Merge 커밋을 제외한 순수한 커밋을 확인해보는 명령이다. Junio Hamano가 2008년 10월에 Git 소스코드 저장소에서 테스트 파일을 수정한 커밋들이다.

```sh
$ git log --pretty="%h - %s" --author=gitster --since="2008-10-01" \
   --before="2008-11-01" --no-merges -- t/
5610e3b - Fix testcase failure when extended attributes are in use
acd3b9e - Enhance hold_lock_file_for_{update,append}() API
f563754 - demonstrate breakage of detached checkout with symbolic link HEAD
d1a43f2 - reset --hard/read-tree --reset -u: remove unmerged new paths
51a94af - Fix "checkout --track -b newbranch" on detached HEAD
b0ad11e - pull: allow "git pull origin $something:$current_branch" into an unborn branch
```

총 4만여 개의 커밋 히스토리에서 이 명령의 검색 조건에 만족하는 것은 단 6개였다.

> ✏️ 머지 커밋 표시하지 않기
>
> 저장소를 사용하는 워크플로우에 따라 머지 커밋이 차지하는 비중이 클 수도 있다. `--no-merges` 옵션을 사용하면 검색 결과에서 머지 커밋을 표시하지 않도록 할 수 있다.



## 2.4 되돌리기

일을 하다보면 모든 단계에서 어떤 것은 되돌리고(Undo) 싶을 때가 있다. 이번에는 우리가 한 일을 되돌리는 방법을 살펴본다. 한 번 되돌리면 복구할 수 없기에 주의해야 한다. **Git을 사용하면 우리가 저지른 실수는 대부분 복구할 수 있지만 되돌린 것은 복구할 수 없다.**


### 이전 커밋 내용 수정

*이전의 커밋을 완전히 새로 고쳐서 새 커밋으로 변경하는 것*

종종 완료한 커밋을 수정해야 할 때가 있다. 너무 일찍 커밋했거나 어떤 파일을 빼먹었을 때 그리고 커밋 메시지를 잘못 적었을 때 한다. 다시 커밋하고 싶으면 파일 수정 작업을 하고 Staging Area에 추가한 다음 `--amend` 옵션을 사용하여 커밋을 재작성 할 수 있다.

```sh
$ git commit --amend
```

이 명령은 Staging Area를 사용하여 커밋한다. 만약 마지막으로 커밋하고 나서 수정한 것이 없다면(커밋하자마자 바로 이 명령을 실행하는 경우) 조금 전에 한 커밋과 모든 것이 같다. 이때는 커밋 메시지만 수정한다.

편집기가 실행되면 이전 커밋 메시지가 자동으로 포함된다. **메시지를 수정하지 않고 그대로 커밋해도 기존의 커밋을 덮어쓴다.**

커밋을 했는데 Stage 하는 것을 깜빡하고 빠트린 파일이 있으면 아래와 같이 고칠 수 있다.

```sh
$ git commit -m 'initial commit'
$ git add forgotten_file
$ git commit --amend
```

여기서 실행한 명령어 3개는 모두 커밋 한 개로 기록된다. 두 번째 커밋은 첫 번째 커밋을 덮어쓴다.

> ⚠️ 이렇게 `--amend` 옵션으로 커밋을 고치는 작업은, 추가로 작업한 일이 작다고 하더라도 **이전의 커밋을 완전히 새로 고쳐서 새 커밋으로 변경하는 것**을 의미한다. 이전의 커밋은 일어나지 않은 일이 되는 것이고 당연히 히스토리에도 남지 않는다.`--amend` 옵션으로 커밋을 고치는 작업이 주는 장점은 마지막 커밋 작업에서 아주 살짝 뭔가 빠뜨린 것을 넣거나 변경하는 것을 새 커밋으로 분리하지 않고 하나의 커밋에서 처리하는 것이다. **“앗차, 빠진 파일 넣었음”, “이전 커밋에서 오타 살짝 고침” 등의 커밋을 만들지 않겠다는 말**이다.



### 파일 상태를 Unstage로 변경하기

*Staged Area에서 워킹 디렉토리로 변경*

다음은 Staging Area와 워킹 디렉토리 사이를 넘나드는 방법을 설명한다. 두 영역의 상태를 확인할 때마다 변경된 상태를 되돌리는 방법을 알려주기 때문에 매우 편리하다. 예를 들어 파일을 두 개 수정하고서 따로따로 커밋하려고 했지만, 실수로 `git add *` 라고 실행해 버렸다. 두 파일 모두 Staging Area에 들어 있다. 이제 둘 중 하나를 어떻게 꺼낼까? 우선 `git status` 명령으로 확인해보자.

```sh
$ git add *
$ git status
On branch master
Changes to be committed:
  (use "git reset HEAD <file>..." to unstage)

    renamed:    README.md -> README
    modified:   CONTRIBUTING.md
```

`Changes to be commited` 밑에 `git reset HEAD <file>...` 메시지가 보인다. 이 명령으로 Unstaged 상태로 변경할 수 있다. `CONTRIBUTING.md` 파일을 Unstaged 상태로 변경해보자.

```sh
$ git reset HEAD CONTRIBUTING.md
Unstaged changes after reset:
M	CONTRIBUTING.md
$ git status
On branch master
Changes to be committed:
  (use "git reset HEAD <file>..." to unstage)

    renamed:    README.md -> README

Changes not staged for commit:
  (use "git add <file>..." to update what will be committed)
  (use "git checkout -- <file>..." to discard changes in working directory)

    modified:   CONTRIBUTING.md
```

명령어가 낮설게 느껴질 수도 있지만 잘 동작한다. `CONTRIBUTING.md` 파일은 Unstaged 상태가 됐다.

> ⚠️ 주의
> `git reset` 명령은 매우 위험하다. **`--hard` 옵션과 함께 사용하면 더욱 위험**하다. 하지만 위에서 처럼 옵션 없이 사용하면 워킹 디렉토리의 파일은 건드리지 않는다.

지금까지 살펴본 내용이 `git reset` 명령에 대해 알아야 할 대부분의 내용이다. `reset` 명령이 정확히는 어떻게 동작하는지, 어떻게 전문적으로 활용하는지는 [Reset 명확히 알고 가기](https://git-scm.com/book/ko/v2/ch00/_git_reset) 부분에서 자세히 살펴보기로 한다.



### Modified 파일 되돌리기

어떻게 해야 CONTRIBUTING.md 파일을 수정하고 나서 다시 되돌릴 수 있을까? 그러니까 최근 커밋된 버전으로(아니면 처음 Clone 했을 때처럼 워킹 디렉토리에 처음 Checkout 한 그 내용으로) 되돌리는 방법이 무얼까? `git status` 명령이 친절하게 알려준다. 바로 위에 있는 예제에서 Unstaged 부분을 보자.

```sh
Changes not staged for commit:
  (use "git add <file>..." to update what will be committed)
  (use "git checkout -- <file>..." to discard changes in working directory)

    modified:   CONTRIBUTING.md
```

위의 메시지는 수정한 파일을 되돌리는 방법을 꽤 정확하게 알려준다. 알려주는 대로 한 번 해보자.

```sh
$ git checkout -- CONTRIBUTING.md
$ git status
On branch master
Changes to be committed:
  (use "git reset HEAD <file>..." to unstage)

    renamed:    README.md -> README
```

정상적으로 복원된 것을 알 수 있다.

> ⭐️ 중요
>
> `git checkout -- [file]` 명령은 **꽤 위험한 명령**이라는 것을 알아야 한다. 원래 파일로 덮어썼기 때문에 수정한 내용은 전부 사라진다. 수정한 내용이 진짜 마음에 들지 않을 때만 사용하자.

**변경한 내용을 쉽게 버릴수는 없고 하지만 당장은 되돌려야만 하는 상황이라면 Stash와 Branch를 사용하자.** [Git 브랜치](https://git-scm.com/book/ko/v2/ch00/ch03-git-branching) 에서 다루는 이 방법들이 훨씬 낫다.

**Git으로 *커밋* 한 모든 것은 언제나 복구할 수 있다.** 삭제한 브랜치에 있었던 것도, `--amend` 옵션으로 다시 커밋한 것도 복구할 수 있다(자세한 것은 [데이터 복구](https://git-scm.com/book/ko/v2/ch00/_data_recovery) 에서 다룬다). **하지만 커밋하지 않고 잃어버린 것은 절대로 되돌릴 수 없다.**



## 2.5 리모트 저장소

리모트 저장소를 관리할 줄 알아야 다른 사람과 함께 일할 수 있다. 리모트 저장소는 인터넷이나 네트워크 어딘가에 있는 저장소를 말한다. 저장소는 여러 개가 있을 수 있는데 어떤 저장소는 읽고 쓰기 모두 할 수 있고 어떤 저장소는 읽기만 가능할 수 있다. 간단히 말해서 다른 사람들과 함께 일한다는 것은 리모트 저장소를 관리하면서 데이터를 거기에 Push 하고 Pull 하는 것이다. **리모트 저장소를 관리한다는 것은 저장소를 추가, 삭제하는 것뿐만 아니라 브랜치를 관리하고 추적할지 말지 등을 관리**하는 것을 말한다. 이번에는 리모트 저장소를 관리하는 방법에 대해 설명한다.

> ⚠️ 주의
>
> 원격 저장소라 하더라도 로컬 시스템에 위치할 수도 있다.“remote” 저장소라고 이름이 붙어있어도 이 원격 저장소가 사실 같은 로컬 시스템에 존재할 수도 있다. 여기서 “remote” 라는 이름은 반드시 저장소가 네트워크나 인터넷을 통해 어딘가 멀리 떨어져 있어야만 한다는 것을 의미하지 않는다. 물론 일반적인 원격 저장소와 마찬가지로 Push, Pull 등의 기능은 동일하게 사용한다.



### 리모트 저장소 확인하기

`git remote` 명령으로 현재 프로젝트에 등록된 리모트 저장소를 확인할 수 있다. 이 명령은 리모트 저장소의 단축 이름을 보여준다. 저장소를 **Clone 하면 `origin`이라는 리모트 저장소가 자동으로 등록되기 때문에 `origin`이라는 이름을 볼 수 있다.**

```sh
$ git clone https://github.com/schacon/ticgit
Cloning into 'ticgit'...
remote: Reusing existing pack: 1857, done.
remote: Total 1857 (delta 0), reused 0 (delta 0)
Receiving objects: 100% (1857/1857), 374.35 KiB | 268.00 KiB/s, done.
Resolving deltas: 100% (772/772), done.
Checking connectivity... done.

$ cd ticgit

$ git remote
origin
```


`-v` 옵션을 주어 단축이름과 URL을 함께 볼 수 있다.

```sh
$ git remote -v
origin	https://github.com/schacon/ticgit (fetch)
origin	https://github.com/schacon/ticgit (push)
```


리모트 저장소가 여러 개 있다면 이 명령은 등록된 전부를 보여준다. 여러 사람과 함께 작업하는 리모트 저장소가 여러개라면 아래와 같은 결과를 얻을 수도 있다.

```sh
$ cd grit
$ git remote -v
bakkdoor  https://github.com/bakkdoor/grit (fetch)
bakkdoor  https://github.com/bakkdoor/grit (push)
cho45     https://github.com/cho45/grit (fetch)
cho45     https://github.com/cho45/grit (push)
defunkt   https://github.com/defunkt/grit (fetch)
defunkt   https://github.com/defunkt/grit (push)
koke      git://github.com/koke/grit.git (fetch)
koke      git://github.com/koke/grit.git (push)
origin    git@github.com:mojombo/grit.git (fetch)
origin    git@github.com:mojombo/grit.git (push)
```

**이렇게 리모트 저장소가 여러 개 등록되어 있으면 다른 사람이 기여한 내용(Contributions)을 쉽게 가져올 수 있다.** 어떤 저장소에는 Push 권한까지 제공하기도 하지만 일단 이 화면에서 Push 가능 권한까지는 확인할 수 없다.

리모트 저장소와 데이터를 주고받는데 사용하는 다양한 프로토콜에 대해서는 [서버에 Git 설치하기](https://git-scm.com/book/ko/v2/ch00/_getting_git_on_a_server) 에서 자세히 살펴보기로 한다.


### 리모트 저장소 추가하기

이전 절에서도 `git clone` 명령이 묵시적으로 `origin` 리모트 저장소를 어떻게 추가되는지 설명했었지만 수박 겉핥기식으로 살펴봤을 뿐이었다. 여기에서는 리모트 저장소를 추가하는 방법을 자세하게 설명한다. 기존 워킹 디렉토리에 새 리모트 저장소를 쉽게 추가할 수 있는데 `git remote add <단축이름> <url>` 명령을 사용한다.

```sh
$ git remote
origin
$ git remote add pb https://github.com/paulboone/ticgit
$ git remote -v
origin	https://github.com/schacon/ticgit (fetch)
origin	https://github.com/schacon/ticgit (push)
pb	https://github.com/paulboone/ticgit (fetch)
pb	https://github.com/paulboone/ticgit (push)
```


이제 URL 대신에 `pb` 라는 이름을 사용할 수 있다. 예를 들어 로컬 저장소에는 없지만 Paul의 저장소에 있는 것을 가져오려면 아래과 같이 실행한다.

```sh
$ git fetch pb
remote: Counting objects: 43, done.
remote: Compressing objects: 100% (36/36), done.
remote: Total 43 (delta 10), reused 31 (delta 5)
Unpacking objects: 100% (43/43), done.
From https://github.com/paulboone/ticgit
 * [new branch]      master     -> pb/master
 * [new branch]      ticgit     -> pb/ticgit
```

로컬에서 `pb/master` 가 Paul의 master 브랜치이다. 이 브랜치를 로컬 브랜치중 하나에 Merge 하거나 Checkout 해서 브랜치 내용을 자세히 확인할 수 있다. (브랜치를 어떻게 사용하는지는 [Git 브랜치](https://git-scm.com/book/ko/v2/ch00/ch03-git-branching) 에서 자세히 살펴본다)


### 리모트 저장소를 Pull 하거나 Fetch 하기

앞서 설명했듯이 리모트 저장소에서 데이터를 가져오려면 간단히 아래와 같이 실행한다.

```sh
$ git fetch <remote>
```

이 명령은 로컬에는 없지만, **리모트 저장소에는 있는 데이터를 모두 가져온다.** 그러면 리모트 저장소의 모든 브랜치를 로컬에서 접근할 수 있어서 언제든지 Merge를 하거나 내용을 살펴볼 수 있다.

저장소를 Clone 하면 명령은 자동으로 리모트 저장소를 “origin” 이라는 이름으로 추가한다. 그래서 나중에 `git fetch origin` 명령을 실행하면 Clone 한 이후에(혹은 마지막으로 가져온 이후에) 수정된 것을 모두 가져온다. **`git fetch` 명령은 리모트 저장소의 데이터를 모두 로컬로 가져오지만, 자동으로 Merge** 하지 않는다. 그래서 당신이 로컬에서 하던 작업을 정리하고 나서 수동으로 Merge 해야 한다.

그냥 쉽게 **`git pull` 명령으로 리모트 저장소 브랜치에서 데이터를 가져올 뿐만 아니라 자동으로 로컬 브랜치와 Merge** 시킬 수 있다(다음 섹션과 [Git 브랜치](https://git-scm.com/book/ko/v2/ch00/ch03-git-branching) 에서 좀더 자세히 살펴본다). 먼저 `git clone` 명령은 자동으로 로컬의 master 브랜치가 리모트 저장소의 master 브랜치를 추적하도록 한다(물론 리모트 저장소에 master 브랜치가 있다는 가정에서). 그리고 `git pull` 명령은 Clone 한 서버에서 데이터를 가져오고 그 데이터를 자동으로 현재 작업하는 코드와 Merge 시킨다.


### 리모트 저장소에 Push 하기

프로젝트를 공유하고 싶을 때 **Upstream 저장소에 Push 할 수 있다**. 이 명령은 `git push <리모트 저장소 이름> <브랜치 이름>`으로 단순하다. master 브랜치를 `origin` 서버에 Push 하려면(다시 말하지만 Clone 하면 보통 자동으로 origin 이름이 생성된다) 아래와 같이 서버에 Push 한다.

```sh
$ git push origin master
```

이 명령은 **Clone 한 리모트 저장소에 쓰기 권한이 있고, Clone 하고 난 이후 아무도 Upstream 저장소에 Push 하지 않았을 때만 사용할 수 있다.** 다시 말해서 Clone 한 사람이 여러 명 있을 때, **다른 사람이 Push 한 후에 Push 하려고 하면 Push 할 수 없다.** **먼저 다른 사람이 작업한 것을 가져와서 Merge 한 후에 Push 할 수 있다.** [Git 브랜치](https://git-scm.com/book/ko/v2/ch00/ch03-git-branching) 에서 서버에 Push 하는 방법에 대해 자세히 설명할 것이다.

> ✏️ Upstream 이란?
>
> 다른 사람의 GitHub의 저장소를 Fork한 경우 내 GitHub가 origin이 됩니다. 여러분이 처음 fork를 시도한 저장소를 upstream이라고 부릅니다. origin와 upstream 모두 remote 저장소입니다. 보통 origin과 구분하기 위해서 upstream 이라는 명칭을 주로 사용합니다.



### 리모트 저장소 살펴보기

`git remote show <리모트 저장소 이름>` 명령으로 리모트 저장소의 구체적인 정보를 확인할 수 있다. `origin` 같은 단축이름으로 이 명령을 실행하면 아래와 같은 정보를 볼 수 있다.

```sh
$ git remote show origin
* remote origin
  Fetch URL: https://github.com/schacon/ticgit
  Push  URL: https://github.com/schacon/ticgit
  HEAD branch: master
  Remote branches:
    master                               tracked
    dev-branch                           tracked
  Local branch configured for 'git pull':
    master merges with remote master
  Local ref configured for 'git push':
    master pushes to master (up to date)
```

리모트 저장소의 URL과 추적하는 브랜치를 출력한다. 이 명령은 `git pull` 명령을 실행할 때 master 브랜치와 Merge 할 브랜치가 무엇인지 보여 준다. `git pull` 명령은 리모트 저장소 브랜치의 데이터를 모두 가져오고 나서 자동으로 Merge 할 것이다. 그리고 가져온 모든 리모트 저장소 정보도 출력한다.

좀 더 Git을 열심히 사용하다 보면 `git remote show` 명령으로 더 많은 정보를 보는 날이 온다. 여러분도 언젠가는 아래와 같은 메시지(역주 - 다수의 브랜치를 사용하는 메시지)를 볼 날이 올 것이다.

```sh
$ git remote show origin
* remote origin
  URL: https://github.com/my-org/complex-project
  Fetch URL: https://github.com/my-org/complex-project
  Push  URL: https://github.com/my-org/complex-project
  HEAD branch: master
  Remote branches:
    master                           tracked
    dev-branch                       tracked
    markdown-strip                   tracked
    issue-43                         new (next fetch will store in remotes/origin)
    issue-45                         new (next fetch will store in remotes/origin)
    refs/remotes/origin/issue-11     stale (use 'git remote prune' to remove)
  Local branches configured for 'git pull':
    dev-branch merges with remote dev-branch
    master     merges with remote master
  Local refs configured for 'git push':
    dev-branch                     pushes to dev-branch                     (up to date)
    markdown-strip                 pushes to markdown-strip                 (up to date)
    master                         pushes to master                         (up to date)
```

브랜치명을 생략하고 `git push` 명령을 실행할 때 어떤 브랜치가 어떤 브랜치로 Push 되는지 보여준다. 또 아직 로컬로 가져오지 않은 리모트 저장소의 브랜치는 어떤 것들이 있는지, 서버에서는 삭제됐지만 아직 가지고 있는 브랜치는 어떤 것인지, `git pull` 명령을 실행했을 때 자동으로 Merge 할 브랜치는 어떤 것이 있는지 보여준다.



### 리모트 저장소 이름을 바꾸거나 리모트 저장소를 삭제하기

`git remote rename` 명령으로 리모트 저장소의 이름을 변경할 수 있다. 예를 들어 `pb` 를 `paul` 로 변경하려면 `git remote rename` 명령을 사용한다.

```sh
$ git remote rename pb paul
$ git remote
origin
paul
```

로컬에서 관리하던 리모트 저장소의 브랜치 이름도 바뀐다는 점을 생각해두자. 여태까지 `pb/master` 로 리모트 저장소 브랜치를 사용했으면 이제는 `paul/master` 라고 사용해야 한다.

리모트 저장소를 삭제해야 한다면 `git remote remove` 나 `git remote rm` 명령을 사용한다. 서버 정보가 바뀌었을 때, 더는 별도의 미러가 필요하지 않을 때, 더는 기여자가 활동하지 않을 때 필요하다.

```sh
$ git remote remove paul
$ git remote
origin
```

위와 같은 방법으로 리모트 저장소를 삭제하면 해당 리모트 저장소에 관련된 추적 브랜치 정보나 모든 설정 내용도 함께 삭제된다.



## 2.6 태그

*특정 release 버전에 대한 포인터를 제공하기 위함*

다른 VCS처럼 Git도 태그를 지원한다. 사람들은 보통 릴리즈할 때 사용한다(v1.0, 등등). 이번에는 태그를 조회하고 생성하는 법과 태그의 종류를 설명한다.



### 태그 조회하기

우선 `git tag` 명령으로 (`-l`, `--list`는 옵션) 이미 만들어진 태그가 있는지 확인할 수 있다.

```sh
$ git tag
v0.1
v1.3
```

이 명령은 알파벳 순서로 태그를 보여준다. 사실 순서는 별로 중요한 게 아니다.

검색 패턴을 사용하여 태그를 검색할 수 있다. Git 소스 저장소는 500여 개의 태그가 있다. 만약 1.8.5 버전의 태그들만 검색하고 싶으면 아래와 같이 실행한다.

```sh
$ git tag -l "v1.8.5*"
v1.8.5
v1.8.5-rc0
v1.8.5-rc1
v1.8.5-rc2
v1.8.5-rc3
v1.8.5.1
v1.8.5.2
v1.8.5.3
v1.8.5.4
v1.8.5.5
```

> ⚠️ 주의
>
> 와일드카드를 사용하여 Tag 리스트를 확인하려면 `-l`, `--list` 옵션을 지정해야한다. 단순히 모든 Tag 목록을 확인하기 위해 `git tag` 명령을 실행했을 때 `-l` 또는 `--list` 옵션이 적용된 것과 동일한 결과가 출력된다. 하지만 와일드카드를 사용하여 태그 목록을 검색하는 경우에는 반드시 `-l` 또는 `--list` 옵션을 같이 써 줘야 원하는 결과를 얻을 수 있다.



### 태그 붙이기

Git의 태그는 ***Lightweight*** 태그와 ***Annotated*** 태그로 두 종류가 있다.

Lightweight 태그는 브랜치와 비슷한데 브랜치처럼 가리키는 지점을 최신 커밋으로 이동시키지 않는다. 단순히 특정 커밋에 대한 포인터일 뿐이다.

한편 Annotated 태그는 Git 데이터베이스에 태그를 만든 사람의 이름, 이메일과 태그를 만든 날짜, 그리고 태그 메시지도 저장한다. GPG(GNU Privacy Guard)로 서명할 수도 있다. 일반적으로 Annotated 태그를 만들어 이 모든 정보를 사용할 수 있도록 하는 것이 좋다. 하지만 임시로 생성하는 태그거나 이러한 정보를 유지할 필요가 없는 경우에는 Lightweight 태그를 사용할 수도 있다.

### Annotated 태그

Annotated 태그를 만드는 방법은 간단하다. `tag` 명령을 실행할 때 `-a` 옵션을 추가한다.

```sh
$ git tag -a v1.4 -m "my version 1.4"
$ git tag
v0.1
v1.3
v1.4
```

-m` 옵션으로 태그를 저장할 때 메시지를 함께 저장할 수 있다. 명령을 실행할 때 메시지를 입력하지 않으면 Git은 편집기를 실행시킨다.

`git show` 명령으로 태그 정보와 커밋 정보를 모두 확인할 수 있다.

```sh
$ git show v1.4
tag v1.4
Tagger: Ben Straub <ben@straub.cc>
Date:   Sat May 3 20:19:12 2014 -0700

my version 1.4

commit ca82a6dff817ec66f44342007202690a93763949
Author: Scott Chacon <schacon@gee-mail.com>
Date:   Mon Mar 17 21:52:11 2008 -0700

    changed the version number
```

커밋 정보를 보여주기 전에 먼저 태그를 만든 사람이 누구인지, 언제 태그를 만들었는지, 그리고 태그 메시지가 무엇인지 보여준다.



### Lightweight 태그

Lightweight 태그는 기본적으로 파일에 커밋 체크섬을 저장하는 것뿐이다. 다른 정보는 저장하지 않는다. Lightweight 태그를 만들 때는 `-a`, `-s`, `-m` 옵션을 사용하지 않는다. 이름만 달아줄 뿐이다.

```sh
$ git tag v1.4-lw
$ git tag
v0.1
v1.3
v1.4
v1.4-lw
v1.5
```

이 태그에 `git show` 를 실행하면 별도의 태그 정보를 확인할 수 없다. 이 명령은 단순히 커밋 정보만을 보여준다.

```sh
$ git show v1.4-lw
commit ca82a6dff817ec66f44342007202690a93763949
Author: Scott Chacon <schacon@gee-mail.com>
Date:   Mon Mar 17 21:52:11 2008 -0700

    changed the version number
```



### 나중에 태그하기

예전 커밋에 대해서도 태그할 수 있다. 커밋 히스토리는 아래와 같다고 가정한다.

```sh
$ git log --pretty=oneline
15027957951b64cf874c3557a0f3547bd83b3ff6 Merge branch 'experiment'
a6b4c97498bd301d84096da251c98a07c7723e65 beginning write support
0d52aaab4479697da7686c15f77a3d64d9165190 one more thing
6d52a271eda8725415634dd79daabbc4d9b6008e Merge branch 'experiment'
0b7434d86859cc7b8c3d5e1dddfed66ff742fcbc added a commit function
4682c3261057305bdd616e23b64b0857d832627b added a todo file
166ae0c4d3f420721acbb115cc33848dfcc2121a started write support
9fceb02d0ae598e95dc970b74767f19372d61af8 updated rakefile
964f16d36dfccde844893cac5b347e7b3d44abbc commit the todo
8a5cbc430f1a9c3d00faaeffd07798508422908a updated readme
```

“updated rakefile” 커밋을 v1.2로 태그하지 못했다고 해도 나중에 태그를 붙일 수 있다. 특정 커밋에 태그하기 위해서 명령의 끝에 커밋 체크섬을 명시한다(긴 체크섬을 전부 사용할 필요는 없다).

```sh
$ git tag -a v1.2 9fceb02
```

이제 아래와 같이 만든 태그를 확인한다.

```sh
$ git tag
v0.1
v1.2
v1.3
v1.4
v1.4-lw
v1.5

$ git show v1.2
tag v1.2
Tagger: Scott Chacon <schacon@gee-mail.com>
Date:   Mon Feb 9 15:32:16 2009 -0800

version 1.2
commit 9fceb02d0ae598e95dc970b74767f19372d61af8
Author: Magnus Chacon <mchacon@gee-mail.com>
Date:   Sun Apr 27 20:43:35 2008 -0700

    updated rakefile
...
```



### 태그 공유하기

**`git push` 명령은 자동으로 리모트 서버에 태그를 전송하지 않는다.** 태그를 만들었으면 서버에 별도로 Push 해야 한다. 브랜치를 공유하는 것과 같은 방법으로 할 수 있다. `git push origin <태그 이름>`을 실행한다.

```sh
$ git push origin v1.5
Counting objects: 14, done.
Delta compression using up to 8 threads.
Compressing objects: 100% (12/12), done.
Writing objects: 100% (14/14), 2.05 KiB | 0 bytes/s, done.
Total 14 (delta 3), reused 0 (delta 0)
To git@github.com:schacon/simplegit.git
 * [new tag]         v1.5 -> v1.5
```

만약 한 번에 태그를 여러 개 Push 하고 싶으면 `--tags` 옵션을 추가하여 `git push` 명령을 실행한다. 이 명령으로 리모트 서버에 없는 태그를 모두 전송할 수 있다.

```sh
$ git push origin --tags
Counting objects: 1, done.
Writing objects: 100% (1/1), 160 bytes | 0 bytes/s, done.
Total 1 (delta 0), reused 0 (delta 0)
To git@github.com:schacon/simplegit.git
 * [new tag]         v1.4 -> v1.4
 * [new tag]         v1.4-lw -> v1.4-lw
```

이제 누군가 저장소에서 Clone 하거나 Pull을 하면 모든 태그 정보도 함께 전송된다.



### 태그를 Checkout 하기

예를 들어 태그가 특정 버전을 가리키고 있고, 특정 버전의 파일을 체크아웃 해서 확인하고 싶다면 다음과 같이 실행한다. 단 태그를 체크아웃하면(브랜치를 체크아웃 하는 것이 아니라면) “detached HEAD”(떨어져나온 HEAD) 상태가 되며 일부 Git 관련 작업이 브랜치에서 작업하는 것과 다르게 동작할 수 있다.

```sh
$ git checkout 2.0.0
Note: checking out '2.0.0'.

You are in 'detached HEAD' state. You can look around, make experimental
changes and commit them, and you can discard any commits you make in this
state without impacting any branches by performing another checkout.

If you want to create a new branch to retain commits you create, you may
do so (now or later) by using -b with the checkout command again. Example:

  git checkout -b <new-branch>

HEAD is now at 99ada87... Merge pull request #89 from schacon/appendix-final

$ git checkout 2.0-beta-0.1
Previous HEAD position was 99ada87... Merge pull request #89 from schacon/appendix-final
HEAD is now at df3f601... add atlas.json and cover image
```

“detached HEAD”(떨어져나온 HEAD) 상태에서는 작업을 하고 커밋을 만들면, 태그는 그대로 있으나 새로운 커밋이 하나 쌓이 상태가 되고 새 커밋에 도달할 수 있는 방법이 따로 없게 된다. 물론 커밋의 해시 값을 정확히 기억하고 있으면 가능하긴 하다. 특정 태그의 상태에서 새로 작성한 커밋이 버그 픽스와 같이 의미있도록 하려면 반드시 브랜치를 만들어서 작업하는 것이 좋다.

```sh
$ git checkout -b version2 v2.0.0
Switched to a new branch 'version2'
```

물론 이렇게 브랜치를 만든 후에 `version2` 브랜치에 커밋하면 브랜치는 업데이트된다. 하지만, `v2.0.0` 태그는 가리키는 커밋이 변하지 않았으므로 두 내용이 가리키는 커밋이 다르다는 것을 알 수 있다.



## 2.7 Git Alias

Git의 기초를 마치기 전에 Git을 좀 더 쉽고 편안하게 쓸 수 있게 만들어 줄 Alias 라는 팁 알려주려 한다. 우리는 이 책에서 이 팁을 다시 거론하지 않고 이런 팁을 알고 있다고 가정한다. 그래서 알고 있는 것이 좋다.

명령을 완벽하게 입력하지 않으면 Git은 알아듣지 못한다. Git의 명령을 전부 입력하는 것이 귀찮다면 `git config` 를 사용하여 각 명령의 Alias을 쉽게 만들 수 있다. 아래는 Alias을 만드는 예이다.

```sh
$ git config --global alias.co checkout
$ git config --global alias.br branch
$ git config --global alias.ci commit
$ git config --global alias.st status
```

이제 `git commit` 대신 `git ci` 만으로도 커밋할 수 있다. Git을 계속 사용한다면 다른 명령어도 자주 사용하게 될 것이다. 주저말고 자주 사용하는 명령은 Alias을 만들어 편하게 사용하시길 바란다.

이미 있는 명령을 편리하고 새로운 명령으로 만들어 사용할 수 있다. 예를 들어 파일을 Unstaged 상태로 변경하는 명령을 만들어서 불편함을 덜 수 있다. 아래와 같이 unstage 라는 Alias을 만든다.

```sh
$ git config --global alias.unstage 'reset HEAD --'
```

아래 두 명령은 동일한 명령이다.

```sh
$ git unstage fileA
$ git reset HEAD -- fileA
```

한결 간결해졌다. 

추가로 `last` 명령을 만들어 보자:

```sh
$ git config --global alias.last 'log -1 HEAD'
```

이제 최근 커밋을 좀 더 쉽게 확인할 수 있다.

```sh
$ git last
commit 66938dae3329c7aebe598c2246a8e6af90d04646
Author: Josh Goebel <dreamer3@example.com>
Date:   Tue Aug 26 19:48:51 2008 +0800

    test for current head

    Signed-off-by: Scott Chacon <schacon@example.com>
```

이것으로 쉽게 새로운 명령을 만들 수 있다. 그리고 Git의 명령어뿐만 아니라 외부 명령어도 실행할 수 있다. `!` 를 제일 앞에 추가하면 외부 명령을 실행한다. 커스텀 스크립트를 만들어서 사용할 때 매우 유용하다. 아래 명령은 `git visual` 이라고 입력하면 `gitk` 가 실행된다.

```sh
$ git config --global alias.visual '!gitk'
```



## 2.8 요약

이제 우리는 로컬에서 사용할 수 있는 Git 명령에 대한 기본 지식은 갖추었다. 저장소를 만들고 Clone 하는 방법, 수정하고 나서 Stage 하고 커밋하는 방법, 저장소의 히스토리를 조회하는 방법 등을 살펴보았다. 이어지는 장에서는 Git의 가장 강력한 기능인 브랜치 모델을 살펴볼 것이다.



## 참고

- [Pro Git](https://git-scm.com/book/ko/v2)

- [Git Tag 꼭 써야할까?](https://programmingsummaries.tistory.com/395)

