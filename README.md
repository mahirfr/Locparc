# Locparc - Equipment Rental Management App

## Overview

Locparc is an equipment rental management application developed as part of a final project. The application is designed for the IFA (Institut Français des Affaires) and MNS (Metz Numeric School) to optimize the management of their equipment, used in various scenarios like events, classes, and loans. Locparc provides both web and mobile interfaces that allow users to reserve, rent, and track equipment in real-time.

## Features

- **User Management:** Manage access rights, user creation, modification, and deletion.
- **Equipment Management:** Add and categorize equipment, manage maintenance, and track history.
- **Reservation & Rental:** Real-time availability and reservation validation.
- **Planning:** Real-time equipment availability and booking management.
- **Notifications:** Alerts for overdue returns, maintenance needs, and more.

## Technology Stack

- **Backend:** Java with Spring Boot
- **Frontend:** Angular
- **Mobile:** React Native (planned)
- **Database:** MySQL
- **API Testing:** Postman
- **Project Management:** Scrum, Gantt chart, Kanban board
  

## Project Structure

- **Backend:**
  - `src/main/java`: Java source code
  - `src/main/resources`: Configuration files and static resources

- **Frontend:**
  - `src/app`: Angular components, services, and modules
  - `src/assets`: Images and other assets

## Functional Specifications

### User Management

- **Roles:** Administrator, Lender, User
- **Permissions:** Vary by role, including the ability to add, modify, and delete equipment or users.

### Equipment Management

- **Categorization:** Equipment is categorized and sub-categorized for easy management.
- **Maintenance:** Equipment can be marked for maintenance, removing it from availability.

### Reservation & Rental Management

- **Validation:** Reservations are validated by the administrator or lender.
- **Contract Management:** Contracts include start/end dates, user information, and can be extended with administrator approval.

### Notifications

- **Types:** Alerts for expired contracts, maintenance needs, etc.
- **Delivery:** Via email to users and administrators.

## Technical Specifications

### Database

- **Technology:** MySQL with phpMyAdmin for management
- **Structure:** Relational database with normalized tables and foreign keys to ensure data integrity.

### Backend

- **Framework:** Spring Boot
- **API:** RESTful API with Spring Security for authentication and authorization.
- **ORM:** Hibernate with JPA

### Frontend

- **Framework:** Angular
- **Design:** Angular Material for UI components

### Mobile Application

- **Framework:** The initial choice was ReactNative but after some thought I would've chosen Flutter.

#### For a more detailed look download the pdf file (only thing is... it's in french as for the project requirememnts).
