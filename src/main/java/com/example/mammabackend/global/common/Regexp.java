package com.example.mammabackend.global.common;

public class Regexp {

    public static final String REGEXP_PASSWORD = "^(?=.*[a-z])(?=.*\\d)(?=.*[!\"#$%&'()*+,-./:;<=>?@\\\\^_`{|}~])[a-zA-Z\\d!\"#$%&'()*+,-./:;<=>?@\\\\^_`{|}~]{8,16}$";
    public static final String REGEXP_NAME = "^[가-힣]{1,12}$";
    public static final String REGEXP_NICKNAME = "^[0-9a-zA-Z가-힣]{1,12}$";
    public static final String REGEXP_PHONE = "^01([0|1|6|7|8|9])([0-9]{7,8})$";
    public static final String REGEXP_ZIPCODE = "^([1-7][0-9]{5})|([0-6][0-3][0-9]{3})$";

}
