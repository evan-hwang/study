package chapter_02;

import io.reactivex.Flowable;
import io.reactivex.Observable;

public class ColdPublisherExample {
    public static void main(String[] args) {
        Flowable<Integer> flowable = Flowable.just(10, 20, 30, 40, 50);

        flowable.subscribe(data -> System.out.println("구독자1 : " + data));
        flowable.subscribe(data -> System.out.println("구독자2 : " + data));
    }
}
