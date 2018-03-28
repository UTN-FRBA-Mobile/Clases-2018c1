package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class AddFragment extends Fragment {
    private static final String ARG_LABEL = "label";

    private String label;

    private OnFragmentInteractionListener mListener;

    public AddFragment() {
        //Constructor vacio y publico requerido
    }

    public static AddFragment newInstance(String label) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LABEL, label);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            label = getArguments().getString(ARG_LABEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View mainView = inflater.inflate(R.layout.fragment_add, container, false);

        TextView labelView = mainView.findViewById(R.id.label);
        final EditText editText = mainView.findViewById(R.id.input);
        FloatingActionButton fab = mainView.findViewById(R.id.plus_button);

        labelView.setText(label);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(editText.getText().toString());
            }
        });

        return mainView;
    }

    public void onButtonPressed(String input) {
        if (mListener != null) {
            mListener.onNewCreated(input);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onNewCreated(String param);
    }
}
