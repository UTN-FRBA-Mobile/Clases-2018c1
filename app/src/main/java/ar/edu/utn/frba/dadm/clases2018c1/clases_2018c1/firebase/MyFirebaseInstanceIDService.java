package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.preferences.MyPreferences;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        MyPreferences.setFirebaseToken(this, FirebaseInstanceId.getInstance().getToken());
    }
}
