package com.iiitv.campus.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class EmptyState extends Div {

    private final String heading;
    private final String specificCopy;

    public EmptyState(String title, String specificCopy, Component action) {
        this.heading = title;
        this.specificCopy = specificCopy;
        addClassNames("campus-card", "empty-state-card");

        VerticalLayout layout = new VerticalLayout();
        layout.addClassName("empty-state-layout");
        layout.setPadding(false);
        layout.setSpacing(true);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        H3 titleElement = new H3(title);
        titleElement.addClassNames("empty-state-title", "font-ui", "text-navy");
        layout.add(titleElement);

        Paragraph copyElement = new Paragraph(specificCopy);
        copyElement.addClassNames("empty-state-copy", "font-ui", "text-muted");
        layout.add(copyElement);

        if (action != null) {
            layout.add(action);
        }

        add(layout);
    }

    public EmptyState(String title, String specificCopy) {
        this(title, specificCopy, null);
    }

    public String getHeading() {
        return heading;
    }

    public String getSpecificCopy() {
        return specificCopy;
    }
}
