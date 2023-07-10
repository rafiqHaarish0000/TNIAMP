package com.farmwiseai.tniamp.utils;

import android.content.Context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {
    private final Context mContext;

    public ValidationUtils(Context mContext) {
        this.mContext = mContext;
    }

    public boolean isValidConfirmPasswrod(String confirmPassword, String password) {
        if (!confirmPassword.equals(password)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isValidAddress(String address) {
        if (address == null || address.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isValidPincode(String pincode) {
        if (pincode == null) {
            return false;
        } else {
            String PINCODE_PATTERN = "^[0-9]{6}$";

            Pattern pattern = Pattern.compile(PINCODE_PATTERN);
            Matcher matcher = pattern.matcher(pincode);
            return matcher.matches();
        }
    }

    public boolean isValidMobile(String mobile) {
        Pattern p = Pattern.compile("^[56789]\\d{9,9}$");
        if (mobile == null && mobile.equals("") && mobile.length() != 10) {
            return false;
        } else {
            Matcher m = p.matcher(mobile);
            return m.matches();
        }
    }

    public static boolean isValidMobileNumber(String s) {

        // The given argument to compile() method
        // is regular expression. With the help of
        // regular expression we can validate mobile
        // number.
        // 2) Then contains 6,7 or 8 or 9.
        // 3) Then contains 9 digits
        Pattern p = Pattern.compile("[6-9][0-9]{9}");

        // Pattern class contains matcher() method
        // to find matching between given number
        // and regular expression
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

    public boolean isOTPNumber(String mobile) {

        if (mobile.equals("")) {
            return false;
        } else if (mobile.length() != 4) {
            return false;
        } else {


            return true;
        }
    }

    public boolean isValidPassword(String password) {
        Pattern p = Pattern.compile("((?!\\s)\\A)(\\s|(?<!\\s)\\S){8,20}\\Z");
        if (password == null) {
            return false;
        } else {
            Matcher m = p.matcher(password);
            return m.matches();
        }
    }

    public boolean isValidEmail(String email) {
       /* if (email == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }*/

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.matches(emailPattern)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isValidLastName(String lastName) {
        Pattern p = Pattern.compile("^[a-zA-Z]{3,20}$");
        if (lastName == null) {
            return false;
        } else {
            Matcher m = p.matcher(lastName);
            return m.matches();
        }
    }

    public boolean isValidFirstName(String firstname) {
        Pattern p = Pattern.compile("^[a-zA-Z]{3,20}$");
        if (firstname == null) {
            return false;
        } else {
            Matcher m = p.matcher(firstname);
            return m.matches();
        }
    }

    public boolean isValidAge(String age) {
        Pattern p = Pattern.compile("^[1-9]{1,3}$");
        if (age == null || age.equals("")) {
            return false;
        } else {
            Matcher m = p.matcher(age);
            return m.matches();
        }
    }

    public boolean isEmptyEditText(String s) {
        if (s == null || s.equals("")) {
            return false;
        } else {
            return true;
        }
    }
}
