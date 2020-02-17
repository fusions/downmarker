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

import com.watermark_apps.downmarker.language.token.IToken;
import com.watermark_apps.downmarker.language.token.ParagraphToken;
import com.watermark_apps.downmarker.language.token.SpaceToken;
import com.watermark_apps.downmarker.language.token.TextToken;

import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private Map<BlockRule, Pattern> rules;

    public void setRules(Map<BlockRule, Pattern> rules) {
        this.rules = rules;
    }

    public Stack<IToken> lex(String markdownText) {
        LexContext lexContext = new LexContext();
        tokenize(normalizeWhiteSpace(markdownText), true, false, lexContext);

        return lexContext.getTokens();
    }

    private void tokenize(String source, boolean top, boolean bq, LexContext context) {
        while (source.length() > 0) {
            {
                Matcher matcher = rules.get(BlockRule.NEWLINE).matcher(source);
                if (matcher.find()) {
                    source = source.substring(matcher.end());
                    context.pushToken(new SpaceToken());
                }
            }

            {
                Matcher matcher = rules.get(BlockRule.PARAGRAPH).matcher(source);
                if (matcher.find()) {
                    source = source.substring(matcher.end());
                    context.pushToken(new ParagraphToken(matcher.group(1).substring(matcher.group(1).length() - 1) == "\n" ? matcher.group(1).substring(0, -1) : matcher.group(1)));

                    continue;
                }
            }

            {
                Matcher matcher = rules.get(BlockRule.TEXT).matcher(source);
                if (matcher.find()) {
                    source = source.substring(matcher.end());
                    context.pushToken(new TextToken(matcher.group()));

                    continue;
                }
            }

            if (source.length() > 0) {
                throw new RuntimeException("Infinite loop on character \"" + source.substring(0, 1) + "\"");
            }
        }
    }

    private String normalizeWhiteSpace(String text) {
        return text.replace("\r\n", "\n")
            .replace("\r", "\n")
            .replace("\t", "    ")
            .replaceAll("^ +$", "");
    }

    private static class LexContext {
        private Stack<IToken> tokens = new Stack<>();

        private void pushToken(IToken token) {
            tokens.push(token);
        }

        private Stack<IToken> getTokens() {
            return tokens;
        }
    }
}
