package chapter_01;

import io.reactivex.Observable;

public class MarbleExample01 {
    public static void main(String[] args) {
        Observable.just(1, 25, 9, 15, 7, 30)
                .filter(x -> x > 10)
                .subscribe(x -> System.out.println(x));
    }
}
