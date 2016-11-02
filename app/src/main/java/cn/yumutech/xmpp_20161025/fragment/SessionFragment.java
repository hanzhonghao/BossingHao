package cn.yumutech.xmpp_20161025.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.yumutech.xmpp_20161025.R;

/**
 * @描述 会话的Fragment
 * A simple {@link Fragment} subclass.
 */
public class SessionFragment extends Fragment {
    private static SessionFragment sessionFragment;

    public SessionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_session, container, false);
    }

    public static SessionFragment getInstance() {
        if (sessionFragment == null){
            sessionFragment = new SessionFragment();
        }
        return sessionFragment;
    }
}
