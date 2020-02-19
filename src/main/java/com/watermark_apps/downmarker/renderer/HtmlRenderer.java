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

package com.watermark_apps.downmarker.renderer;

import com.watermark_apps.downmarker.language.Helpers;
import com.watermark_apps.downmarker.language.IRenderer;
import com.watermark_apps.downmarker.language.Link;

public class HtmlRenderer implements IRenderer {
    @Override
    public String renderLink(Link link) {
        String url = Helpers.cleanUrl(link.getUrl());
        StringBuilder output = new StringBuilder("<a href=\"" + Helpers.escape(url) + "\"");
        if (!link.getTitle().isEmpty()) {
            output.append(" title=\"" + link.getTitle() + "\"");
        }

        output.append(">" + link.getText() + "</a>");

        return output.toString();
    }

    @Override
    public String renderImage(Link link) {
        String url = Helpers.cleanUrl(link.getUrl());
        StringBuilder output = new StringBuilder("<img src=\"" + url + "\" alt=\"" + link.getText() + "\"");
        if (!link.getTitle().isEmpty()) {
            output.append(" title=\"" + link.getTitle() + "\"");
        }

        output.append(" />");

        return output.toString();
    }

    @Override
    public String renderText(String text) {
        return text;
    }

    @Override
    public String renderParagraph(String text) {
        return "<p>" + text + "</p>\n";
    }
}
