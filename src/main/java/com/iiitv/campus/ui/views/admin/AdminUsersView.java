package com.iiitv.campus.ui.views.admin;

import com.iiitv.campus.security.RequiresRole;
import com.iiitv.campus.security.Role;
import com.iiitv.campus.ui.components.PageHeader;
import com.iiitv.campus.ui.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "admin/users", layout = MainLayout.class)
@PageTitle("Admin User Management - IIIT Vadodara Campus Portal")
@RequiresRole({Role.ADMIN})
public class AdminUsersView extends VerticalLayout {

    private record UserEntry(String id, String name, String role, String email, String status) {}

    private final List<UserEntry> users = List.of(
            new UserEntry("202201045", "Raghav A.", "STUDENT", "raghav_202201045@iiitvadodara.ac.in", "ACTIVE"),
            new UserEntry("FAC-102", "Dr. A. Mehta", "FACULTY", "a.mehta@iiitvadodara.ac.in", "ACTIVE"),
            new UserEntry("FAC-108", "Dr. R. Iyer", "FACULTY", "r.iyer@iiitvadodara.ac.in", "ACTIVE"),
            new UserEntry("ADM-001", "Campus Registrar", "ADMIN", "registrar@iiitvadodara.ac.in", "ACTIVE")
    );

    public AdminUsersView() {
        addClassName("view-container");
        setSizeFull();
        setSpacing(true);
        setPadding(false);

        Button addUserBtn = new Button("Add User Account");
        addUserBtn.addClassNames("btn-primary", "font-ui");
        addUserBtn.getElement().setAttribute("theme", "primary");
        addUserBtn.addClickListener(e ->
                Notification.show("User provisioning modal opened.", 2500, Notification.Position.BOTTOM_END));

        PageHeader header = new PageHeader(
                "User Management",
                "Manage student profiles, faculty credentials, and role authorizations",
                addUserBtn
        );

        Div card = new Div();
        card.addClassNames("campus-card");

        Div list = new Div();
        list.setWidthFull();

        for (UserEntry u : users) {
            Div row = new Div();
            row.addClassName("classroom-task-row");

            HorizontalLayout left = new HorizontalLayout();
            left.setAlignItems(Alignment.CENTER);
            left.setSpacing(true);

            Span idChip = new Span(u.id());
            idChip.addClassNames("chip", "chip-neutral", "font-mono");

            Div info = new Div();
            info.addClassName("classroom-task-info");

            Span name = new Span(u.name());
            name.addClassNames("classroom-task-title", "font-ui");

            Span email = new Span(u.email());
            email.addClassNames("classroom-task-date", "font-mono");

            info.add(name, email);
            left.add(idChip, info);

            HorizontalLayout right = new HorizontalLayout();
            right.setAlignItems(Alignment.CENTER);
            right.setSpacing(true);

            Span roleChip = new Span(u.role());
            if ("ADMIN".equals(u.role())) {
                roleChip.addClassNames("chip", "chip-watch", "font-mono");
            } else if ("FACULTY".equals(u.role())) {
                roleChip.addClassNames("chip", "chip-neutral", "font-mono");
            } else {
                roleChip.addClassNames("chip", "chip-safe", "font-mono");
            }

            Span statusChip = new Span(u.status());
            statusChip.addClassNames("chip", "chip-safe", "font-mono");

            Button manageBtn = new Button("Manage");
            manageBtn.addClassNames("btn-secondary", "font-ui");
            manageBtn.addClickListener(e ->
                    Notification.show("Managing privileges for " + u.name(), 2000, Notification.Position.BOTTOM_END));

            right.add(roleChip, statusChip, manageBtn);
            row.add(left, right);
            list.add(row);
        }

        card.add(list);
        add(header, card);
    }
}
