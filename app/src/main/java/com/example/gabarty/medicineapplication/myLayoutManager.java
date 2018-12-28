package com.example.gabarty.medicineapplication;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

public class myLayoutManager extends LinearLayoutManager {

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    public myLayoutManager(Context context) {
        super(context);
    }

    public myLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public myLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
