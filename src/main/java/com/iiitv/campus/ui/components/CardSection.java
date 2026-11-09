package com.iiitv.campus.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

public class CardSection extends Div {

    private final VerticalLayout body = new VerticalLayout();

    public CardSection(String title, String viewAllText, Class<? extends Component> viewAllTarget, Component... content) {
        addClassNames("campus-card", "card-section");

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("card-section-header");
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        H2 titleElement = new H2(title);
        titleElement.addClassNames("card-section-title", "font-ui", "text-navy");
        header.add(titleElement);

        if (viewAllText != null && !viewAllText.isBlank() && viewAllTarget != null) {
            RouterLink link = new RouterLink(viewAllText, viewAllTarget);
            link.addClassNames("card-section-link", "font-ui", "text-blue");
            header.add(link);
        }

        body.addClassName("card-section-body");
        body.setPadding(false);
        body.setSpacing(true);
        body.setWidthFull();

        if (content != null && content.length > 0) {
            body.add(content);
        }

        add(header, body);
    }

    public CardSection(String title, Component... content) {
        this(title, null, null, content);
    }

    public void addContent(Component... components) {
        body.add(components);
    }
}
