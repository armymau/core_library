package core.view;

import android.content.Context;
import android.view.View;
import android.widget.SeekBar;

public class CoreComponents {

    public static SeekBar createSeekBarRadius(Context context) {
        SeekBar seekBar = new SeekBar(context);
        seekBar.setMax(100);
        seekBar.setVisibility(View.INVISIBLE);
        return seekBar;
    }
}
