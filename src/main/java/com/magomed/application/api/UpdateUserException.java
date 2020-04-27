package com.magomed.application.api;

import java.sql.SQLException;

public class UpdateUserException extends RuntimeException {
    public UpdateUserException(String countryIsWrong) {
        super(countryIsWrong);
    }

    public UpdateUserException(String message, SQLException e) {
        super(message, e);
    }
}
