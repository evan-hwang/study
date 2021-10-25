# 스레드 단면(thread dump)

스레드 단면은 보통 스레드 덤프라고 한다. 스레드 덤프를 생성하는 명령어를 수행하면, 수행한 그 시점에 JVM에서 수행되고 있는 모든 스레드가 무슨 일을 하고 있는지 알 수 있다. 



### 스레드 단면이 필요한 시점

운영 중인 시스템에 문제가 있을 때, APM 같은 도구가 없다면 반드시 스레드 단면을 잘라봐야한다.

다음과 같은 경우 단면 분석을 통해 매우 빨리 원인을 찾을 수 있다.

- 모든 시스템이 응답이 없을 때 (시스템 행)
- 사용자 수가 많지도 않은데, 시스템의 CPU 사용량이 떨어지지 않을 때
- 특정 어플리케이션을 수행했는데, 전혀 응답이 없을 때
- 기타 여러 가지 상황에서 시스템이 내 마음대로 동작하지 않을 때



### 자바 애플리케이션 실행

다음과 같은 간단한 자바 예제를 실행해보고, 단면을 잘라보자.

```java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreadDump {
    public static void main(String[] args) {
        for (int loop = 0; loop < 3; loop++) {
            LoopingThread thread = new LoopingThread();
            thread.start();
        }

        System.out.println("Started looping threads..." +
                " You must stop this process after test...");
    }
}
class LoopingThread extends Thread {
    public void run() {
        int runCount = 100;
        while(true) {
            try {
                String string = new String("AAA");
                List<String> list = new ArrayList<>(runCount);
                for (int loop = 0; loop < runCount; loop++) {
                    list.add(string);
                }
                Map<String, Integer> hashMap = new HashMap<>(runCount);
                for (int loop = 0; loop < runCount; loop++) {
                    hashMap.put(string + loop, loop);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
```

위 자바 프로그램을 실행하자.

1. `javac ThreadDump.java`
2. `java ThreadDump`
   - `java -XX:+PrintConcurrentLocks -XX+PrintHistogram ThreadDump` 옵션을 주고 실행하면 더 자세한 정보를 뿌려준다.
   - `-XX:+PrintConcurrentLocks` : 각 스레드 정보 출력 시 록의 상태 출력
   - `-XX+PrintHistogram` : 클래스별로 점유하고 있는 메모리의 히스토그램을 출력



### 스레드 단면 생성

1. 자바 프로세스들의 상태만 확인할 수 있는`jps` 명령어를 통해 실행 중인 어플리케이션을 확인한다.
2. `kill` 커맨드의 `-QUIT` 옵션(mac에서는 3) 을 통해 단면 확인

이 과정을 거치면 아래와 같이 스레드 단면의 정보를 볼 수 있다.



### 스레드 단면 정보

**스레드 단면 예시**

각 내용에 대한 자세한 설명은 아래에서 다룬다.

```bash
❯ java ThreadDump
Started looping threads... You must stop this process after test...

# 1. 스레드 단면 생성 시간 정보
2021-10-25 18:04:12

# 2. JVM에 대한 정보
Full thread dump Java HotSpot(TM) 64-Bit Server VM (11.0.12+8-LTS-237 mixed mode):

Threads class SMR info:
_java_thread_list=0x00007f933c0a2a30, length=13, elements={
0x00007f933b046800, 0x00007f933b04b800, 0x00007f933c923000, 0x00007f933c923800,
0x00007f933c924800, 0x00007f933b827800, 0x00007f933b04d000, 0x00007f933b829800,
0x00007f933b082000, 0x00007f933b048800, 0x00007f933f836800, 0x00007f933f024000,
0x00007f933f8c0800
}

# 3. 각 스레드 스택을 포함한 다양한 정보
"Reference Handler" #2 daemon prio=10 os_prio=31 cpu=0.35ms elapsed=17.42s tid=0x00007f933b046800 nid=0x3303 waiting on condition  [0x000070000b709000]
   java.lang.Thread.State: RUNNABLE
	at java.lang.ref.Reference.waitForReferencePendingList(java.base@11.0.12/Native Method)
	at java.lang.ref.Reference.processPendingReferences(java.base@11.0.12/Reference.java:241)
	at java.lang.ref.Reference$ReferenceHandler.run(java.base@11.0.12/Reference.java:213)

"Finalizer" #3 daemon prio=8 os_prio=31 cpu=0.19ms elapsed=17.42s tid=0x00007f933b04b800 nid=0x3503 in Object.wait()  [0x000070000b80c000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(java.base@11.0.12/Native Method)
	- waiting on <0x0000000700002b48> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(java.base@11.0.12/ReferenceQueue.java:155)
	- waiting to re-lock in wait() <0x0000000700002b48> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(java.base@11.0.12/ReferenceQueue.java:176)
	at java.lang.ref.Finalizer$FinalizerThread.run(java.base@11.0.12/Finalizer.java:170)

"Signal Dispatcher" #4 daemon prio=9 os_prio=31 cpu=0.10ms elapsed=17.41s tid=0x00007f933c923000 nid=0x3b03 waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Service Thread" #5 daemon prio=9 os_prio=31 cpu=0.03ms elapsed=17.41s tid=0x00007f933c923800 nid=0x5503 runnable  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread0" #6 daemon prio=9 os_prio=31 cpu=32.05ms elapsed=17.41s tid=0x00007f933c924800 nid=0x5603 waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE
   No compile task

"C1 CompilerThread0" #14 daemon prio=9 os_prio=31 cpu=35.01ms elapsed=17.41s tid=0x00007f933b827800 nid=0xa503 waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE
   No compile task

"Sweeper thread" #18 daemon prio=9 os_prio=31 cpu=0.02ms elapsed=17.41s tid=0x00007f933b04d000 nid=0x5803 runnable  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Common-Cleaner" #19 daemon prio=8 os_prio=31 cpu=0.23ms elapsed=17.38s tid=0x00007f933b829800 nid=0xa003 in Object.wait()  [0x000070000c12a000]
   java.lang.Thread.State: TIMED_WAITING (on object monitor)
	at java.lang.Object.wait(java.base@11.0.12/Native Method)
	- waiting on <0x0000000700003a50> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(java.base@11.0.12/ReferenceQueue.java:155)
	- waiting to re-lock in wait() <0x0000000700003a50> (a java.lang.ref.ReferenceQueue$Lock)
	at jdk.internal.ref.CleanerImpl.run(java.base@11.0.12/CleanerImpl.java:148)
	at java.lang.Thread.run(java.base@11.0.12/Thread.java:834)
	at jdk.internal.misc.InnocuousThread.run(java.base@11.0.12/InnocuousThread.java:134)

"Thread-0" #20 prio=5 os_prio=31 cpu=17042.01ms elapsed=17.38s tid=0x00007f933b082000 nid=0x5e03 runnable  [0x000070000c22c000]
   java.lang.Thread.State: RUNNABLE
	at LoopingThread.run(ThreadDump.java:29)

"Thread-1" #21 prio=5 os_prio=31 cpu=17039.10ms elapsed=17.38s tid=0x00007f933b048800 nid=0x6003 runnable  [0x000070000c32f000]
   java.lang.Thread.State: RUNNABLE
	at LoopingThread.run(ThreadDump.java:29)

"Thread-2" #22 prio=5 os_prio=31 cpu=17039.66ms elapsed=17.38s tid=0x00007f933f836800 nid=0x9c03 runnable  [0x000070000c432000]
   java.lang.Thread.State: RUNNABLE
	at java.util.HashMap.put(java.base@11.0.12/HashMap.java:607)
	at LoopingThread.run(ThreadDump.java:29)

"DestroyJavaVM" #23 prio=5 os_prio=31 cpu=57.98ms elapsed=17.38s tid=0x00007f933f024000 nid=0x1703 waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread1" #7 daemon prio=9 os_prio=31 cpu=19.50ms elapsed=17.35s tid=0x00007f933f8c0800 nid=0x9803 waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE
   No compile task

"VM Thread" os_prio=31 cpu=141.34ms elapsed=17.43s tid=0x00007f933c918000 nid=0x3003 runnable

"GC Thread#0" os_prio=31 cpu=74.95ms elapsed=17.44s tid=0x00007f933b810800 nid=0x5203 runnable

"GC Thread#1" os_prio=31 cpu=74.78ms elapsed=17.27s tid=0x00007f93400c0000 nid=0x7203 runnable

"GC Thread#2" os_prio=31 cpu=74.30ms elapsed=17.27s tid=0x00007f93400c1000 nid=0x7403 runnable

"GC Thread#3" os_prio=31 cpu=73.59ms elapsed=17.27s tid=0x00007f933b083000 nid=0x7503 runnable

"GC Thread#4" os_prio=31 cpu=71.60ms elapsed=17.27s tid=0x00007f933f8f1800 nid=0x7703 runnable

"GC Thread#5" os_prio=31 cpu=72.45ms elapsed=17.27s tid=0x00007f933ca28000 nid=0x7803 runnable

"GC Thread#6" os_prio=31 cpu=74.25ms elapsed=17.27s tid=0x00007f933f8f2800 nid=0x8d03 runnable

"GC Thread#7" os_prio=31 cpu=74.24ms elapsed=17.27s tid=0x00007f933b82a800 nid=0x8b03 runnable

"GC Thread#8" os_prio=31 cpu=65.25ms elapsed=16.84s tid=0x00007f934006a000 nid=0x8a03 runnable

"GC Thread#9" os_prio=31 cpu=65.32ms elapsed=16.84s tid=0x00007f933b92f000 nid=0x7b03 runnable

"GC Thread#10" os_prio=31 cpu=62.36ms elapsed=16.62s tid=0x00007f933f022800 nid=0x7c03 runnable

"GC Thread#11" os_prio=31 cpu=63.02ms elapsed=16.62s tid=0x00007f933f09d000 nid=0x8603 runnable

"GC Thread#12" os_prio=31 cpu=61.59ms elapsed=16.62s tid=0x00007f933f09e000 nid=0x8403 runnable

"G1 Main Marker" os_prio=31 cpu=0.16ms elapsed=17.44s tid=0x00007f933b03f000 nid=0x5003 runnable

"G1 Conc#0" os_prio=31 cpu=0.03ms elapsed=17.44s tid=0x00007f933c808800 nid=0x4c03 runnable

"G1 Refine#0" os_prio=31 cpu=0.24ms elapsed=17.44s tid=0x00007f933c8bd800 nid=0x4a03 runnable

"G1 Young RemSet Sampling" os_prio=31 cpu=6.37ms elapsed=17.44s tid=0x00007f933c8be000 nid=0x4803 runnable
"VM Periodic Task Thread" os_prio=31 cpu=9.17ms elapsed=17.39s tid=0x00007f933b823000 nid=0x5a03 waiting on condition

JNI global refs: 14, weak refs: 0

# 5. 힙 영역의 사용 현황
Heap
 garbage-first heap   total 589824K, used 234200K [0x0000000700000000, 0x0000000800000000)
  region size 1024K, 230 young (235520K), 1 survivors (1024K)
 Metaspace       used 4938K, capacity 5035K, committed 5120K, reserved 1056768K
  class space    used 416K, capacity 443K, committed 512K, reserved 1048576K
```



1. 스레드 단면 생성 시간 정보

   ```bash
   2021-10-25 18:04:12
   ```

   

2. JVM에 대한 정보

   ```bash
   Full thread dump Java HotSpot(TM) 64-Bit Server VM (11.0.12+8-LTS-237 mixed mode):
   ```

   

3. 각 스레드 스택을 포함한 다양한 정보

   ```bash
   "Thread-0" #20 prio=5 os_prio=31 cpu=17042.01ms elapsed=17.38s tid=0x00007f933b082000 nid=0x5e03 runnable  [0x000070000c22c000]
      java.lang.Thread.State: RUNNABLE
   	at LoopingThread.run(ThreadDump.java:29)
   ```

   - 하나만 가져와서 살펴보자.

     - 스레드 이름

       - 스레드의 이름이며, 이 이름은 스레드 객체 생성 시 지정할 수 있다.

     - 식별자

       - 데몬 스레드일 경우에만 표시된다. (`daemon` 이라고 표시됨)

     - 스레드 우선순위(prio)

       - 스레드의 우선순위르르 숫자로 나타낸다.(가장 낮은것이 1, 높은 것이 10)

     - 스레드 ID(tid)

       - 다른 스레드와 구분되는 스레드의 ID를 나타낸다. 정확하게 말하면, 해당 스레드가 점유하는 메모리의 주소를 표시한다.

     - 네이티브 스레드 ID(nid)

       - OS에서 관리하는 스레드의 ID를 나타낸다.

     - 스레드의 상태

       - 스레드 단면을 생성할 때 해당 스레드가 하고 있던 작업에 대한 설명을 표시해준다.

         | 스레드 상태   | 설명                                                         |
         | ------------- | ------------------------------------------------------------ |
         | NEW           | 스레드가 아직 시작되지 않은 상태                             |
         | RUNNABLE      | 스레드가 수행 중인 상태                                      |
         | BLOCKED       | 스레드가 잠겨 있어 풀리기를 기다리는 상태                    |
         | WAITING       | 다른 스레드가 특정 작업을 수행하여 깨울 때까지 무한정 기다리는 상태 |
         | TIMED_WAITING | 다른 스레드가 특정 작업을 수행하여 깨울 때까지 지정된 시간만큼 기다리고 있는 상태 |
         | TERMINATED    | 스레드가 종료된 상태                                         |

     - 주소 범위

       - 스레드의 스택 영역의 예상된 주소 범위다.
       - 스택은 아래서부터 위로 실행된 순서를 나타낸다.



4. 데드록에 대한 정보

   ```bash
   Found one Java-level deadlock:
   =============================
   "Thread-0":
     waiting to lock monitor 0x00007f99c3613f00 (object 0x000000070fe1d600, a Deadlock$Friend),
     which is held by "Thread-1"
   "Thread-1":
     waiting to lock monitor 0x00007f99c3613d00 (object 0x000000070fe1d5c0, a Deadlock$Friend),
     which is held by "Thread-0"
   
   Java stack information for the threads listed above:
   ===================================================
   "Thread-0":
   	at Deadlock$Friend.bowBack(Deadlock.java:21)
   	- waiting to lock <0x000000070fe1d600> (a Deadlock$Friend)
   	at Deadlock$Friend.bow(Deadlock.java:17)
   	- locked <0x000000070fe1d5c0> (a Deadlock$Friend)
   	at Deadlock$1.run(Deadlock.java:32)
   	at java.lang.Thread.run(java.base@11.0.12/Thread.java:834)
   "Thread-1":
   	at Deadlock$Friend.bowBack(Deadlock.java:21)
   	- waiting to lock <0x000000070fe1d5c0> (a Deadlock$Friend)
   	at Deadlock$Friend.bow(Deadlock.java:17)
   	- locked <0x000000070fe1d600> (a Deadlock$Friend)
   	at Deadlock$2.run(Deadlock.java:36)
   	at java.lang.Thread.run(java.base@11.0.12/Thread.java:834)
   
   Found 1 deadlock.
   ```

   - 데드록이 존재하는 경우만 출력

   

5. 힙 영역의 사용 현황

   - GC 방식에 따라 조금씩 다르다.

   ```bash
   Heap
   # Eden 영역
    garbage-first heap   total 589824K, used 234200K [0x0000000700000000, 0x0000000800000000)
     region size 1024K, 230 young (235520K), 1 survivors (1024K)
    Metaspace       used 4938K, capacity 5035K, committed 5120K, reserved 1056768K
     class space    used 416K, capacity 443K, committed 512K, reserved 1048576K
   ```

   - 다음의 내용으로 이루어져 있다.
     - 영역 이름
     - 영역에 할당된 메모리 크기
     - 메모리 사용량(혹은 비율)
     - 메모리의 영역 정보(range)



그 외 jconsole 같은 프로그램으로 단면을 보는 방법은 따로 다뤄보도록 한다.



## 참고

- 자바 트러블 슈팅, 이상민
