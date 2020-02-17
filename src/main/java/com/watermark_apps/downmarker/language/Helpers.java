package com.watermark_apps.downmarker.language;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helpers {
    private static final Pattern HTML_SPECIAL_CHARS_PATTERN_ENCODE = Pattern.compile("[&<>\"']");
    private static final Pattern HTML_SPECIAL_CHARS_PATTERN_NO_ENCODE = Pattern.compile("[<>\"']|&(?!#?\\w+;)");
    private static final Map<String, String> HTML_SPECIAL_CHARS_REPLACEMENTS = new HashMap<String, String>() {{
        put("&", "&amp;");
        put("<", "&lt;");
        put(">", "&gt;");
        put("\"", "&quot;");
        put("'", "&#039;");
    }};

    public static String replaceAll(String subject, Pattern pattern, Function<Matcher, String> replacer) {
        Matcher matcher = pattern.matcher(subject);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(result, replacer.apply(matcher));
        }
        matcher.appendTail(result);

        return result.toString();
    }

    public static String escape(String html) {
        return escape(html, false);
    }

    public static String escape(String html, boolean encode) {
        if (encode) {
            if (HTML_SPECIAL_CHARS_PATTERN_ENCODE.matcher(html).find()) {
                return replaceAll(html, HTML_SPECIAL_CHARS_PATTERN_ENCODE, (matcher) -> HTML_SPECIAL_CHARS_REPLACEMENTS.get(matcher.group()));
            }
        } else {
            if (HTML_SPECIAL_CHARS_PATTERN_NO_ENCODE.matcher(html).find()) {
                return replaceAll(html, HTML_SPECIAL_CHARS_PATTERN_NO_ENCODE, (matcher) -> HTML_SPECIAL_CHARS_REPLACEMENTS.get(matcher.group()));
            }
        }

        return html;
    }

    public static int indexOfClosingParenthesis(String subject, char openingParenthesis, char closingParenthesis) {
        if (subject.indexOf(closingParenthesis) == -1) {
            return -1;
        }

        for (int i = 0, depth = 0; i < subject.length(); i++) {
            char c = subject.charAt(i);
            if (c == '\\') {
                i++;
            } else if (c == openingParenthesis) {
                depth++;
            } else if (c == closingParenthesis) {
                depth--;
                if (depth < 0) {
                    return i;
                }
            }
        }

        return -1;
    }

    public static String cleanUrl(String url) {
        if (url.isEmpty()) {
            return "";
        }

        return url
            .replace("[", "%5B")
            .replace("\\", "%5C")
            ;
    }
}
