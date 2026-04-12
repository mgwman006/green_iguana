# green_iguana

# Backend
- Added Spring Boot Actuator for basic health monitoring and service observability.

# At application startup:

Generate Sections from existing DB data (Course + Teacher + Room + Patterns)


Algorithn:

For each course:
    Read hoursPerWeek
    Get patterns from your TimeSlotProvider
    Find:
        compatible teachers
        compatible classrooms
    Create Sections
    Attach TimeSlots
    Save to DB