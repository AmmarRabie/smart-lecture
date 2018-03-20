package cmp.sem.team8.smarlecture.common.util;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cmp.sem.team8.smarlecture.R;

/**
 * Created by ramym on 3/17/2018.
 */

public class DialogFragment extends android.app.DialogFragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog,null);
    }
}
