document.addEventListener('DOMContentLoaded', function() {
    const taskForm = document.getElementById('taskForm');
    const appointmentForm = document.getElementById('appointmentForm');
    const contactForm = document.getElementById('contactForm');

    taskForm.addEventListener('submit', function(event) {
        event.preventDefault();
        const taskName = document.getElementById('taskName').value;
        const taskDescription = document.getElementById('taskDescription').value;

        fetch('/tasks', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ taskName, taskDescription })
        })
        .then(response => response.text())
        .then(data => alert(data))
        .catch(error => console.error('Error:', error));
    });

    appointmentForm.addEventListener('submit', function(event) {
        event.preventDefault();
        const appointmentId = document.getElementById('appointmentId').value;
        const appointmentDate = document.getElementById('appointmentDate').value;
        const description = document.getElementById('description').value;

        fetch('/appointments', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ appointmentId, appointmentDate, description })
        })
        .then(response => response.text())
        .then(data => alert(data))
        .catch(error => console.error('Error:', error));
    });

    contactForm.addEventListener('submit', function(event) {
        event.preventDefault();
        const contactId = document.getElementById('contactId').value;
        const firstName = document.getElementById('firstName').value;
        const lastName = document.getElementById('lastName').value;
        const phone = document.getElementById('phone').value;
        const address = document.getElementById('address').value;

        fetch('/contacts', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ contactId, firstName, lastName, phone, address })
        })
        .then(response => response.text())
        .then(data => alert(data))
        .catch(error => console.error('Error:', error));
    });
});
