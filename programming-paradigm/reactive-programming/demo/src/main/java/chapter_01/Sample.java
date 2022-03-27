package chapter_01;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class Sample {
    public static void main(String[] args) throws InterruptedException {
        Observable.just(100, 200, 300, 400, 500)
                .doOnNext(data -> System.out.println(getThreadName() + " : #doOnNext() : " + data))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .filter(num -> {
                    System.out.println(getThreadName() + " : #filter() : " + num);
                    return num > 300;
                })
                .subscribe(num -> System.out.println(getThreadName() + " : result : " + num))
        ;

        Thread.sleep(500);
    }

    public static String getThreadName() {
        return Thread.currentThread().getName();
    }
}
