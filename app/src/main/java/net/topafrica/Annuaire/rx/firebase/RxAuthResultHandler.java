package net.topafrica.Annuaire.rx.firebase;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import rx.Subscriber;

public class RxAuthResultHandler implements Firebase.AuthResultHandler {
    private final Subscriber<AuthData> subscriber;

    public RxAuthResultHandler(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void onAuthenticationError(FirebaseError firebaseError) {
        subscriber.onError(firebaseError.toException());
    }

    @Override
    public void onAuthenticated(AuthData authData) {
        subscriber.onNext(authData);
        subscriber.onCompleted();
    }
}