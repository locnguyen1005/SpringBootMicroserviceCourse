package com.example.commonservice.utils;

import org.springframework.beans.factory.annotation.Configurable;

import lombok.Data;

@Configurable
@Data
public class ConstantCommon {
    public static final String LESSION_ACCOUNT = "LESSION_ACCOUNT_SERVICE";
    public static final String EMAIL = "EMAIL_SERVICE";
    public static final String ACCOUNT = "ACCOUNT_SERVICE";
    public static final String CreatedProduct = "CreatedProduct";
    public static final String NOTIFICATION ="NOTIFICATION_SERVICE";

}
