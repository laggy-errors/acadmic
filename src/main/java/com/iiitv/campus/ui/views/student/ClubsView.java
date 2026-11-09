package com.iiitv.campus.ui.views.student;

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

@Route(value = "clubs", layout = MainLayout.class)
@PageTitle("Clubs & Committees - IIIT Vadodara Campus Portal")
@RequiresRole({Role.STUDENT, Role.FACULTY, Role.ADMIN})
public class ClubsView extends VerticalLayout {

    private record ClubInfo(
            String name,
            String category,
            String description,
            String lead,
            int members,
            String meetingSlot
    ) {}

    private final List<ClubInfo> clubs = List.of(
            new ClubInfo("Coding Club (dotSlash)", "Technical", "Competitive programming contests, algorithm discussions, and hackathon teams.", "Tanmay Sharma (Final Year)", 140, "Every Saturday 17:00"),
            new ClubInfo("Robotics & IoT Club", "Technical", "Hardware prototyping, embedded microcontrollers, ROS, and autonomous drones.", "Ananya Roy (Pre-Final Year)", 85, "Every Friday 17:30"),
            new ClubInfo("Design & Media Society", "Creative", "UI/UX, motion graphics, branding, and campus visual media coverage.", "Rohan Verma (Third Year)", 62, "Every Wednesday 16:30"),
            new ClubInfo("Literary & Debating Society", "Cultural", "Parliamentary debates, MUNs, creative writing, and annual college magazine.", "Isha Patel (Second Year)", 95, "Every Thursday 18:00")
    );

    public ClubsView() {
        addClassNames("view-container", "neo-clubs-view");
        setSizeFull();
        setSpacing(true);
        setPadding(false);

        PageHeader header = new PageHeader("Clubs & Committees", "Technical societies, cultural clubs, and student bodies");
        header.addClassName("neo-clubs-header");

        Div grid = new Div();
        grid.addClassNames("dashboard-row-2", "neo-clubs-grid");

        for (ClubInfo c : clubs) {
            Div card = new Div();
            card.addClassNames("campus-card", "stream-card", "neo-glass-card", "neo-club-card");

            HorizontalLayout top = new HorizontalLayout();
            top.setWidthFull();
            top.setJustifyContentMode(JustifyContentMode.BETWEEN);
            top.setAlignItems(Alignment.CENTER);
            top.addClassName("neo-club-card-top");

            H3 title = new H3(c.name());
            title.addClassNames("font-ui", "text-navy", "margin-none", "neo-club-name");

            Span catBadge = new Span(c.category().toUpperCase());
            catBadge.addClassNames("chip", "font-mono", "neo-club-category-chip", getCategoryClass(c.category()));

            top.add(title, catBadge);

            Paragraph desc = new Paragraph(c.description());
            desc.addClassNames("font-ui", "text-main", "neo-club-desc");

            Paragraph meta = new Paragraph("Lead: " + c.lead() + " · " + c.members() + " members");
            meta.addClassNames("font-ui", "text-muted", "neo-club-meta");

            Span meet = new Span("📅 " + c.meetingSlot());
            meet.addClassNames("font-mono", "text-muted", "neo-club-schedule");

            Button joinBtn = new Button("Join Club");
            joinBtn.addClassNames("btn-primary", "neo-btn-maroon", "neo-club-join-btn", "font-ui");
            joinBtn.setWidthFull();

            final boolean[] isJoined = {false};
            joinBtn.addClickListener(e -> {
                isJoined[0] = !isJoined[0];
                if (isJoined[0]) {
                    joinBtn.setText("Joined ✓");
                    joinBtn.removeClassName("neo-btn-maroon");
                    joinBtn.addClassNames("btn-rsvp-active", "neo-btn-rsvp-mint");
                    Notification.show("Membership request submitted for " + c.name(), 2500, Notification.Position.BOTTOM_END);
                } else {
                    joinBtn.setText("Join Club");
                    joinBtn.removeClassName("btn-rsvp-active");
                    joinBtn.removeClassName("neo-btn-rsvp-mint");
                    joinBtn.addClassNames("neo-btn-maroon");
                    Notification.show("Left " + c.name(), 2000, Notification.Position.BOTTOM_END);
                }
            });

            card.add(top, desc, meta, meet, joinBtn);
            grid.add(card);
        }

        add(header, grid);
    }

    private String getCategoryClass(String category) {
        if (category == null) return "neo-club-cat-tech";
        return switch (category.toLowerCase()) {
            case "technical" -> "neo-club-cat-tech";
            case "creative" -> "neo-club-cat-creative";
            case "cultural" -> "neo-club-cat-cultural";
            default -> "neo-club-cat-tech";
        };
    }
}
