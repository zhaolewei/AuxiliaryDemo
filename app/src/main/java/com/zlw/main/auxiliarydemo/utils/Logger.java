package com.zlw.main.auxiliarydemo.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.google.common.base.Joiner;
import com.zlw.main.auxiliarydemo.BuildConfig;

import java.io.File;
import java.util.Locale;

/**
 * @author Capt.Michael.
 */
public class Logger {
    private static final String FILTER = "^_^";
    private static final String TAG = Logger.class.getSimpleName();
    private static final int LOG_LENGTH_LIMITATION = 4000;

    /**
     * 是否开启检测Token
     */
    public static boolean checkToken = true;

    /**
     * bug记录：开机后 TranslatorApplication.getInstance()可能为空，logCacheFolderPath的初始化已放到TranslatorApplication.init中
     */
    public static String logCacheFolderPath;

    public static boolean IsDebug = BuildConfig.DEBUG;

    private static final String SPACE = "====================================================================================================";

    public enum LogLevel {
        /**
         * Verbose
         */
        V,

        /**
         * Debug
         */
        D,

        /**
         * Info
         */
        I,

        /**
         * Warning
         */
        W,
        /**
         * Error
         */
        E
    }

    public static void v(String tag, String format, Object... args) {
        if (!IsDebug) {
            return;
        }

        String message = buildMessage(format, args);
        tag = formatLength(FILTER + tag, 28);

        Log.v(tag, message);
        cacheLongLog(tag, message);
    }

    public static void v(Throwable throwable, String tag, String format, Object... args) {
        if (!IsDebug) {
            return;
        }

        String message = buildMessage(format, args);
        tag = formatLength(FILTER + tag, 28);

        Log.v(tag, message, throwable);
        cacheLongLog(tag, message, throwable);
    }


    public static void d(String tag, String format, Object... args) {
        if (!IsDebug) {
            return;
        }
        String message = buildMessage(format, args);
        tag = formatLength(FILTER + tag, 28);

        Log.d(tag, message);
        cacheLongLog(tag, message);
    }

    public static void d(Throwable throwable, String tag, String format, Object... args) {
        if (!IsDebug) {
            return;
        }

        String message = buildMessage(format, args);
        tag = formatLength(FILTER + tag, 28);

        Log.d(tag, message, throwable);
        cacheLongLog(tag, message, throwable);
    }

    public static void i(String tag, String format, Object... args) {
        if (!IsDebug) {
            return;
        }

        String message = buildMessage(format, args);
        tag = formatLength(FILTER + tag, 28);

        Log.i(tag, message);
        cacheLongLog(tag, message);
    }

    public static void i(Throwable throwable, String tag, String format, Object... args) {
        if (!IsDebug) {
            return;
        }

        String message = buildMessage(format, args);
        tag = formatLength(FILTER + tag, 28);

        Log.i(tag, message, throwable);
        cacheLongLog(tag, message, throwable);
    }

    public static void w(String tag, String format, Object... args) {
        if (!IsDebug) {
            return;
        }

        String message = buildMessage(format, args);
        tag = formatLength(FILTER + tag, 28);

        Log.w(tag, message);
        cacheLongLog(tag, message);
    }

    public static void w(Throwable throwable, String tag, String format, Object... args) {
        if (!IsDebug) {
            return;
        }

        String message = buildMessage(format, args);
        tag = formatLength(FILTER + tag, 28);

        Log.w(tag, message, throwable);
        cacheLongLog(tag, message, throwable);
    }


    public static void e(String tag, String format, Object... args) {
        if (!IsDebug) {
            return;
        }

        String message = buildMessage(format, args);
        tag = formatLength(FILTER + tag, 28);

        Log.e(tag, message);
        cacheLongLog(tag, message);
    }

    public static void e(Throwable throwable, String tag, String format, Object... args) {
        if (!IsDebug) {
            return;
        }

        String message = buildMessage(format, args);
        tag = formatLength(FILTER + tag, 28);

        Log.e(tag, message, throwable);
        cacheLongLog(tag, message, throwable);
    }

    /**
     * Please refer to comment of {@link #cacheLongLog(String, String, Throwable)}
     *
     * @param tag        TAG name.
     * @param logContent Log content.
     */
    private static void cacheLongLog(String tag, String logContent) {
        cacheLongLog(tag, logContent, null);
    }

    /**
     * Due to length limitation of Logcat, over long log content won't be shown completely in log window,<br/>
     * so cache it to local file at particular path for convenient checking.
     *
     * @param tag        TAG name.
     * @param logContent Log content.
     * @param throwable  Throwable instance, for printing stack trace.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void cacheLongLog(String tag, String logContent, Throwable throwable) {
    }

    /**
     * 打印调用者栈信息
     * 本方法使用System.out输出, 对log进行过滤时请注意
     */
    public static void printCaller() {
        if (!IsDebug) {
            return;
        }

        try {
            String caller, callingClass, callFile;
            int lineNumber;
            StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();
            StringBuilder infoBuffer = new StringBuilder();
            infoBuffer.append("print caller info\n==========BEGIN OF CALLER INFO============\n");
            for (int i = 2; i < trace.length; i++) {
                callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
                caller = trace[i].getMethodName();
                callFile = trace[i].getFileName();
                lineNumber = trace[i].getLineNumber();
                String method = String.format(Locale.US, "[%03d] %s.%s(%s:%d)"
                        , Thread.currentThread().getId(), callingClass, caller, callFile, lineNumber);
                infoBuffer.append(method);
                infoBuffer.append("\n");
            }
            infoBuffer.append("==========END OF CALLER INFO============");
            Logger.i(TAG, infoBuffer.toString());
        } catch (Exception e) {
            Logger.e(e, TAG, e.getMessage());
        }
    }

    private static String buildMessage(String format, Object[] args) {
        try {
            String msg = (args == null || args.length == 0) ? format : String.format(Locale.US, format, args);
            if (!IsDebug) {
                return msg;
            }
            StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();
            String caller = "";
            String callingClass = "";
            String callFile = "";
            int lineNumber = 0;
            for (int i = 2; i < trace.length; i++) {
                Class<?> clazz = trace[i].getClass();
                if (!clazz.equals(Logger.class)) {
                    callingClass = trace[i].getClassName();
                    callingClass = callingClass.substring(callingClass
                            .lastIndexOf('.') + 1);
                    caller = trace[i].getMethodName();
                    callFile = trace[i].getFileName();
                    lineNumber = trace[i].getLineNumber();
                    break;
                }
            }

            String method = String.format(Locale.US, "[%03d] %s.%s(%s:%d)"
                    , Thread.currentThread().getId(), callingClass, caller, callFile, lineNumber);

            return String.format(Locale.US, "%s> %s", formatLength(method, 93), msg);
        } catch (Exception e) {
            Logger.e(e, TAG, e.getMessage());
        }
        return "----->ERROR LOG STRING<------";
    }

    private static String formatLength(String src, int len) {
        StringBuilder sb = new StringBuilder();
        if (src.length() >= len) {
            sb.append(src);
        } else {
            sb.append(src);
            sb.append(SPACE.substring(0, len - src.length()));
        }
        return sb.toString();
    }

    public static void printStackTrace(String tag) {
        printStackTrace(tag, LogLevel.D);
    }

    public static void printStackTrace(String tag, LogLevel level) {
        tag = (TextUtils.isEmpty(tag) ? TAG : tag);
        String message = Joiner.on("\n").join(new Throwable().getStackTrace());

        switch (level) {
            case V:
                Logger.v(tag, message);
                break;
            case I:
                Logger.i(tag, message);
                break;
            case W:
                Logger.w(tag, message);
                break;
            case E:
                Logger.e(tag, message);
                break;
            default:
                Logger.d(tag, message);
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static File getAvailableExternalCacheDir(Context ctx) {
        File[] cacheDirectories = ctx.getExternalCacheDirs();
        for (int index = cacheDirectories.length - 1; index >= 0; index--) {
            File file = cacheDirectories[index];
            if (null != file && Environment.MEDIA_MOUNTED.equals(Environment.getStorageState(file))) {
                return file;
            }
        }
        return null;
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean isFileExists(String filePath) {
        return (!TextUtils.isEmpty(filePath) && isFileExists(new File(filePath)));
    }

    /**
     * 判断文件是否存在
     *
     * @param file 文件
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean isFileExists(File file) {
        return file != null && file.exists();
    }

    public static class TimeCalculator {
        long start;

        public TimeCalculator() {
            start = SystemClock.elapsedRealtime();
        }

        public long end() {
            return SystemClock.elapsedRealtime() - start;
        }
    }
}
