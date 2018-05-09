package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.preferences.MyPreferences;

public class MainActivity extends AppCompatActivity {
    TextView firebaseTokenTextView;
    EditText topicEditText;
    AppCompatButton reloadButton;
    AppCompatButton copyButton;
    AppCompatButton subscribeButton;
    View topicContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reloadButton = findViewById(R.id.reload_button);
        copyButton = findViewById(R.id.copy_button);
        subscribeButton = findViewById(R.id.subscribe_button);
        topicEditText = findViewById(R.id.topic);
        topicContainer = findViewById(R.id.topic_container);

        setFirebaseTokenInView();

        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFirebaseTokenInView();
            }
        });

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyTokenToClipboard();
            }
        });

        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subscribeToTopic();
            }
        });
    }

    private void setFirebaseTokenInView() {
        String firebaseToken = MyPreferences.getFirebaseToken(this);

        if(firebaseToken != null){
            firebaseTokenTextView = findViewById(R.id.firebaseToken);
            firebaseTokenTextView.setText(firebaseToken);
            reloadButton.setVisibility(View.GONE);
            copyButton.setVisibility(View.VISIBLE);
            topicContainer.setVisibility(View.VISIBLE);
        } else {
            copyButton.setVisibility(View.GONE);
            topicContainer.setVisibility(View.GONE);
            reloadButton.setVisibility(View.VISIBLE);
        }
    }

    private void subscribeToTopic() {
        String topic = topicEditText.getText().toString();
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
        Toast.makeText(this, "Subscripto a " + topic, Toast.LENGTH_SHORT).show();
        topicEditText.setText("");
    }

    public void copyTokenToClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Firebase token", firebaseTokenTextView.getText());
        clipboard.setPrimaryClip(clip);
    }
}
