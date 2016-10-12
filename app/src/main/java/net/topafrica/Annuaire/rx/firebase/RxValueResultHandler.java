package net.topafrica.Annuaire.rx.firebase;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import rx.Subscriber;


/**
 * Created by ericbasendra on 08/10/16.
 */

public class RxValueResultHandler<T> implements Firebase.ValueResultHandler<T> {
    private final Subscriber<? super T> subscriber;

    public RxValueResultHandler(Subscriber<? super T> subscriber){
        this.subscriber = subscriber;
    }
    @Override
    public void onSuccess(T t) {
        subscriber.onNext(t);
        subscriber.onCompleted();
    }

    @Override
    public void onError(FirebaseError firebaseError) {
        subscriber.onError(firebaseError.toException());
    }
}
