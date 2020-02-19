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

enum InlineRule {
    ESCAPE() {
        @Override
        protected String getPattern() {
            return "^\\\\([!\"#$%&'()*+,\\-./:;<=>?@\\[\\]\\\\^_`{|}~])";
        }
    },
    AUTOLINK() {
        @Override
        protected String getPattern() {
            return "^<(scheme:[^\\s\\x00-\\x1f<>]*|email)>";
        }
    },
    URL() {
    },
    LINK() {
        @Override
        protected String getPattern() {
            return "^!?\\[(label)\\]\\(\\s*(href)(?:\\s+(title))?\\s*\\)";
        }
    },
    _PUNCTUATION() {
        @Override
        protected String getPattern() {
            return "!\"#$%&'()*+,\\-./:;<=>?@\\[^_{|}~";
        }
    },
    EM() {
        @Override
        protected String getPattern() {
            return "^_([^\\s_])_(?!_)|^\\*([^\\s*<\\[])\\*(?!\\*)|^_([^\\s<][\\s\\S]*?[^\\s_])_(?!_|[^\\spunctuation])|^_([^\\s_<][\\s\\S]*?[^\\s])_(?!_|[^\\spunctuation])|^\\*([^\\s<\"][\\s\\S]*?[^\\s\\*])\\*(?!\\*|[^\\spunctuation])|^\\*([^\\s*\"<\\[][\\s\\S]*?[^\\s])\\*(?!\\*)";
        }
    },
    TEXT() {
        @Override
        protected String getPattern() {
            return "^(`+|[^`])(?:[\\s\\S]*?(?:(?=[\\\\<!\\[`*]|\\b_|$)|[^ ](?= {2,}\\n))|(?= {2,}\\n))";
        }
    },
    _ESCAPES() {
        @Override
        protected String getPattern() {
            return "\\\\([!\"#$%&'()*+,\\-./:;<=>?@\\[\\]\\\\^_`{|}~])";
        }
    },
    _SCHEME() {
        @Override
        protected String getPattern() {
            return "[a-zA-Z][a-zA-Z0-9+.-]{1,31}";
        }
    },
    _EMAIL() {
        @Override
        protected String getPattern() {
            return "[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+(@)[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)+(?![-_])";
        }
    },
    _LABEL() {
        @Override
        protected String getPattern() {
            return "(?:\\[[^\\[\\]]*\\]|\\\\.|`[^`]*`|[^\\[\\]\\\\`])*?";
        }
    },
    _HREF() {
        @Override
        protected String getPattern() {
            return "<(?:\\\\[<>]?|[^\\s<>\\\\])*>|[^\\s\\x00-\\x1f]*";
        }
    },
    _TITLE() {
        @Override
        protected String getPattern() {
            return "\"(?:\\\\\"?|[^\"\\\\])*\"|'(?:\\\\'?|[^'\\\\])*'|\\((?:\\\\\\)?|[^)\\\\])*\\)";
        }
    },
    BACKPEDAL() {
    },
    ;

    protected String getPattern() {
        return "";
    }
}
