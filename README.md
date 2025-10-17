# TripTracker - Vacation Management Application

## Purpose
TripTracker is an Android mobile application designed to help users organize and manage their vacation plans. Users can create multiple trips with lodging details, set date-based alerts, and manage excursions/events associated with each trip. The application provides sharing capabilities to easily send trip details to others via email, SMS, or clipboard.

## Android Version
**Deployment**: Android 8.0 (API 26) and higher  
**Target SDK**: Android 14 (API 34)

## Features
- Create, edit, and delete vacation trips
- Add multiple events/excursions to each trip
- Date validation (end dates after start dates, events within trip dates)
- Set alerts for trip start/end dates and individual events
- Share complete trip details via email, SMS, or clipboard
- Visual overview dashboard showing all trips and events
- Room Database for local data persistence

## How to Use the Application

### Getting Started
1. Launch TripTracker
2. You'll see the **Home Screen** with an overview of all your vacations

### Managing Vacations

#### Adding a New Vacation
1. From the Home Screen, tap the **+ (FAB) button** in the bottom right
2. This takes you to the Trip List screen
3. Tap the **+ button** again
4. Fill in the required fields:
    - **Trip Name**: e.g., "Bermuda Trip", "Spring Break"
    - **Lodging**: Hotel or accommodation name
    - **Start Date**: Tap the field to open date picker
    - **End Date**: Tap the field to open date picker (must be on or after start date)
5. Click **Save**

#### Editing a Vacation
1. From the Home Screen, **tap any vacation card**
2. Modify any fields
3. Click **Save**

#### Deleting a Vacation
1. Open the vacation details
2. Click **Delete**
3. **Note**: You cannot delete a vacation if it has events - delete all events first
4. Confirm deletion

#### Setting Alerts
1. Open vacation details (must be saved first)
2. Click **Set Start Date Alert** - notification triggers at 8:00 AM on start date
3. Click **Set End Date Alert** - notification triggers at 8:00 AM on end date
4. **Note**: Cannot set alerts for past dates

#### Sharing Vacation Details
1. Open vacation details
2. Click **Share Trip Details**
3. Choose sharing method (Email, SMS, Copy to clipboard, etc.)
4. The message includes trip name, lodging, dates, and all events

### Managing Events/Excursions

#### Adding Events to a Vacation
1. Open vacation details
2. Click **View Events**
3. Tap the **+ (FAB) button**
4. Fill in:
    - **Event Title**: e.g., "Snorkeling", "Hiking", "Bus Tour"
    - **Event Date**: Must be within the vacation's start and end dates
5. Click **Save**

#### Editing an Event
1. From Event List, tap the event
2. Modify title or date
3. Click **Save**

#### Deleting an Event
1. Open event details
2. Click **Delete**
3. Confirm deletion

#### Setting Event Alerts
1. Open event details (must be saved first)
2. Click **Set Event Alert**
3. Notification triggers at 8:00 AM on the event date

## Accessing All Required Features

- **Home Screen**: Launch app (shows vacation overview with events)
- **Vacation List**: Home → Tap FAB button
- **Add Vacation**: Vacation List → Tap FAB → Fill form → Save
- **Edit Vacation**: Home → Tap vacation card → Modify → Save
- **Delete Vacation**: Vacation Details → Delete (only if no events)
- **View Events**: Vacation Details → "View Events" button
- **Add Event**: Event List → Tap FAB → Fill form → Save
- **Edit Event**: Event List → Tap event → Modify → Save
- **Delete Event**: Event Details → Delete → Confirm
- **Date Validation**: Automatic (date pickers enforce valid ranges)
- **Set Alerts**: Trip/Event Details → "Set Alert" buttons
- **Share Trip**: Trip Details → "Share Trip Details" button

## GitLab Repository
**Repository URL**: https://gitlab.com/wgu-gitlab-environment/student-repos/anniefees/d308-mobile-application-development-android.git

## Technical Details
- **Database**: Room Framework (abstraction layer over SQLite)
- **Minimum SDK**: API 26 (Android 8.0 Oreo)
- **Target SDK**: API 34 (Android 14)
- **Architecture**: MVVM with Repository pattern
- **Key Components**:
    - Room Database with Trip and Event entities
    - Foreign key relationships
    - Date validation logic
    - Alert scheduling with AlarmManager
    - Share functionality with Intent chooser

## Project Structure
```
com.annief.tracker/
├── data/
│   ├── dao/           # Database access objects
│   ├── db/            # Database configuration
│   ├── entity/        # Data models (Trip, Event)
│   └── repo/          # Repository pattern implementation
├── ui/
│   ├── adapter/       # RecyclerView adapters
│   ├── event/         # Event/Excursion screens
│   ├── home/          # Home screen
│   └── trip/          # Trip/Vacation screens
└── util/              # Helper classes (Alerts)
```

---
**Version**: 1.0  
**Last Updated**: October 2025