package com.ayan.redditclone.utils;

import java.util.concurrent.TimeUnit;

public class Utils {
    public static String getPostInterval(long postMillis) {
        // calculate time of posting
        Long timeDiffInMillis = (System.currentTimeMillis() / 1000) - postMillis;

        Long timeDiff = TimeUnit.MILLISECONDS.toDays(timeDiffInMillis);
        String bulletDot = " \u2022 ";
        StringBuffer timeUnit = new StringBuffer().append('d');

        if (timeDiff == 0) {
            timeDiff = TimeUnit.MILLISECONDS.toHours(timeDiffInMillis);
            timeUnit.setCharAt(0, 'H');
            if (timeDiff == 0) {
                timeDiff = TimeUnit.MILLISECONDS.toMinutes(timeDiffInMillis);
                timeUnit.setCharAt(0, 'm');
                if (timeDiff == 0) {
                    timeDiff = TimeUnit.MILLISECONDS.toSeconds(timeDiffInMillis);
                    timeUnit.setCharAt(0, 's');
                }
            }
        }

        return (timeDiff.toString() + " " + timeUnit);
    }

    public static String getCommentVoteString(Float value, String defaultValue) {
        String voteStr = value.toString();

        if (value <= 0) {
            voteStr = defaultValue;
        } else if (value >= 1000) {
            value = value / 1000;
            voteStr = String.format("%.1f", value) + " k";
        } else {
            voteStr = String.format("%.0f", value);
        }
        return voteStr;
    }
}
