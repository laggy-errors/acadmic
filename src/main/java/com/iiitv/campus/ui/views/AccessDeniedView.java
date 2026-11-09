package com.iiitv.campus.ui.views;

import com.iiitv.campus.security.UserSession;
import com.iiitv.campus.ui.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "access-denied", layout = MainLayout.class)
@PageTitle("Access Denied (403) - Campus Portal")
public class AccessDeniedView extends VerticalLayout {

    public AccessDeniedView(UserSession userSession) {
        addClassName("view-container");
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        Div card = new Div();
        card.addClassNames("campus-card", "access-denied-card");

        Span statusPill = new Span("HTTP 403 · FORBIDDEN");
        statusPill.addClassNames("chip", "chip-short");

        H1 title = new H1("Restricted Area");
        title.addClassNames("text-navy", "font-ui");

        Paragraph description = new Paragraph(
                "Your account is currently signed in under the role: " + userSession.getCurrentRole().getDisplayName() +
                ". You do not have permission to view the requested destination."
        );
        description.addClassNames("text-muted", "font-ui");

        Paragraph guidance = new Paragraph("Select an authorized view or return to your designated portal dashboard.");
        guidance.addClassNames("text-main", "font-ui");

        HorizontalLayout actions = new HorizontalLayout();
        actions.setSpacing(true);

        Button returnBtn = new Button("Return to Dashboard", e -> getUI().ifPresent(ui -> ui.navigate("")));
        returnBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        actions.add(returnBtn);

        card.add(statusPill, title, description, guidance, actions);
        add(card);
    }
}
