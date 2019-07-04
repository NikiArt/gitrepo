package ru.geekbrains.gb_android_libraries;

import android.text.TextUtils;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function4;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subscribers.DisposableSubscriber;
import timber.log.Timber;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RxStuff {

    private Observer<String> getStringObserver() {
        return new Observer<String>() {
            Disposable subscription;

            @Override
            public void onSubscribe(Disposable d) {
                subscription = d;
                Timber.d("onSubscribe");
            }

            @Override
            public void onNext(String s) {
                Timber.d("onNext: " + s);
            }

            @Override
            public void onError(Throwable e) {
                Timber.d("onError: " + e);
            }

            @Override
            public void onComplete() {
                Timber.d("onComplete");
            }
        };
    }

    private Observer<Long> getLongObserver() {
        return new Observer<Long>() {
            Disposable subscription;

            @Override
            public void onSubscribe(Disposable d) {
                subscription = d;
                Timber.d("onSubscribe");
            }

            @Override
            public void onNext(Long s) {
                Timber.d("onNext: " + s);
            }

            @Override
            public void onError(Throwable e) {
                Timber.d("onError: " + e);
            }

            @Override
            public void onComplete() {
                Timber.d("onComplete");
            }
        };
    }

    private Observer<List<String>> getListStringObserver() {
        return new Observer<List<String>>() {
            Disposable subscription;

            @Override
            public void onSubscribe(Disposable d) {
                subscription = d;
                Timber.d("onSubscribe");
            }

            @Override
            public void onNext(List<String> l) {
                Timber.d("onNext: " + TextUtils.join(",", l));
            }

            @Override
            public void onError(Throwable e) {
                Timber.d("onError: " + e);
            }

            @Override
            public void onComplete() {
                Timber.d("onComplete");
            }
        };
    }


    public void run() {
//        just();
//        fromIterable();
//        interval();
//        take();
//        skip();
//        map();
//        distinct();
//        filter();
//        merge();
//        zip();
//        flatMap();
        switchMapVsFlatmap();
    }

    public void just() {
        Observable<String> justObservable = Observable
                .just("just0", "just1", "just2");

        justObservable.subscribe(getStringObserver());
    }

    public void fromIterable() {
        Observable<String> fromIterableObservable = Observable
                .fromIterable(Arrays.asList("from0", "from1", "from2", "from3"));

        fromIterableObservable.subscribe(getStringObserver());
    }

    public void interval() {
        Observable<Long> fromIterableObservable = Observable
                .interval(1, TimeUnit.SECONDS);

        fromIterableObservable.subscribe(getLongObserver());
    }

    public void take() {
        Observable<String> takeObservable = Observable
                .fromIterable(Arrays.asList("take0", "take1", "take2", "take3"))
                .take(2);

        takeObservable.subscribe(getStringObserver());
    }


    public void skip() {
        Observable<String> skipObservable = Observable
                .fromIterable(Arrays.asList("skip0", "skip1", "skip2", "skip3"))
                .skip(2);

        skipObservable.subscribe(getStringObserver());
    }

    public void map() {
        Observable<String> mapObservable = Observable
                .fromIterable(Arrays.asList("map0", "map1", "map2", "map3"))
                .map(s -> "_" + s);

        mapObservable.subscribe(getStringObserver());
    }

    public void distinct() {
        Observable<String> distinctObservable = Observable
                .fromIterable(Arrays.asList("distinct", "distinct0", "distinct1", "distinct2", "distinct3"))
                .map(s -> s.contains("0") ? s : s + "0")
                .distinct();

        distinctObservable.subscribe(getStringObserver());
    }

    public void filter() {
        Observable<String> filterObservable = Observable
                .fromIterable(Arrays.asList("_filter", "filter", "filter10", "filter12", "filter2", "filter31"))
                .filter(s -> !s.contains("1"));
        filterObservable.subscribe(getStringObserver());
    }

    public void merge() {
        Observable<String> mergeObservable = Observable
                .fromIterable(Arrays.asList("_merge", "merge0", "merge1", "merge2", "merge3", "merge4"))
                .skip(1)
                .mergeWith(Observable.fromIterable(Arrays.asList("_merge", "merge2", "merge3", "merge4", "merge5", "merge6")))
                .distinct();
        mergeObservable.subscribe(getStringObserver());

    }

    public void zip() {
        Observable<String> observableOne = Observable.just("zip0");
        Observable<String> observableTwo = Observable.just("zip1");
        Observable<String> observableThree = Observable.just("zip2");
        Observable<String> observableFour = Observable.just("zip3");

        Observable<List<String>> zipObservable = Observable.zip(observableOne, observableTwo, observableThree, observableFour, new Function4<String, String, String, String, List<String>>() {
            @Override
            public List<String> apply(String s, String s2, String s3, String s4) throws Exception {
                return Arrays.asList(s, s2, s3, s4);
            }
        });

        zipObservable.subscribe(getListStringObserver());
    }

    public void flatMap() {
        Observable<String> observableCOne = Observable.just("flatMap0");
        Observable<String> observableCTwo = Observable.just("flatMap1");
        Observable<String> observableCThree = Observable.just("flatMap2");
        Observable<String> observableCFour = Observable.just("flatMap3");

        Observable<String> flatMapObservable = Observable.zip(observableCOne, observableCTwo, observableCThree, observableCFour, new Function4<String, String, String, String, List<String>>() {
            @Override
            public List<String> apply(String s, String s2, String s3, String s4) throws Exception {
                return Arrays.asList(s, s2, s3, s4);
            }
        }).flatMap((Function<List<String>, ObservableSource<String>>) strings -> Observable.fromIterable(strings));

        flatMapObservable.subscribe(getStringObserver());
    }

    void switchMapVsFlatmap() {
        final List<String> items = Arrays.asList("a", "b", "c", "d", "e", "f");
        final TestScheduler scheduler = new TestScheduler();

        Observable.fromIterable(items)
                .flatMap(s -> {
                    final int delay = new Random().nextInt(10);
                    return Observable.just(s + "x")
                            .delay(delay, TimeUnit.SECONDS, scheduler);
                })
                .toList()
                .doOnSuccess(list -> {
                    Timber.d("flatMap : " + list);
                })
                .subscribe();
        scheduler.advanceTimeBy(1, TimeUnit.MINUTES);

        Observable.fromIterable(items)
                .switchMap(s -> {
                    final int delay = new Random().nextInt(10);
                    return Observable.just(s + "x")
                            .delay(delay, TimeUnit.SECONDS, scheduler);
                })
                .toList()
                .doOnSuccess(list -> {
                    Timber.d("switchMap : " + list);
                })
                .subscribe();
        scheduler.advanceTimeBy(1, TimeUnit.MINUTES);
    }


    public void flowable() {
        Flowable.range(1, 1_000_000)
                .onBackpressureDrop()
                .observeOn(Schedulers.computation(), false, 1)
                .subscribe(num -> {
                    Timber.d(num.toString());
                }, Throwable::printStackTrace);

//                Flowable.range(1, 1000000)
//                .observeOn(Schedulers.computation())
//                .subscribe(new DisposableSubscriber<Integer>() {
//                    @Override
//                    public void onStart() {
//                        request(1);
//                    }
//
//                    public void onNext(Integer num) {
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        request(1);
//                        Timber.d("onNext: " + num);
//                    }
//
//                    @Override
//                    public void onError(Throwable ex) {
//                        Timber.e(ex);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Timber.d("onComplete");
//                    }
//                });
    }


}
