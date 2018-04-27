package com.example.vuphu.app.user.catogries;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.vuphu.app.R;
import com.example.vuphu.app.user.adapter.CatoAdapter;


public class CatogriesFragment extends Fragment {


    private TabLayout tabCato ;
    private ViewPager viewPagerCato;
    CatoAdapter adapter;


    public CatogriesFragment() {

    }
    public static CatogriesFragment newInstance() {
        CatogriesFragment fragment = new CatogriesFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_catogries, container, false);

        tabCato = v.findViewById(R.id.tab_layout_cato);
        viewPagerCato = v.findViewById(R.id.view_pager);

        adapter = new CatoAdapter(v.getContext(), getFragmentManager());

        viewPagerCato.setAdapter(adapter);

        tabCato.setupWithViewPager(viewPagerCato);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPagerCato.setAdapter(adapter);
        tabCato.setupWithViewPager(viewPagerCato);

    }
}

