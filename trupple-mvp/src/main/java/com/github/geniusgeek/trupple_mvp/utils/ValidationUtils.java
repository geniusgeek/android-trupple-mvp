package com.github.geniusgeek.trupple_mvp.utils;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 4/15/16.
 */
public enum ValidationUtils {
    INSTANCE;
    private static final String USERNAME_PATTERN = "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$";
    //"^[A-Za-z_0-9]{3,15}$";


    /**
     * reset the error flag of a textview or edittext.
     *
     * @param editText the view to unflag
     * @see TextView , please note textview is the superclass of {@link EditText}
     */
    public static void resetErrorEditText(TextView editText, TextView... others) {
        editText.setError(null);
        editText.clearFocus();

        for (int i = 0; i < others.length; i++) {
            others[i].setError(null);
            others[i].clearFocus();
        }

    }

    // validating email id
    public boolean isValidEmail(String email) {
        return !(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    // validating password with retype password
    public boolean isValidPassword(String pass) {
        return pass != null && !pass.isEmpty();
    }

    //validating otp code
    public boolean isValidOtp(String otp) {
        return !otp.isEmpty() && otp.length() == 6;
    }

    /**
     * Validate username with regular expression
     *
     * @param userName username for validation
     * @return true valid username, false invalid username
     */
    public boolean isValidName(String userName) {
        if (userName == null)
            return false;

        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(userName);

        return matcher.matches();
    }

    public boolean containsWhiteSpace(String string) {
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }

    public boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }


    /**
     * This is used to check the given URL is valid or not.
     * @param uri
     * @return true if url is valid, false otherwise.
     */
    public boolean isValidUri(Context context, String uri) {
        Uri resimUri = Uri.parse(uri);
        return FileUtils.getFile(context, resimUri)!=null;
    }


}
