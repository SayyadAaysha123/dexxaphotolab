package com.dexxaphotolab.app.payment.Utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;


public class Constants {


    public static String getDownloadDir(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + File.separator;
    }

    public static final String USER_LOGIN_STATUS = "login_status";

}
