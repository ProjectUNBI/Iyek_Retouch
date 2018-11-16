package com.unbi.iyekretouch;

import safety.com.br.android_shake_detector.core.ShakeDetector;
import safety.com.br.android_shake_detector.core.ShakeOptions;

public class shakeOptionsObj {
    private ShakeDetector shakeDetector;

    public shakeOptionsObj(float sensitivity){


        ShakeOptions options = new ShakeOptions()
                .background(true)
                .interval(500)
                .shakeCount(1)
                .sensibility(sensitivity);
        setShakeDetector(new ShakeDetector(options));

    }


    public ShakeDetector getShakeDetector() {
        return shakeDetector;
    }

    public void setShakeDetector(ShakeDetector shakeDetector) {
        this.shakeDetector = shakeDetector;
    }
}
