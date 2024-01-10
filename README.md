# Futsal Booking System

## Project Overview

The primary objective of this Java project is to simplify the process of booking futsal.

### User, Futsal Owner, Admin Authentication

- Users, Futsal Owners, and Admins can easly sign up, sign in, and log out of the system.

### User Functionality

- Users are able to book futsal slots, cancel bookings, and access a list of available futsals slots.

### Admin Authority

- Admins have the ability to view lists of users, futsal owners, and futsals which are registered in the application.

### Futsal Owner Registration Workflow

- Futsal Owners have to send the futsal registration request to the Admin for approval.

### Admin Approval Process

- Admins have the ability to accept or reject futsal registration requests submitted by Futsal Owners.

## Git Pull Request Strategy

For the most part, I've done in a linear approach by creating new branches for each feature. This helped me to easy to code without effecting the main branch .Once the feature is completed.I pushed the branch to GitHub. Created a pull request to merge with main branch.After merging the feature branch into main branch in github. I update the local main branch by pulling the latest changes from the GitHub.

## Challenges Faced

While the most of the time I worked on linear branching strategy,In the middle of the project I have created a 'jwt_feature' branch which I worked alot. While merging the 'jwt_feature' branch there was a merge conflict becouse at that time 'main' branch was merged with another branche.

## Overcoming Challenges

To tackle this challenge, I resolved the Merge Conflict by pulling the main branch of GitHub into the local main branch. Then 'jwt_feature' branch was merged with main branch, and conflicts were successfully resolved by removing unnecessary code then I pushed into GitHub, and creted a Pull Request,and merged 'jwt_feature' branch into the main branch.

