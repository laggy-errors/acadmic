package com.iiitv.campus.ui.components;

import com.vaadin.flow.component.html.Div;

public class SkeletonBlock extends Div {

    public enum Variant {
        TEXT("skeleton-text"),
        RECTANGLE("skeleton-rect"),
        CIRCLE("skeleton-circle");

        private final String cssClass;

        Variant(String cssClass) {
            this.cssClass = cssClass;
        }

        public String getCssClass() {
            return cssClass;
        }
    }

    public SkeletonBlock(String width, String height, Variant variant) {
        addClassNames("skeleton-block", variant.getCssClass());
        setWidth(width);
        setHeight(height);
        getElement().setAttribute("aria-hidden", "true");
    }

    public SkeletonBlock(String width, String height) {
        this(width, height, Variant.RECTANGLE);
    }

    public static SkeletonBlock text(String width) {
        return new SkeletonBlock(width, "16px", Variant.TEXT);
    }

    public static SkeletonBlock title(String width) {
        return new SkeletonBlock(width, "28px", Variant.TEXT);
    }

    public static SkeletonBlock circle(String size) {
        return new SkeletonBlock(size, size, Variant.CIRCLE);
    }
}
