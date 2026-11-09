package com.iiitv.campus.ui.layout;

import com.iiitv.campus.security.Role;
import com.iiitv.campus.security.UserSession;
import com.iiitv.campus.ui.components.SearchDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.RouterLayout;

public class MainLayout extends Div implements RouterLayout, AfterNavigationObserver {

    private final UserSession userSession;
    private final SearchDialog searchDialog = new SearchDialog();
    private final Div pageFrame = new Div();

    private MenuItem todayMenu;
    private MenuItem academicMenu;
    private MenuItem campusMenu;
    private boolean isHindi = false;
    private Button notificationsBtn;
    private int unreadNotificationsCount = 3;

    public MainLayout(UserSession userSession) {
        this.userSession = userSession;
        addClassName("neo-app-layout");
        setWidthFull();

        // Top Shell Container (Utility Bar + Institute Header + Main Nav Bar)
        VerticalLayout shellTop = new VerticalLayout();
        shellTop.setPadding(false);
        shellTop.setSpacing(false);
        shellTop.setWidthFull();
        shellTop.addClassName("neo-shell-top-container");

        shellTop.add(
                createUtilityBar(),
                createInstituteHeader(),
                createMainNavBar()
        );

        // Page Frame: Centered glass container for router content
        pageFrame.addClassName("neo-glass-page-frame");

        add(shellTop, pageFrame);

        // Register Global Keyboard Shortcut (Ctrl+K and Cmd+K)
        Shortcuts.addShortcutListener(this, searchDialog::open, Key.KEY_K, KeyModifier.CONTROL);
        Shortcuts.addShortcutListener(this, searchDialog::open, Key.KEY_K, KeyModifier.META);
    }

    @Override
    public void showRouterLayoutContent(HasElement content) {
        pageFrame.removeAll();
        if (content != null) {
            pageFrame.getElement().appendChild(content.getElement());
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        String path = event.getLocation().getPath();
        updateActiveNavGroup(path);
    }

    private void updateActiveNavGroup(String path) {
        if (todayMenu == null || academicMenu == null || campusMenu == null) return;

        todayMenu.removeClassName("active");
        academicMenu.removeClassName("active");
        campusMenu.removeClassName("active");

        if (path.isEmpty() || path.equals("timetable") || path.equals("tasks") || path.equals("profile")) {
            todayMenu.addClassName("active");
        } else if (path.equals("attendance") || path.equals("classrooms") || path.equals("faculty-directory") || path.equals("remarks") || path.contains("faculty-") || path.contains("admin-")) {
            academicMenu.addClassName("active");
        } else if (path.equals("events") || path.equals("announcements") || path.equals("clubs") || path.equals("mess-menu") || path.equals("suggestions")) {
            campusMenu.addClassName("active");
        }
    }

    // A) UTILITY BAR (top, full width, solid #660000, white text, ~34px)
    private Component createUtilityBar() {
        HorizontalLayout bar = new HorizontalLayout();
        bar.addClassName("neo-utility-bar");
        bar.setWidthFull();
        bar.setPadding(false);
        bar.setSpacing(false);
        bar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        bar.setAlignItems(FlexComponent.Alignment.CENTER);

        // Left: "A- | A | A+", "English / हिन्दी", "Academic Portal", "Library", "Help"
        HorizontalLayout leftGroup = new HorizontalLayout();
        leftGroup.addClassName("neo-util-left");
        leftGroup.setAlignItems(FlexComponent.Alignment.CENTER);
        leftGroup.setSpacing(true);

        HorizontalLayout fontGroup = new HorizontalLayout();
        fontGroup.addClassName("neo-font-scaler-group");
        fontGroup.setSpacing(false);
        fontGroup.setAlignItems(FlexComponent.Alignment.CENTER);

        Button btnDec = new Button("A-", e -> adjustFontSize(-1));
        btnDec.addClassNames("neo-util-btn", "font-ui");
        btnDec.getElement().setAttribute("aria-label", "Decrease font size");

        Span sep1 = new Span("|");
        sep1.addClassNames("neo-util-sep");

        Button btnReset = new Button("A", e -> adjustFontSize(0));
        btnReset.addClassNames("neo-util-btn", "font-ui");
        btnReset.getElement().setAttribute("aria-label", "Reset font size");

        Span sep2 = new Span("|");
        sep2.addClassNames("neo-util-sep");

        Button btnInc = new Button("A+", e -> adjustFontSize(1));
        btnInc.addClassNames("neo-util-btn", "font-ui");
        btnInc.getElement().setAttribute("aria-label", "Increase font size");

        fontGroup.add(btnDec, sep1, btnReset, sep2, btnInc);

        Button langBtn = new Button("English / हिन्दी", e -> toggleLanguage());
        langBtn.addClassNames("neo-util-btn", "font-ui");
        langBtn.getElement().setAttribute("aria-label", "Toggle language between English and Hindi");

        Anchor academicLink = new Anchor("https://iiitvadodara.ac.in", "Academic Portal");
        academicLink.addClassNames("neo-util-link", "font-ui");

        Anchor libraryLink = new Anchor("#", "Library");
        libraryLink.addClassNames("neo-util-link", "font-ui");

        Anchor helpLink = new Anchor("#", "Help");
        helpLink.addClassNames("neo-util-link", "font-ui");

        leftGroup.add(fontGroup, langBtn, academicLink, libraryLink, helpLink);

        // Right: "Notifications · 3", avatar circle (#ffc107, "R"), "Raghav A.", "Sign out"
        HorizontalLayout rightGroup = new HorizontalLayout();
        rightGroup.addClassName("neo-util-right");
        rightGroup.setAlignItems(FlexComponent.Alignment.CENTER);
        rightGroup.setSpacing(true);

        notificationsBtn = new Button("Notifications · " + unreadNotificationsCount, e -> openNotificationsDialog());
        notificationsBtn.addClassNames("neo-util-notifications-btn", "font-ui");
        notificationsBtn.getElement().setAttribute("aria-label", "View notifications, " + unreadNotificationsCount + " unread items");

        Div avatar = new Div();
        avatar.addClassNames("neo-util-avatar", "font-ui");
        avatar.setText("R");
        avatar.getElement().setAttribute("aria-label", "View user profile");
        avatar.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("profile")));

        Span nameSpan = new Span("Raghav A.");
        nameSpan.addClassNames("neo-util-name", "font-ui");
        nameSpan.getElement().setAttribute("role", "button");
        nameSpan.getElement().setAttribute("tabindex", "0");
        nameSpan.getElement().setAttribute("aria-label", "View user profile for Raghav A.");
        nameSpan.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("profile")));

        // Role Switcher menu for paired development & testing
        MenuBar roleMenuBar = new MenuBar();
        roleMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY);
        roleMenuBar.addClassNames("neo-util-role-menu", "font-ui");
        MenuItem roleItem = roleMenuBar.addItem("Role (" + userSession.getCurrentRole().getDisplayName() + ")");
        roleItem.getSubMenu().addItem("Student (Raghav A.)", e -> switchRole(Role.STUDENT));
        roleItem.getSubMenu().addItem("Faculty (Dr. A. Mehta)", e -> switchRole(Role.FACULTY));
        roleItem.getSubMenu().addItem("Admin (Academic Office)", e -> switchRole(Role.ADMIN));

        Button signOutBtn = new Button("Sign out", e -> {
            Notification.show("Signed out of session. Switched to default Student view.");
            switchRole(Role.STUDENT);
        });
        signOutBtn.addClassNames("neo-util-signout-btn", "font-ui");
        signOutBtn.getElement().setAttribute("aria-label", "Sign out");

        rightGroup.add(notificationsBtn, avatar, nameSpan, roleMenuBar, signOutBtn);

        bar.add(leftGroup, rightGroup);
        return bar;
    }

    // B) INSTITUTE HEADER BANNER (official banner on white-tinted glass panel)
    private Component createInstituteHeader() {
        Div panel = new Div();
        panel.addClassName("neo-banner-panel");

        HtmlComponent picture = new HtmlComponent("picture");
        picture.addClassName("neo-banner-picture");

        HtmlComponent source = new HtmlComponent("source");
        source.getElement().setAttribute("media", "(max-width: 767px)");
        source.getElement().setAttribute("srcset", "/themes/campus/brand/header-banner-compact.png");
        source.getElement().setAttribute("width", "156");
        source.getElement().setAttribute("height", "56");

        Image img = new Image("/themes/campus/brand/header-banner.png",
                "Indian Institute of Information Technology Vadodara. An Institution of National Importance under the Ministry of Education, Government of India");
        img.addClassName("neo-banner-img");
        img.setWidth("100%");
        img.getElement().setAttribute("width", "1024");
        img.getElement().setAttribute("height", "112");
        img.getElement().setAttribute("loading", "eager");

        picture.getElement().appendChild(source.getElement(), img.getElement());
        panel.add(picture);
        return panel;
    }

    // C) MAIN NAV BAR (glass pill-bar under the header)
    private Component createMainNavBar() {
        HorizontalLayout navBar = new HorizontalLayout();
        navBar.addClassName("neo-main-nav-bar");
        navBar.setWidthFull();
        navBar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        navBar.setAlignItems(FlexComponent.Alignment.CENTER);

        // Mobile Hamburger Toggle
        Button mobileMenuToggle = new Button("☰ Menu", e -> openMobileMenuDialog());
        mobileMenuToggle.addClassNames("neo-mobile-menu-btn", "font-ui");
        mobileMenuToggle.getElement().setAttribute("aria-label", "Open all navigation destinations");

        // Three dropdown groups, centred: Today / Academic / Campus, each with a ▾
        MenuBar menuBar = new MenuBar();
        menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY);
        menuBar.addClassNames("neo-main-menubar", "font-ui");

        todayMenu = menuBar.addItem("Today ▾");
        todayMenu.addClassNames("neo-nav-group-item", "neo-nav-today");
        todayMenu.getSubMenu().addItem("Dashboard", e -> getUI().ifPresent(ui -> ui.navigate("")));
        todayMenu.getSubMenu().addItem("Timetable", e -> getUI().ifPresent(ui -> ui.navigate("timetable")));
        todayMenu.getSubMenu().addItem("Tasks", e -> getUI().ifPresent(ui -> ui.navigate("tasks")));
        todayMenu.getSubMenu().addItem("Profile", e -> getUI().ifPresent(ui -> ui.navigate("profile")));

        academicMenu = menuBar.addItem("Academic ▾");
        academicMenu.addClassNames("neo-nav-group-item", "neo-nav-academic");
        academicMenu.getSubMenu().addItem("Attendance", e -> getUI().ifPresent(ui -> ui.navigate("attendance")));
        academicMenu.getSubMenu().addItem("Classrooms", e -> getUI().ifPresent(ui -> ui.navigate("classrooms")));
        academicMenu.getSubMenu().addItem("Faculty Directory", e -> getUI().ifPresent(ui -> ui.navigate("faculty-directory")));
        academicMenu.getSubMenu().addItem("Remarks", e -> getUI().ifPresent(ui -> ui.navigate("remarks")));

        campusMenu = menuBar.addItem("Campus ▾");
        campusMenu.addClassNames("neo-nav-group-item", "neo-nav-campus");
        campusMenu.getSubMenu().addItem("Events", e -> getUI().ifPresent(ui -> ui.navigate("events")));
        campusMenu.getSubMenu().addItem("Announcements", e -> getUI().ifPresent(ui -> ui.navigate("announcements")));
        campusMenu.getSubMenu().addItem("Clubs & Committees", e -> getUI().ifPresent(ui -> ui.navigate("clubs")));
        campusMenu.getSubMenu().addItem("Mess Menu", e -> getUI().ifPresent(ui -> ui.navigate("mess-menu")));
        campusMenu.getSubMenu().addItem("Suggestion Box", e -> getUI().ifPresent(ui -> ui.navigate("suggestions")));

        // Right side: rounded white search field "Search courses, events, faculty... Ctrl+K"
        Button searchTrigger = new Button("Search courses, events, faculty... Ctrl+K");
        searchTrigger.addClassNames("neo-nav-search-btn", "font-ui");
        searchTrigger.getElement().setAttribute("aria-label", "Search courses, events, faculty, rooms. Shortcut: Ctrl+K");
        searchTrigger.addClickListener(e -> searchDialog.open());

        navBar.add(mobileMenuToggle, menuBar, searchTrigger);
        return navBar;
    }

    private void adjustFontSize(int direction) {
        String script = switch (direction) {
            case -1 -> "document.documentElement.style.fontSize = '14px';";
            case 1 -> "document.documentElement.style.fontSize = '18px';";
            default -> "document.documentElement.style.fontSize = '16px';";
        };
        getUI().ifPresent(ui -> ui.getPage().executeJs(script));
    }

    private void toggleLanguage() {
        isHindi = !isHindi;
        Notification.show(isHindi ? "भाषा बदलकर हिन्दी कर दी गई है" : "Language set to English", 2000, Notification.Position.BOTTOM_CENTER);
    }

    private void switchRole(Role role) {
        userSession.setCurrentRole(role);
        getUI().ifPresent(ui -> ui.navigate(""));
    }

    private void openMobileMenuDialog() {
        Dialog dialog = new Dialog();
        dialog.addClassName("neo-mobile-dialog");
        dialog.setHeaderTitle("All Destinations");
        dialog.setWidth("380px");
        dialog.setMaxWidth("92vw");

        VerticalLayout sheet = new VerticalLayout();
        sheet.setPadding(false);
        sheet.setSpacing(true);

        sheet.add(createMobileSectionTitle("TODAY"));
        sheet.add(createMobileNavLink("Dashboard", "", dialog));
        sheet.add(createMobileNavLink("Timetable", "timetable", dialog));
        sheet.add(createMobileNavLink("Tasks", "tasks", dialog));

        sheet.add(createMobileSectionTitle("ACADEMIC"));
        sheet.add(createMobileNavLink("Attendance", "attendance", dialog));
        sheet.add(createMobileNavLink("Classrooms", "classrooms", dialog));
        sheet.add(createMobileNavLink("Faculty Directory", "faculty-directory", dialog));
        sheet.add(createMobileNavLink("Remarks", "remarks", dialog));

        sheet.add(createMobileSectionTitle("CAMPUS"));
        sheet.add(createMobileNavLink("Events", "events", dialog));
        sheet.add(createMobileNavLink("Announcements", "announcements", dialog));
        sheet.add(createMobileNavLink("Clubs & Committees", "clubs", dialog));
        sheet.add(createMobileNavLink("Mess Menu", "mess-menu", dialog));
        sheet.add(createMobileNavLink("Suggestion Box", "suggestions", dialog));

        sheet.add(createMobileSectionTitle("ACCOUNT"));
        sheet.add(createMobileNavLink("My Profile", "profile", dialog));

        dialog.add(sheet);
        Button close = new Button("Close", e -> dialog.close());
        dialog.getFooter().add(close);
        dialog.open();
    }

    private Div createMobileSectionTitle(String title) {
        Div heading = new Div();
        heading.setText(title);
        heading.addClassNames("neo-mobile-section-heading", "font-mono");
        return heading;
    }

    private Button createMobileNavLink(String title, String route, Dialog dialog) {
        Button btn = new Button(title, e -> {
            dialog.close();
            getUI().ifPresent(ui -> ui.navigate(route));
        });
        btn.addClassNames("neo-mobile-link-btn", "font-ui");
        btn.setWidthFull();
        return btn;
    }

    private void openNotificationsDialog() {
        Dialog dialog = new Dialog();
        dialog.addClassNames("neo-glass-dialog", "neo-notifications-dialog");
        dialog.setHeaderTitle("Notifications");
        dialog.setWidth("520px");
        dialog.setMaxWidth("92vw");

        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);
        content.setSpacing(true);

        HorizontalLayout headerBar = new HorizontalLayout();
        headerBar.setWidthFull();
        headerBar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        headerBar.setAlignItems(FlexComponent.Alignment.CENTER);

        Span unreadChip = new Span(unreadNotificationsCount > 0 ? unreadNotificationsCount + " UNREAD" : "ALL READ");
        unreadChip.addClassNames(unreadNotificationsCount > 0 ? "neo-chip-gold" : "neo-chip-mint", "font-mono");

        Button markReadBtn = new Button("Mark all read", e -> {
            unreadNotificationsCount = 0;
            notificationsBtn.setText("Notifications · 0");
            unreadChip.setText("ALL READ");
            unreadChip.removeClassName("neo-chip-gold");
            unreadChip.addClassName("neo-chip-mint");
            content.getChildren().forEach(comp -> {
                comp.getElement().getChildren().forEach(ch -> {
                    ch.getChildren().forEach(inner -> {
                        if ("neo-gold-dot".equals(inner.getAttribute("class"))) {
                            inner.getStyle().set("display", "none");
                        }
                    });
                });
            });
            Notification.show("All notifications marked as read.", 2500, Notification.Position.BOTTOM_END);
        });
        markReadBtn.addClassNames("neo-btn-outline", "font-ui");
        markReadBtn.getStyle().set("font-size", "12px");
        markReadBtn.getStyle().set("min-height", "32px");

        headerBar.add(unreadChip, markReadBtn);
        content.add(headerBar);

        Div n1 = createNotificationRow("Academic", "Mid-Sem examination schedule published for Semester V.", "20 NOV 10:30", unreadNotificationsCount > 0);
        Div n2 = createNotificationRow("Classroom", "Dr. Amit Mehta posted Assignment 2 in Data Structures.", "06 NOV 14:15", unreadNotificationsCount > 0);
        Div n3 = createNotificationRow("Attendance", "Attendance advisory: You are currently SAFE with 82.5%.", "TODAY 08:00", unreadNotificationsCount > 0);

        content.add(n1, n2, n3);
        dialog.add(content);

        Button close = new Button("Close", e -> dialog.close());
        close.addClassNames("neo-btn-maroon", "font-ui");
        dialog.getFooter().add(close);

        dialog.open();
    }

    private Div createNotificationRow(String category, String message, String monoTime, boolean isUnread) {
        Div row = new Div();
        row.addClassNames("neo-notification-item", "notification-item");

        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout left = new HorizontalLayout();
        left.setAlignItems(FlexComponent.Alignment.CENTER);
        left.setSpacing(true);

        if (isUnread) {
            Span goldDot = new Span();
            goldDot.addClassName("neo-gold-dot");
            left.add(goldDot);
        }

        Span cat = new Span(category.toUpperCase());
        cat.addClassNames("chip", "font-mono");
        if ("ACADEMIC".equalsIgnoreCase(category)) {
            cat.addClassName("neo-chip-rose");
        } else if ("CLASSROOM".equalsIgnoreCase(category)) {
            cat.addClassName("neo-chip-sky");
        } else {
            cat.addClassName("neo-chip-mint");
        }
        left.add(cat);

        Span t = new Span(monoTime);
        t.addClassNames("neo-notification-time", "font-mono", "notification-time");

        layout.add(left, t);

        Paragraph text = new Paragraph(message);
        text.addClassNames("neo-notification-text", "font-ui", "text-main", "notification-message");

        row.add(layout, text);
        return row;
    }
}
