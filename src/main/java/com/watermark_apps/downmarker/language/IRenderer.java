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

public interface IRenderer {
    String renderLink(Link link);
    String renderImage(Link link);
    String renderText(String text);
    String renderParagraph(String text);
}
