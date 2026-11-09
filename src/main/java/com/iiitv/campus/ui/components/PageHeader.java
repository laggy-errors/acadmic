package com.iiitv.campus.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

public class PageHeader extends Div {

    private final H1 titleHeading = new H1();
    private final Paragraph subtitleParagraph = new Paragraph();
    private final HorizontalLayout actionsSlot = new HorizontalLayout();
    private final HorizontalLayout breadcrumbBar = new HorizontalLayout();

    public PageHeader(String title, String subtitle, Component primaryAction, List<String> breadcrumbs) {
        addClassName("page-header");

        VerticalLayout mainColumn = new VerticalLayout();
        mainColumn.setPadding(false);
        mainColumn.setSpacing(false);
        mainColumn.setWidthFull();

        // Breadcrumbs Row
        if (breadcrumbs != null && !breadcrumbs.isEmpty()) {
            breadcrumbBar.addClassName("page-header-breadcrumbs");
            breadcrumbBar.setSpacing(true);
            breadcrumbBar.setAlignItems(FlexComponent.Alignment.CENTER);

            for (int i = 0; i < breadcrumbs.size(); i++) {
                Span crumb = new Span(breadcrumbs.get(i));
                crumb.addClassNames("breadcrumb-item", "font-ui");
                if (i == breadcrumbs.size() - 1) {
                    crumb.addClassName("breadcrumb-current");
                } else {
                    crumb.addClassName("breadcrumb-parent");
                }
                breadcrumbBar.add(crumb);

                if (i < breadcrumbs.size() - 1) {
                    Span separator = new Span("›");
                    separator.addClassNames("breadcrumb-separator", "font-ui");
                    breadcrumbBar.add(separator);
                }
            }
            mainColumn.add(breadcrumbBar);
        }

        HorizontalLayout mainRow = new HorizontalLayout();
        mainRow.addClassName("page-header-row");
        mainRow.setWidthFull();
        mainRow.setAlignItems(FlexComponent.Alignment.CENTER);
        mainRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        VerticalLayout textGroup = new VerticalLayout();
        textGroup.setPadding(false);
        textGroup.setSpacing(false);
        textGroup.addClassName("page-header-text");

        titleHeading.setText(title);
        titleHeading.addClassNames("page-header-title", "font-ui", "text-navy");
        textGroup.add(titleHeading);

        if (subtitle != null && !subtitle.isBlank()) {
            subtitleParagraph.setText(subtitle);
            subtitleParagraph.addClassNames("page-header-subtitle", "font-ui", "text-muted");
            textGroup.add(subtitleParagraph);
        }

        actionsSlot.addClassName("page-header-actions");
        actionsSlot.setSpacing(true);
        actionsSlot.setAlignItems(FlexComponent.Alignment.CENTER);

        if (primaryAction != null) {
            actionsSlot.add(primaryAction);
        }

        mainRow.add(textGroup, actionsSlot);
        mainColumn.add(mainRow);
        add(mainColumn);
    }

    public PageHeader(String title, String subtitle, Component primaryAction) {
        this(title, subtitle, primaryAction, null);
    }

    public PageHeader(String title, String subtitle) {
        this(title, subtitle, null, null);
    }

    public PageHeader(String title) {
        this(title, null, null, null);
    }

    public void addAction(Component component) {
        actionsSlot.add(component);
    }
}
