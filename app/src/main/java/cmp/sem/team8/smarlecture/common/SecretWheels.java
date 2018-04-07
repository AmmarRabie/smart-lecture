package cmp.sem.team8.smarlecture.common;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.LinearLayout;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.wheel.OnWheelChangedListener;
import cmp.sem.team8.smarlecture.common.wheel.OnWheelScrollListener;
import cmp.sem.team8.smarlecture.common.wheel.WheelView;
import cmp.sem.team8.smarlecture.common.wheel.adapters.NumericWheelAdapter;

/**
 * Created by AmmarRabie on 06/04/2018.
 */

public class SecretWheels extends LinearLayout {

    public static final int SECRET_LONG = 4;

    View rootView;
    private WheelView[] wheelsViews;

    // Wheel scrolled flag
    private boolean[] wheelScrolled = {false, false, false, false};
    private OnSecretChangeListener mOnSecretChangeListener = null;
    // Wheel scrolled listener
    OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
        public void onScrollingStarted(WheelView wheel) {
            wheelScrolled[getIndex(wheel)] = true;
        }

        public void onScrollingFinished(WheelView wheel) {
            wheelScrolled[getIndex(wheel)] = false;
            updateListener();
        }
    };
    // Wheel changed listener
    private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            if (!wheelScrolled[getIndex(wheel)]) {
                updateListener();
            }
        }
    };

    public SecretWheels(Context context) {
        super(context);
        init(context);
    }

    public SecretWheels(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.secret_wheels, this);
        wheelsViews = new WheelView[SECRET_LONG];
        wheelsViews[0] = rootView.findViewById(R.id.secretWheels_s1);
        wheelsViews[1] = rootView.findViewById(R.id.secretWheels_s2);
        wheelsViews[2] = rootView.findViewById(R.id.secretWheels_s3);
        wheelsViews[3] = rootView.findViewById(R.id.secretWheels_s4);

        for (WheelView wheel : wheelsViews) {
            initWheel(wheel);
        }

    }

    private void initWheel(final WheelView wheel) {
        wheel.setViewAdapter(new NumericWheelAdapter(getContext(), 0, 9));
//        wheel.setCurrentItem((int) (Math.random() * 10));
        wheel.setCurrentItem(0);

        wheel.addChangingListener(changedListener);
        wheel.addScrollingListener(scrolledListener);
        wheel.setCyclic(true);
        wheel.setInterpolator(new AnticipateOvershootInterpolator());
    }

    private void mixWheel(WheelView wheel, int time) {
        wheel.scroll(-25 + (int) (Math.random() * 50), time);
//        wheel.setCurrentItem(5,true);
    }

    public void setOnSecretChange(OnSecretChangeListener onSecretChange) {
        mOnSecretChangeListener = onSecretChange;
    }

    public void mixWheels(int time) {
        for (WheelView wheel : wheelsViews) {
            mixWheel(wheel, time);
        }
    }

    /**
     * Returns wheel by Id
     *
     * @param index the index of the wheel (from 0 : 3)
     * @return the wheel with passed index
     */
    public WheelView getWheel(int index) {
        if (index >= 4 || index < 0)
            return null;
        return wheelsViews[index];
    }

    public int getIndex(WheelView wheel) {
        for (int i = 0; i < wheelsViews.length; i++) {
            if (wheel.equals(wheelsViews[i])) {
                return i;
            }
        }
        return -1;
    }

    private void updateListener() {
        if (mOnSecretChangeListener == null)
            return;
        final int v1 = getWheel(0).getCurrentItem();
        final int v2 = getWheel(1).getCurrentItem();
        final int v3 = getWheel(2).getCurrentItem();
        final int v4 = getWheel(3).getCurrentItem();
        final String asString = String.valueOf(v1)
                + String.valueOf(v2)
                + String.valueOf(v3)
                + String.valueOf(v4);
        mOnSecretChangeListener.onSecretChange(v1, v2, v3, v4, asString);
    }

    public String getSecret() {
        final String v1 = String.valueOf(getWheel(0).getCurrentItem());
        final String v2 = String.valueOf(getWheel(1).getCurrentItem());
        final String v3 = String.valueOf(getWheel(2).getCurrentItem());
        final String v4 = String.valueOf(getWheel(3).getCurrentItem());
        return v1 + v2 + v3 + v4;
    }

    public interface OnSecretChangeListener {
        void onSecretChange(int v1, int v2, int v3, int v4, String asString);
    }
}