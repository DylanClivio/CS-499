<%@ taglib uri="http://jakarta.apache.org/taglibs/standard-1.2" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search Results</title>
</head>
<body>
    <h1>Search Results</h1>

    <h2>Appointments</h2>
    <c:forEach var="appointment" items="${appointments}">
        <p>Appointment ID: ${appointment.appointmentId}, Date: ${appointment.appointmentDate}, Description: ${appointment.description}</p>
    </c:forEach>

    <h2>Tasks</h2>
    <c:forEach var="task" items="${tasks}">
        <p>Task ID: ${task.taskId}, Name: ${task.name}, Description: ${task.description}</p>
    </c:forEach>

    <h2>Contacts</h2>
    <c:forEach var="contact" items="${contacts}">
        <p>Contact ID: ${contact.contactId}, Name: ${contact.firstName} ${contact.lastName}, Phone: ${contact.phone}, Address: ${contact.address}</p>
    </c:forEach>
</body>
</html>