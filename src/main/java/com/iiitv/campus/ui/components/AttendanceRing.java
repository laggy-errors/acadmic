package com.iiitv.campus.ui.components;

import com.iiitv.campus.dto.AttendanceStatus;
import com.vaadin.flow.component.html.Div;

public class AttendanceRing extends Div {

    public enum Size {
        SMALL("ring-sm"),
        MEDIUM("ring-md"),
        LARGE("ring-lg");

        private final String cssClass;

        Size(String cssClass) {
            this.cssClass = cssClass;
        }

        public String getCssClass() {
            return cssClass;
        }
    }

    public AttendanceRing(double percentage, String label, AttendanceStatus status, Size size) {
        addClassNames("attendance-ring-container", size.getCssClass());

        String statusClass = switch (status) {
            case SAFE -> "ring-safe";
            case WATCH -> "ring-watch";
            case SHORT -> "ring-short";
        };
        addClassName(statusClass);

        double radius = 38.0;
        double circumference = 2 * Math.PI * radius; // ~238.76
        double offset = circumference * (1.0 - (Math.min(100.0, Math.max(0.0, percentage)) / 100.0));
        String formattedPercentage = String.format("%.1f%%", percentage);

        // Raw SVG without inline styles, using SVG attributes and CSS classes
        String svgHtml = """
            <svg class="attendance-ring-svg" viewBox="0 0 100 100" aria-label="Attendance: %s">
                <circle class="attendance-ring-track" cx="50" cy="50" r="%.2f"></circle>
                <circle class="attendance-ring-fill" cx="50" cy="50" r="%.2f"
                        stroke-dasharray="%.2f" stroke-dashoffset="%.2f"></circle>
                <text class="attendance-ring-value font-mono" x="50" y="%s" text-anchor="middle">%s</text>
                %s
            </svg>
            """.formatted(
                formattedPercentage,
                radius,
                radius,
                circumference,
                offset,
                label != null && !label.isBlank() ? "48" : "55",
                formattedPercentage,
                label != null && !label.isBlank()
                    ? "<text class=\"attendance-ring-sublabel font-ui\" x=\"50\" y=\"64\" text-anchor=\"middle\">" + label + "</text>"
                    : ""
        );

        getElement().setProperty("innerHTML", svgHtml);
    }

    public AttendanceRing(double percentage, String label, AttendanceStatus status) {
        this(percentage, label, status, Size.MEDIUM);
    }
}
