## 락(lock) 처리



## 멀티 스레드 대표적 문제

- 레이스 컨디션(race condition)
- 데드락(deadlock)
- 스타베이션(starvation)
- 라이브 록(live lock)
- 다른 형태의 예측 불가능한 오류(nondeterminism)



### 레이스 컨디션

공유 데이터 처리 시 아무런 락 처리를 하지 않을 경우, 동시에 여러 스레드에서 데이터를 수정할 수 있다. 경우에 따라서 동시에 스레드가 처리하러 들어왔을 경우 데이터가 꼬여서 무한 대기하거나 무한 루프에 빠질 수 있는데 이를 레이스 컨디션이라고 한다.



### 데드락

데이터를 안전하게 처리하기 위해서 거는 락에서 문제가 발생할 수 있다. 두 개 이상의 스레드에서 이 락이 서로 풀리기만을 기다리는 상황이 발생할 수 있는데, 이러한 상황을 데드록이라고한다.



### 스타베이션

어떻게 보면 데드락과 비슷하다. 하지만 그 원인은 데드락과 다르다. 스타베이션은 멈추어 있는 스레드가 이론적으로 수행은 할 수 있지만 CPU로부터 일 할 기회를 받지 못하는 경우 때문에 발생한다. 스레드에는 우선순위라는 것이 있다. 그 우선순위가 다른 스레드보다 낮으면 해당 스레드는 스타베이션에 빠질 수 있다. 그래서 보통은 스레드의 우선순위를 건드리지 않는게 좋다.



### 라이브 락

하나의 스레드에서 다른 스레드로 응답을 주는 경우가 있다. 그런데 응답을 받은 스레드에서 요청했던 스레드로 다시 요청을 하는 작업이 계속 반복될 수 있는데, 이것을 라이브 락이라고 한다. 데드락과의 차이는 데드락의 경우 CPU를 점유하지 않고 멈추어 버리지만, 라이브 락은 멈추지 않고 지속해서 수행하므로 CPU까지 점유할 확률이 높다. 그렇게 되면 CPU 코어 하나를 모두 점유해 버릴 수 있으니, CPU 사용량도 같이 모니터링하는 것을 권장한다.



## 데드락 예제

다음은 오라클  튜토리얼에서 제공하는 [데드락 예제](https://docs.oracle.com/javase/tutorial/essential/concurrency/deadlock.html)이다.

```java
public class Deadlock {
    static class Friend {
        private final String name;

        public Friend(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public synchronized void bow(Friend bower) {
            System.out.format("%s: %s"
                            + "  has bowed to me!%n",
                    this.name, bower.getName());
            bower.bowBack(this);
        }

        public synchronized void bowBack(Friend bower) {
            System.out.format("%s: %s"
                            + " has bowed back to me!%n",
                    this.name, bower.getName());
        }
    }

    public static void main(String[] args) {
        final Friend alphonse = new Friend("Alphonse");
        final Friend gaston = new Friend("Gaston");

        new Thread(new Runnable() {
            public void run() { alphonse.bow(gaston); }
        }).start();

        new Thread(new Runnable() {
            public void run() { gaston.bow(alphonse); }
        }).start();
    }
}
```



이를 실행하면 다음과 같은 메시지를 출력하고 멈춰 있을 것이다.

```
Alphonse: Gaston  has bowed to me!
Gaston: Alphonse  has bowed to me!
```



> 🔍 synchronized 메소드
>
> 



## 참고

- [오라클 동시성 튜토리얼](https://docs.oracle.com/javase/tutorial/essential/concurrency/index.html)

