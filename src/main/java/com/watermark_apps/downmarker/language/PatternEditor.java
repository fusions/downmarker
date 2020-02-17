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

class PatternEditor {
    private final String regex;

    PatternEditor(String regex) {
        this.regex = regex;
    }

    public PatternEditor replace(String from, String to) {
        return new PatternEditor(regex.replace(from, to.replaceAll("(^|[^\\[])\\^", "$1")));
    }

    public String getPattern() {
        return regex;
    }
}
