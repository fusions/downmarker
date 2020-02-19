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

enum BlockRule {
    NEWLINE() {
        @Override
        protected String getPattern() {
            return "^\\n+";
        }
    },
    HR() {
        @Override
        protected String getPattern() {
            return "^ {0,3}((?:- *){3,}|(?:_ *){3,}|(?:\\* *){3,})(?:\\n+|$)";
        }
    },
    DEF() {
        @Override
        protected String getPattern() {
            return "^ {0,3}\\[(label)\\]: *\\n? *<?([^\\s>]+)>?(?:(?: +\\n? *| *\\n *)(title))? *(?:\\n+|$)";
        }
    },
    NPTABLE() {
    },
    TABLE() {
    },
    _PARAGRAPH() {
        @Override
        protected String getPattern() {
            return "^([^\\n]+(?:\\n(?!hr|heading|lheading|blockquote|fences|list|html)[^\\n]+)*)";
        }
    },
    _LABEL() {
        @Override
        protected String getPattern() {
            return "(?!\\s*\\])(?:\\\\[\\[\\]]|[^\\[\\]])+";
        }
    },
    _TITLE() {
        @Override
        protected String getPattern() {
            return "(?:\"(?:\\\\\"?|[^\"\\\\])*\"|'[^'\\n]*(?:\\n[^'\\n]+)*\\n?'|\\([^()]*\\))";
        }
    },
    PARAGRAPH() {
        @Override
        protected String getPattern() {
            return new PatternEditor(_PARAGRAPH.getPattern())
                .replace("hr", HR.getPattern())
                .replace("heading", " {0,3}#{1,6} +")
                .replace("|lheading", "")
                .replace("blockquote", " {0,3}>")
                .replace("fences", " {0,3}(?:`{3,}|~{3,})[^`\\n]*\\n")
                .replace("list", " {0,3}(?:[*+-]|1[.)]) ")
                .replace("html", "</?(?:tag)(?: +|\\n|/?>)|<(?:script|pre|style|!--)")
                .replace("tag", _TAG.getPattern())
                .getPattern();
        }
    },
    TEXT() {
        @Override
        protected String getPattern() {
            return "^[^\\n]+";
        }
    },
    _TAG() {
        @Override
        protected String getPattern() {
            return "address|article|aside|base|basefont|blockquote|body|caption|center|col|colgroup|dd|details|dialog|dir|div|dl|dt|fieldset|figcaption|figure|footer|form|frame|frameset|h[1-6]|head|header|hr|html|iframe|legend|li|link|main|menu|menuitem|meta|nav|noframes|ol|optgroup|option|p|param|section|source|summary|table|tbody|td|tfoot|th|thead|title|tr|track|ul";
        }
    },
    ;

    protected String getPattern() {
        return "";
    }
}
