package com.iiitv.campus.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class StatCard extends Div {

    public StatCard(String label, String monoValue, Component ring, Component auxiliary) {
        addClassNames("campus-card", "stat-card");

        HorizontalLayout contentLayout = new HorizontalLayout();
        contentLayout.addClassName("stat-card-content");
        contentLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        contentLayout.setSpacing(true);

        if (ring != null) {
            contentLayout.add(ring);
        }

        VerticalLayout textGroup = new VerticalLayout();
        textGroup.setPadding(false);
        textGroup.setSpacing(false);
        textGroup.addClassName("stat-card-text-group");

        Paragraph labelElement = new Paragraph(label);
        labelElement.addClassNames("stat-card-label", "font-ui", "text-muted");
        textGroup.add(labelElement);

        HorizontalLayout valueRow = new HorizontalLayout();
        valueRow.addClassName("stat-card-value-row");
        valueRow.setAlignItems(FlexComponent.Alignment.BASELINE);
        valueRow.setSpacing(true);

        Span valueSpan = new Span(monoValue);
        valueSpan.addClassNames("stat-card-value", "font-mono", "text-navy");
        valueRow.add(valueSpan);

        if (auxiliary != null) {
            valueRow.add(auxiliary);
        }

        textGroup.add(valueRow);
        contentLayout.add(textGroup);

        add(contentLayout);
    }

    public StatCard(String label, String monoValue) {
        this(label, monoValue, null, null);
    }

    public StatCard(String label, String monoValue, Component ring) {
        this(label, monoValue, ring, null);
    }
}
