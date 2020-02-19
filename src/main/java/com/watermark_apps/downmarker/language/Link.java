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

public class Link {
    private final String url;
    private final String text;
    private final String title;

    public Link(String url, String text) {
        this(url, text, null);
    }

    public Link(String url, String text, String title) {
        this.url = url;
        this.text = text == null ? "" : text;
        this.title = title == null ? "" : title;
    }

    public String getUrl() {
        return url;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }
}
