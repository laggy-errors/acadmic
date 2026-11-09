package com.iiitv.campus.ui.components;

import com.vaadin.flow.component.html.Span;

public class StatusChip extends Span {

    public enum Type {
        SAFE("chip-safe"),
        WATCH("chip-watch"),
        SHORT("chip-short"),
        CRITICAL("chip-critical"),
        HIGH("chip-high"),
        MEDIUM("chip-medium"),
        NEXT_UP("chip-next-up"),
        NEUTRAL("chip-neutral");

        private final String cssClass;

        Type(String cssClass) {
            this.cssClass = cssClass;
        }

        public String getCssClass() {
            return cssClass;
        }
    }

    public StatusChip(Type type) {
        this(type.name(), type);
    }

    public StatusChip(String text, Type type) {
        super(text);
        addClassNames("chip", type.getCssClass(), "font-mono");
    }

    public static StatusChip safe(String text) {
        return new StatusChip(text, Type.SAFE);
    }

    public static StatusChip watch(String text) {
        return new StatusChip(text, Type.WATCH);
    }

    public static StatusChip shortStatus(String text) {
        return new StatusChip(text, Type.SHORT);
    }

    public static StatusChip critical(String text) {
        return new StatusChip(text, Type.CRITICAL);
    }

    public static StatusChip high(String text) {
        return new StatusChip(text, Type.HIGH);
    }

    public static StatusChip medium(String text) {
        return new StatusChip(text, Type.MEDIUM);
    }

    public static StatusChip nextUp(String text) {
        return new StatusChip(text, Type.NEXT_UP);
    }

    public static StatusChip neutral(String text) {
        return new StatusChip(text, Type.NEUTRAL);
    }
}
