/*
 * Copyright (c) fusions corporation,
 * All rights reserved.
 *
 * This file is part of Downmarker.
 *
 * This program and the accompanying materials are made available under
 * the terms of the MIT License which accompanies this
 * distribution, and is available at https://opensource.org/licenses/MIT
 */

package com.watermark_apps.downmarker.language;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InlineCompiler {
    private Map<InlineRule, Pattern> rules;
    private IRenderer renderer;
    private boolean inLink = false;
    private boolean inRawBlock = false;

    public InlineCompiler(Map<InlineRule, Pattern> rules, IRenderer renderer) {
        this.rules = rules;
        this.renderer = renderer;
    }

    public String compile(String markdownText) {
        StringBuilder output = new StringBuilder();

        while (markdownText.length() > 0) {
            escape: {
                Matcher matcher = createMatcher(InlineRule.ESCAPE, markdownText);
                if (matcher != null && matcher.find()) {
                    output.append(Helpers.escape(matcher.group(1)));
                    markdownText = markdownText.substring(matcher.end());
                }
            }

            link:
            {
                Matcher matcher = createMatcher(InlineRule.LINK, markdownText);
                if (matcher != null && matcher.find()) {
                    String href, title;
                    String group2, group0, group3;

                    int indexOfClosingParenthesis = Helpers.indexOfClosingParenthesis(matcher.group(2), '(', ')');
                    if (indexOfClosingParenthesis > -1) {
                        int start = matcher.group().indexOf('!') == 0 ? 5 : 4;
                        int linkLength = start + matcher.group(1).length() + indexOfClosingParenthesis;
                        group2 = matcher.group(2).substring(0, indexOfClosingParenthesis);
                        group0 = matcher.group(0).substring(0, linkLength).trim();
                        group3 = null;
                    } else {
                        group2 = matcher.group(2);
                        group0 = matcher.group(0);
                        group3 = matcher.group(3);
                    }

                    inLink = true;
                    href = group2;
                    title = group3 == null ? "" : group3.substring(1, group3.length() - 1);
                    href = href.trim().replaceAll("^<([\\s\\S]*)>$", "$1");
                    output.append(compileLink(matcher, escape(href), escape(title)));
                    inLink = false;
                    markdownText = markdownText.substring(group0.length());

                    continue;
                }
            }

            autolink: {
                Matcher matcher = createMatcher(InlineRule.AUTOLINK, markdownText);
                if (matcher != null && matcher.find()) {
                    String href, text;
                    text = Helpers.escape(matcher.group(1));
                    if (matcher.group(2) == null) {
                        href = text;
                    } else {
                        href = "mailto:" + text;
                    }

                    output.append(renderer.renderLink(new Link(href, text)));
                    markdownText = markdownText.substring(matcher.end());

                    continue;
                }
            }

            url: {
                if (!inLink) {
                    Matcher matcher = createMatcher(InlineRule.URL, markdownText);
                    if (matcher != null && matcher.find()) {
                        String href, text;
                        String currentCapZero = matcher.group(0);
                        if ("@".equals(matcher.group(2))) {
                            text = Helpers.escape(matcher.group(0));
                            href = "mailto:" + text;
                        } else {
                            String prevCapZero;

                            do {
                                prevCapZero = currentCapZero;
                                Matcher matcher1 = createMatcher(InlineRule.BACKPEDAL, currentCapZero);
                                if (matcher1 != null && matcher1.find()) {
                                    currentCapZero = matcher1.group(0);
                                }
                            } while (!prevCapZero.equals(currentCapZero));

                            text = Helpers.escape(currentCapZero);

                            if (matcher.group(1).equals("www.")) {
                                href = "http://" + text;
                            } else {
                                href = text;
                            }
                        }

                        output.append(renderer.renderLink(new Link(href, text)));
                        markdownText = markdownText.substring(currentCapZero.length());

                        continue;
                    }
                }
            }

            text: {
                Matcher matcher = createMatcher(InlineRule.TEXT, markdownText);
                if (matcher != null && matcher.find()) {
                    if (inRawBlock) {
                        output.append(renderer.renderText(Helpers.escape(matcher.group(0))));
                    } else {
                        output.append(renderer.renderText(Helpers.escape(matcher.group(0))));
                    }
                    markdownText = markdownText.substring(matcher.end());

                    continue;
                }
            }

            if (markdownText.length() > 0) {
                throw new RuntimeException("Infinite loop on character \"" + markdownText.substring(0, 1) + "\"");
            }
        }

        return output.toString();
    }

    private Matcher createMatcher(InlineRule inlineRule, String subject) {
        Pattern pattern = rules.get(inlineRule);
        if (pattern == null) {
            return null;
        }

        return pattern.matcher(subject);
    }

    private String compileLink(Matcher matcher, String url, String title) {
        if (matcher.group(0).charAt(0) == '!') {
            return renderer.renderImage(new Link(url, Helpers.escape(matcher.group(1)), title));
        } else {
            return renderer.renderLink(new Link(url, compile(matcher.group(1)), title));
        }
    }

    private String escape(String text) {
        if (text.isEmpty()) {
            return text;
        }

        return text.replaceAll(InlineRule._ESCAPES.getPattern(), "$1");
    }
}
