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

@Route(value = "admin/courses", layout = MainLayout.class)
@PageTitle("Admin Course Management - IIIT Vadodara Campus Portal")
@RequiresRole({Role.ADMIN})
public class AdminCoursesView extends VerticalLayout {

    private record CourseOffering(String code, String name, int credits, String faculty, String room, int students) {}

    private final List<CourseOffering> courses = List.of(
            new CourseOffering("CS301", "Data Structures & Algorithms", 4, "Dr. A. Mehta", "ICD-2125", 64),
            new CourseOffering("CS302", "Operating Systems", 4, "Dr. R. Iyer", "ICD-2125", 64),
            new CourseOffering("CS303", "Computer Networks", 4, "Dr. S. Kulkarni", "ICD-2125", 64),
            new CourseOffering("CS304", "Database Management Systems", 4, "Dr. N. Patel", "ICD-2113", 64),
            new CourseOffering("CS305", "Theory of Computation", 3, "Dr. V. Sharma", "ICD-2125", 64)
    );

    public AdminCoursesView() {
        addClassName("view-container");
        setSizeFull();
        setSpacing(true);
        setPadding(false);

        Button addCourseBtn = new Button("Add New Course");
        addCourseBtn.addClassNames("btn-primary", "font-ui");
        addCourseBtn.getElement().setAttribute("theme", "primary");
        addCourseBtn.addClickListener(e ->
                Notification.show("Course creation dialog initialized.", 2500, Notification.Position.BOTTOM_END));

        PageHeader header = new PageHeader(
                "Course Management",
                "Curriculum structure, semester allocations, and course offerings",
                addCourseBtn
        );

        Div card = new Div();
        card.addClassNames("campus-card");

        Div list = new Div();
        list.setWidthFull();

        for (CourseOffering c : courses) {
            Div row = new Div();
            row.addClassName("classroom-task-row");

            HorizontalLayout left = new HorizontalLayout();
            left.setAlignItems(Alignment.CENTER);
            left.setSpacing(true);

            Span code = new Span(c.code());
            code.addClassNames("chip", "chip-neutral", "font-mono");

            Div info = new Div();
            info.addClassName("classroom-task-info");

            Span name = new Span(c.name());
            name.addClassNames("classroom-task-title", "font-ui");

            Span sub = new Span("Faculty: " + c.faculty() + " · Room " + c.room() + " · " + c.students() + " students");
            sub.addClassNames("classroom-task-date", "font-ui");

            info.add(name, sub);
            left.add(code, info);

            HorizontalLayout right = new HorizontalLayout();
            right.setAlignItems(Alignment.CENTER);
            right.setSpacing(true);

            Span creditsChip = new Span(c.credits() + " Credits");
            creditsChip.addClassNames("chip", "chip-safe", "font-mono");

            Button editBtn = new Button("Edit");
            editBtn.addClassNames("btn-secondary", "font-ui");
            editBtn.addClickListener(e ->
                    Notification.show("Editing " + c.code(), 2000, Notification.Position.BOTTOM_END));

            right.add(creditsChip, editBtn);
            row.add(left, right);
            list.add(row);
        }

        card.add(list);
        add(header, card);
    }
}
