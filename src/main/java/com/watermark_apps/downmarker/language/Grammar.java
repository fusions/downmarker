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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Grammar {
    public static final Map<BlockRule, Pattern> BLOCK_RULES_NORMAL = new HashMap<>();
    public static final Map<BlockRule, Pattern> BLOCK_RULES_GFM;
    public static final Map<InlineRule, Pattern> INLINE_RULES_NORMAL = new HashMap<>();
    public static final Map<InlineRule, Pattern> INLINE_RULES_GFM;

    static {
        for (BlockRule value : BlockRule.values()) {
            if (!value.name().startsWith("_")) {
                BLOCK_RULES_NORMAL.put(value, value.getPattern() == null ? null : Pattern.compile(value.getPattern()));
            }

            BLOCK_RULES_NORMAL.put(BlockRule.DEF, Pattern.compile(new PatternEditor(BlockRule.DEF.getPattern())
                .replace("label", BlockRule._LABEL.getPattern())
                .replace("title", BlockRule._TITLE.getPattern())
                .getPattern()
            ));
        }

         BLOCK_RULES_GFM = BLOCK_RULES_NORMAL.entrySet().stream().collect(
            Collectors.toMap(
                e -> e.getKey(),
                e -> {
                    switch (e.getKey()) {
                        case NPTABLE:
                            return Pattern.compile("^ *([^|\\n ].*\\|.*)\\n *([-:]+ *\\|[-| :]*)(?:\\n((?:.*[^>\\n ].*(?:\\n|$))*)\\n*|$)");
                        case TABLE:
                            return Pattern.compile("^ *\\|(.+)\\n *\\|?( *[-:]+[-| :]*)(?:\\n((?: *[^>\\n ].*(?:\\n|$))*)\\n*|$)");
                        default:
                            return e.getValue();
                    }
                }
            )
        );

        for (InlineRule value : InlineRule.values()) {
            if (!value.name().startsWith("_")) {
                INLINE_RULES_NORMAL.put(value, value.getPattern() == null ? null : Pattern.compile(value.getPattern()));
            }

            INLINE_RULES_NORMAL.put(InlineRule.EM, Pattern.compile(new PatternEditor(InlineRule.EM.getPattern())
                .replace("punctuation", InlineRule._PUNCTUATION.getPattern())
                .getPattern()
            ));
            INLINE_RULES_NORMAL.put(InlineRule.AUTOLINK, Pattern.compile(new PatternEditor(InlineRule.AUTOLINK.getPattern())
                .replace("scheme", InlineRule._SCHEME.getPattern())
                .replace("email", InlineRule._EMAIL.getPattern())
                .getPattern()
            ));
            INLINE_RULES_NORMAL.put(InlineRule.LINK, Pattern.compile(new PatternEditor(InlineRule.LINK.getPattern())
                .replace("label", InlineRule._LABEL.getPattern())
                .replace("href", InlineRule._HREF.getPattern())
                .replace("title", InlineRule._TITLE.getPattern())
                .getPattern()
            ));
        }

        INLINE_RULES_GFM = INLINE_RULES_NORMAL.entrySet().stream().collect(
            Collectors.toMap(
                e -> e.getKey(),
                e -> {
                    switch (e.getKey()) {
                        case ESCAPE:
                            return Pattern.compile(new PatternEditor(InlineRule.ESCAPE.getPattern())
                                .replace("])", "~|])")
                                .getPattern()
                            );
                        case URL:
                            return Pattern.compile(new PatternEditor("^((?:ftp|https?):\\/\\/|www\\.)(?:[a-zA-Z0-9\\-]+\\.?)+[^\\s<]*|^email")
                                .replace("email", "[A-Za-z0-9._+-]+(@)[a-zA-Z0-9-_]+(?:\\.[a-zA-Z0-9-_]*[a-zA-Z0-9])+(?![-_])")
                                .getPattern(),
                                Pattern.CASE_INSENSITIVE
                            );
                        case TEXT:
                            return Pattern.compile("^(`+|[^`])(?:[\\s\\S]*?(?:(?=[\\\\<!\\[`*~]|\\b_|https?:\\/\\/|ftp:\\/\\/|www\\.|$)|[^ ](?= {2,}\\n)|[^a-zA-Z0-9.!#$%&'*+\\/=?_`{\\|}~-](?=[a-zA-Z0-9.!#$%&'*+\\/=?_`{\\|}~-]+@))|(?= {2,}\\n|[a-zA-Z0-9.!#$%&'*+\\/=?_`{\\|}~-]+@))");
                        default:
                            return e.getValue();
                    }
                }
            )
        );
        INLINE_RULES_GFM.put(InlineRule.BACKPEDAL, Pattern.compile("(?:[^?!.,:;*_~()&]+|\\([^)]*\\)|&(?![a-zA-Z0-9]+;$)|[?!.,:;*_~)]+(?!$))+"));
    }
}
