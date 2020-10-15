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

import com.watermark_apps.downmarker.language.IRenderer;
import com.watermark_apps.downmarker.language.Link;

public class TextRenderer implements IRenderer {
    @Override
    public String renderLink(Link link) {
        return link.getText();
    }

    @Override
    public String renderImage(Link link) {
        return link.getText();
    }

    @Override
    public String renderText(String text) {
        return text;
    }

    @Override
    public String renderParagraph(String text) {
        return text;
    }
}
