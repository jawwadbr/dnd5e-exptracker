package com.jawbr.dnd5e.exptracker.util;

import com.jawbr.dnd5e.exptracker.exception.IllegalParameterException;

public class CheckRequestConfirmation {

    public static void checkConfirmation(boolean isConfirmed) {
        if(!isConfirmed)
            throw new IllegalParameterException("Action requires confirmation. Please include 'isConfirmed=true' in your request.");
    }
}
