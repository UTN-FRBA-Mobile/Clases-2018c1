package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        List<Model> items = new ArrayList<>();
        items.add(new Model(android.R.drawable.ic_dialog_alert, android.R.string.paste));
        items.add(new Model(android.R.drawable.ic_dialog_dialer, android.R.string.copy));
        items.add(new Model(android.R.drawable.ic_dialog_email, android.R.string.copyUrl));
        items.add(new Model(android.R.drawable.ic_dialog_map, android.R.string.cancel));
        items.add(new Model(android.R.drawable.ic_dialog_info, android.R.string.cut));
        items.add(new Model(android.R.drawable.ic_dialog_info, android.R.string.cut));
        items.add(new Model(android.R.drawable.ic_dialog_info, android.R.string.cut));
        items.add(new Model(android.R.drawable.ic_dialog_info, android.R.string.cut));
        items.add(new Model(android.R.drawable.ic_dialog_info, android.R.string.cut));
        items.add(new Model(android.R.drawable.ic_dialog_info, android.R.string.cut));
        items.add(new Model(android.R.drawable.ic_dialog_info, android.R.string.cut));
        items.add(new Model(android.R.drawable.ic_dialog_info, android.R.string.cut));
        items.add(new Model(android.R.drawable.ic_dialog_info, android.R.string.cut));
        items.add(new Model(android.R.drawable.ic_dialog_alert, android.R.string.paste));
        items.add(new Model(android.R.drawable.ic_dialog_dialer, android.R.string.copy));
        items.add(new Model(android.R.drawable.ic_dialog_email, android.R.string.copyUrl));
        items.add(new Model(android.R.drawable.ic_dialog_alert, android.R.string.paste));
        items.add(new Model(android.R.drawable.ic_dialog_dialer, android.R.string.copy));
        items.add(new Model(android.R.drawable.ic_dialog_email, android.R.string.copyUrl));

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ModelAdapter(items));
    }
}
