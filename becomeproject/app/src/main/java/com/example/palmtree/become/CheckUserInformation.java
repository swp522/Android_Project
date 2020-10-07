package com.example.palmtree.become;

import java.util.regex.Pattern;

//사용자의 회원 가입 절차에서의 제약조건 포함
public class CheckUserInformation {

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(

            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    public boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }
}
