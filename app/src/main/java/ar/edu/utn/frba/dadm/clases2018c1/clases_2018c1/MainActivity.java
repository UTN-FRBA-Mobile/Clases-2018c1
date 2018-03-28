package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1;

import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener, AddFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Si la Activity ya tiene un estado de instancia guardado, recrear el fragment puede terminar con dos fragments
        //solapados en el mismo container  (Probar comentar este IF y correr la app, abrir el segundo fragment,
        //rotar la pantalla y ver cómo el primer y el segundo Fragment se solapan)
        if(savedInstanceState == null){
            MainFragment fragment = new MainFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment).commit();
        }
    }

    @Override
    public void onCreateNew() {
        AddFragment addFragment = AddFragment.newInstance(getString(R.string.write_hello));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, addFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onNewCreated(String text) {
        getSupportFragmentManager().popBackStackImmediate();

        Snackbar.make(findViewById(R.id.fragment_container), text, Snackbar.LENGTH_LONG).show();
    }
}
