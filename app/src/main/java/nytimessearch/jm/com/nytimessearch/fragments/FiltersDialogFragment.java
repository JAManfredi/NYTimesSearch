package nytimessearch.jm.com.nytimessearch.fragments;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import nytimessearch.jm.com.nytimessearch.R;
import nytimessearch.jm.com.nytimessearch.databinding.FragmentFiltersBinding;
import nytimessearch.jm.com.nytimessearch.models.Filters;

/**
 * Created by Jared12 on 3/19/17.
 */

public class FiltersDialogFragment extends AppCompatDialogFragment {
    private FragmentFiltersBinding binding;
    private Filters mFilters;
    private FiltersDialogListener listener;

    public FiltersDialogFragment() {
        super();
        this.listener = null;
    }

    public void setFiltersDialogListener(FiltersDialogListener listener) {
        this.listener = listener;
    }

    public static FiltersDialogFragment newInstance(Filters filters) {
        FiltersDialogFragment mDialogFragment = new FiltersDialogFragment();
        mDialogFragment.setStyle(AppCompatDialogFragment.STYLE_NO_TITLE, 0);
        mDialogFragment.mFilters = filters;
        return mDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filters, container, false);
        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClose();
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadFilters();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    private void loadFilters() {
        if (mFilters != null) {
            if (mFilters.getDeskValues() != null) {
                for (String deskValue : mFilters.getDeskValues()) {
                    if (deskValue.equals("Arts")) {
                        binding.cbArts.setChecked(true);
                    }
                    if (deskValue.equals("Fashion")) {
                        binding.cbFashion.setChecked(true);
                    }
                    if (deskValue.equals("Sports")) {
                        binding.cbSports.setChecked(true);
                    }
                    if (deskValue.equals("Style")) {
                        binding.cbStyle.setChecked(true);
                    }
                }
            }

            Date beginDate;
            if (mFilters.getBeginDate() != null) {
                beginDate = mFilters.getBeginDate();
            } else {
                beginDate = new Date(0);
            }
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(beginDate.getTime());
            binding.dpBeginDate.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

            if (mFilters.getSortOrder() != null) {
                switch (mFilters.getSortOrder()) {
                    case "Newest":
                        binding.spSortOrder.setSelection(1);
                        break;
                    case "Oldest":
                        binding.spSortOrder.setSelection(2);
                        break;
                    default:
                        binding.spSortOrder.setSelection(0);
                        break;
                }
            }
        } else {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(new Date(0).getTime());
            binding.dpBeginDate.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        }
    }

    public void onClose() {
        dismiss();
    }

    public void onSave() {
        ArrayList<String> deskValueArray = new ArrayList<>();
        if (binding.cbArts.isChecked()) {
            deskValueArray.add("Arts");
        }
        if (binding.cbFashion.isChecked()) {
            deskValueArray.add("Fashion");
        }
        if (binding.cbSports.isChecked()) {
            deskValueArray.add("Sports");
        }
        if (binding.cbStyle.isChecked()) {
            deskValueArray.add("Style");
        }

        int day = binding.dpBeginDate.getDayOfMonth();
        int month = binding.dpBeginDate.getMonth();
        int year = binding.dpBeginDate.getYear();
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        Date beginDate = new Date(c.getTimeInMillis());

        String selectedSortOrder = (String)binding.spSortOrder.getSelectedItem();
        Filters returnFilters = new Filters(beginDate, selectedSortOrder, deskValueArray);

        if (listener != null) {
            listener.onFiltersSaved(returnFilters);
        }

        dismiss();
    }

    public interface FiltersDialogListener {
        public void onFiltersSaved(Filters filters);
    }
}
