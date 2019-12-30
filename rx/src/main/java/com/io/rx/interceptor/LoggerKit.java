package com.io.rx.interceptor;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/12.
 */

public class LoggerKit {
    private LoggerKit() {
    }

    public static PrinterHelper with(int logLevel, String tag, String headText) {
        return new PrinterHelper(logLevel, tag, headText);
    }

    public static String json(String json) {
        if (!TextUtils.isEmpty(json)) {
            try {
                String message;
                if (json.startsWith("{")) {
                    JSONObject e1 = new JSONObject(json);
                    message = e1.toString(4);
                    return message;
                }

                if (json.startsWith("[")) {
                    JSONArray e = new JSONArray(json);
                    message = e.toString(4);
                    return message;
                }
            } catch (JSONException var4) {
            }
        }
        return "Empty/Null json content";
    }

    private static class Printer {
        private static final String TOP_BORDER = "╔════════════════════════════════════════════════════════════════════════════════════════";
        private static final String BOTTOM_BORDER = "╚════════════════════════════════════════════════════════════════════════════════════════";
        private static final String MIDDLE_BORDER = "╟────────────────────────────────────────────────────────────────────────────────────────";
        private static final String HORIZONTAL_DOUBLE_LINE = "║ ";

        private Printer() {
        }

        public static synchronized void print(PrinterHelper helper) {
            print(helper, TOP_BORDER);
//            print(helper, "%s%s\t: %s", HORIZONTAL_DOUBLE_LINE, "Thread", Thread.currentThread().getName());
            print(helper, "%s%s\t: %s", HORIZONTAL_DOUBLE_LINE, "Title", helper.headText);
            print(helper, MIDDLE_BORDER);
            for (String content : helper.logContentes) {
                logContent(helper, content);
            }
            print(helper, BOTTOM_BORDER);
        }

        private static void print(PrinterHelper helper, String messageFormat, Object... values) {
            print(helper, String.format(messageFormat, values));
        }

        private static void print(PrinterHelper helper, String message) {
            Log.println(helper.logLevel, helper.tag, message);
        }

        private static void logContent(PrinterHelper helper, String chunk) {
            String[] lines = chunk.split(System.getProperty("line.separator"));
            String[] arr$ = lines;
            int len$ = lines.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                String line = arr$[i$];
                Log.println(helper.logLevel, helper.tag, "║ " + line);
            }
        }
    }

    public static class PrinterHelper {
        private String tag;
        private int logLevel;
        private String headText;
        private List<String> logContentes;

        public PrinterHelper(int logLevel, String tag, String headText) {
            this.tag = tag;
            this.logLevel = logLevel;
            this.headText = headText;
            logContentes = new ArrayList<>();
        }

        public PrinterHelper add(String content) {
            logContentes.add(content);
            return this;
        }

        public PrinterHelper add(String contentFormat, Object... values) {
            logContentes.add(String.format(contentFormat, values));
            return this;
        }

        public void print() {
            Printer.print(this);
        }
    }
}
