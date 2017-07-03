package io.hasura.drive_android.ui.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.hasura.drive_android.R;
import io.hasura.drive_android.models.HasuraFile;
import io.hasura.drive_android.utils.DateManager;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     FileOptionBottomSheet.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 * <p>You activity (or fragment) needs to implement {@link Listener}.</p>
 */
public class FileOptionBottomSheet extends BottomSheetDialogFragment {

    private static final String HASURA_FILE_KEY = "HasuraFileKey";
    @BindView(R.id.editButton)
    ImageView editButton;
    private Listener listener;
    private HasuraFile hasuraFile;

    @BindView(R.id.fileName)
    TextView fileName;
    @BindView(R.id.fileNumber)
    TextView fileNumber;
    @BindView(R.id.fileExpiry)
    TextView fileExpiry;
    @BindView(R.id.editInfoCard)
    CardView editInfoCard;
    @BindView(R.id.deleteFileCard)
    CardView deleteFileCard;
    Unbinder unbinder;

    // TODO: Customize parameters
    public static FileOptionBottomSheet newInstance(HasuraFile file) {
        final FileOptionBottomSheet fragment = new FileOptionBottomSheet();
        final Bundle args = new Bundle();
        args.putParcelable(HASURA_FILE_KEY, file);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_option, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        hasuraFile = getArguments().getParcelable(HASURA_FILE_KEY);

        if (hasuraFile == null) {
            Toast.makeText(getContext(), "Please try again", Toast.LENGTH_SHORT).show();
            return;
        }

        fileName.setText(hasuraFile.getName());
        if (hasuraFile.getFile_number() == null)
            hasuraFile.setFile_number("");
        fileNumber.setText(!hasuraFile.getFile_number().isEmpty() ? hasuraFile.getFile_number() : "Not Provided");
        fileExpiry.setText(hasuraFile.getFile_expiry() != null ? DateManager.getFormattedExpiryDate(hasuraFile.getFile_expiry()) : "Not Provided");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            listener = (Listener) parent;
        } else {
            listener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.editInfoCard)
    public void onEditInfoCardClicked() {
        listener.onEditFileClicked(hasuraFile);
        dismiss();
    }

    @OnClick(R.id.deleteFileCard)
    public void onDeleteFileCardClicked() {
        listener.onDeleteFileClicked(hasuraFile);
        dismiss();
    }

    @OnClick(R.id.editButton)
    public void onEditButtonClicked() {
        listener.onEditFileClicked(hasuraFile);
        dismiss();
    }

    public interface Listener {
        void onEditFileClicked(HasuraFile file);

        void onDeleteFileClicked(HasuraFile file);
    }
}
