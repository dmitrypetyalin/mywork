package com.petsoft.task1.exceptions;

import java.io.IOException;

/**
 * Created by PetSoft on 02.08.2019
 */

public class DataOverloadException extends IOException {

    public DataOverloadException() {
    }

    public DataOverloadException(String message) {
        super(message);
    }
}
