package iit.dentacare;

import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Thiloshon on 22-Jan-17.
 */

public class Timer extends Thread {
    ProgressBar p;
    int num;

    public void run() {
        p.setProgress(num);
        System.out.println("prog is " + num);


    }

    public Timer(ProgressBar p, int num) {
        this.p = p;
        this.num = num;
    }
}
