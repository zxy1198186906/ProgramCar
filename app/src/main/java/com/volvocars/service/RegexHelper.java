package com.volvocars.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.volvocars.model.CaseFactory.*;

public class RegexHelper {
    private static final String NUM_PATTERN = "(\\D*)(\\d+)(.*)";
    public static final int NUM_LESS = 0;
    public static final int NUM_MORE = 1;
    public static final int NUM_ILLEGAL = -1;

    public static int regexFindNum(String source){
        Pattern temp = Pattern.compile(NUM_PATTERN);
        Matcher tempMatcher = temp.matcher(source);
        if (tempMatcher.find())
            return Integer.parseInt(tempMatcher.group(2));
        return -1;
    }

    public static boolean regexFindState(String source){
        if (Pattern.matches(".*on.*", source))
            return true;
        else
            return false;
    }

    public static int regexFindType(String source){
        if (Pattern.matches(".*time.*", source))
            return HANDLER_TIME;
        else if (Pattern.matches(".*temperature.*", source))
            return HANDLER_TEMPERATURE;
        else if (Pattern.matches(".*network.*", source))
            return HANDLER_NET;
        else if (Pattern.matches(".*clock.*", source))
            return HANDLER_CLOCK;
        else if (Pattern.matches(".*music.*", source))
            return HANDLER_MUSIC;
        else if (Pattern.matches(".*gps.*", source))
            return HANDLER_GPS;
        return -1;
    }

    public static int regexFindEqual(String source){
        if (Pattern.matches(".*earlier.*", source))
            return NUM_LESS;
        if (Pattern.matches(".*lower.*", source))
            return NUM_LESS;
        if (Pattern.matches(".*off.*", source))
            return NUM_LESS;
        if (Pattern.matches(".*later.*", source))
            return NUM_MORE;
        if (Pattern.matches(".*higher.*", source))
            return NUM_MORE;
        if (Pattern.matches(".*on.*", source))
            return NUM_MORE;
        return NUM_ILLEGAL;
    }
}
