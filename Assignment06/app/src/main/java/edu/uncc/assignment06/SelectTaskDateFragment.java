package edu.uncc.assignment06;

import static java.time.LocalDateTime.*;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.time.LocalDateTime;

import edu.uncc.assignment06.databinding.FragmentSelectTaskDateBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectTaskDateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectTaskDateFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private static final String ARG_PARAM_TASK = "ARG_PARAM_TASK";

    // TODO: Rename and change types of parameters

    private Task task;

    public SelectTaskDateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectTaskDateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectTaskDateFragment newInstance(Task task) {
        SelectTaskDateFragment fragment = new SelectTaskDateFragment();
        Bundle args = new Bundle();

        args.putSerializable(ARG_PARAM_TASK, task);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            task = (Task) getArguments().getSerializable(ARG_PARAM_TASK);
        }
    }

    FragmentSelectTaskDateBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSelectTaskDateBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Select Task Date");

        Calendar cal = Calendar.getInstance();
        long date = cal.getTime().getTime();

        binding.calendarView.setMinDate(date);
        binding.calendarView.setDate(date);

        if (!(task.getDate().equalsIgnoreCase("n/a")))
        {
            try {
                updateDate();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                STDListener.onDateCancelPressed();
            }
        });

        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                Calendar cal = Calendar.getInstance();
                cal.set(year, month, dayOfMonth);
                long date = cal.getTime().getTime();
                binding.calendarView.setDate(date);
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("SelectTaskDateFragment", "onClick: " + gatherDate());

                task.setDate(gatherDate());

                STDListener.onDateSubmitPressed(task);
            }
        });
    }

    private String gatherDate()
    {
        long date = binding.calendarView.getDate();

        Calendar cal = Calendar.getInstance();

        cal.setTimeInMillis(date);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        return sdf.format(cal.getTime());
    }

    private void updateDate() throws ParseException {
        String dateString = task.getDate();

        Calendar cal = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        cal.setTime(Objects.requireNonNull(sdf.parse(dateString)));

        binding.calendarView.setDate(cal.getTime().getTime());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        STDListener = (SelectTaskDateListener) context;
    }

    SelectTaskDateListener STDListener;
    public interface SelectTaskDateListener {
        void onDateCancelPressed();
        void onDateSubmitPressed(Task task);
    }
}