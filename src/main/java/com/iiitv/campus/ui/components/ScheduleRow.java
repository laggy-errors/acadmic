package com.iiitv.campus.ui.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ScheduleRow extends Div {

    public ScheduleRow(String timeMono, String courseTitle, String room, String faculty, String nextUpChipText) {
        addClassName("schedule-row");

        boolean isHighlighted = nextUpChipText != null && !nextUpChipText.isBlank();
        if (isHighlighted) {
            addClassName("schedule-row-highlight");
        }

        HorizontalLayout rowLayout = new HorizontalLayout();
        rowLayout.addClassName("schedule-row-layout");
        rowLayout.setWidthFull();
        rowLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        rowLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        HorizontalLayout leftGroup = new HorizontalLayout();
        leftGroup.setAlignItems(FlexComponent.Alignment.CENTER);
        leftGroup.setSpacing(true);

        Span timeSpan = new Span(timeMono);
        timeSpan.addClassNames("schedule-row-time", "font-mono", "text-navy");
        leftGroup.add(timeSpan);

        VerticalLayout courseGroup = new VerticalLayout();
        courseGroup.setPadding(false);
        courseGroup.setSpacing(false);

        String titleWithRoom = courseTitle;
        if (room != null && !room.isBlank()) {
            titleWithRoom += " (" + room + ")";
        }

        Span courseSpan = new Span(titleWithRoom);
        courseSpan.addClassNames("schedule-row-course", "font-ui", "text-main");
        courseGroup.add(courseSpan);

        if (faculty != null && !faculty.isBlank()) {
            Paragraph facultyText = new Paragraph(faculty);
            facultyText.addClassNames("schedule-row-faculty", "font-ui", "text-muted");
            courseGroup.add(facultyText);
        }

        leftGroup.add(courseGroup);
        rowLayout.add(leftGroup);

        if (isHighlighted) {
            StatusChip chip = StatusChip.nextUp(nextUpChipText);
            rowLayout.add(chip);
        }

        add(rowLayout);
    }

    public ScheduleRow(String timeMono, String courseTitle, String room, String faculty) {
        this(timeMono, courseTitle, room, faculty, null);
    }
}
