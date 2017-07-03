package io.hasura.drive_android.ui.launcher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.hasura.drive_android.R;


public class OnBoardingFragment extends Fragment {

    private static final String TITLE_KEY = "TitleKey";
    private static final String DESCRIPTION_KEY = "DescriptionKey";
    private static final String IMAGE_RESOURSE_KEY = "ImageResourceKey";

    String titleString;
    String descriptionString;
    int imageResourceId;

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.description)
    TextView description;
    Unbinder unbinder;

    public static OnBoardingFragment newInstance(String title, String description, int imageResourceId) {
        OnBoardingFragment fragment = new OnBoardingFragment();
        Bundle args = new Bundle();
        args.putString(TITLE_KEY, title);
        args.putString(DESCRIPTION_KEY, description);
        args.putInt(IMAGE_RESOURSE_KEY, imageResourceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.titleString = getArguments().getString(TITLE_KEY);
            this.descriptionString = getArguments().getString(DESCRIPTION_KEY);
            this.imageResourceId = getArguments().getInt(IMAGE_RESOURSE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_on_boarding, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        title.setText(titleString);
        description.setText(descriptionString);
        image.setImageResource(imageResourceId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
