package foodvisor.com.foodvisor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BookmarkFragment extends Fragment {
    private static final String TAG = BookmarkFragment.class.getSimpleName();
    public static BookmarkFragment newInstance() {
        BookmarkFragment fragment = new BookmarkFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_two, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView action = (TextView) view.findViewById(R.id.action_text_demo);

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.viewpager, RestaurantDetails.newInstance());
                transaction.commit();
            }
        });

    }

    }
