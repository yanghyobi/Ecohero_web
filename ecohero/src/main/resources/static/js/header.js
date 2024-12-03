document.addEventListener('DOMContentLoaded', () => {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const logoutForms = document.querySelectorAll('.logout-form');
    logoutForms.forEach(form => {
        const csrfField = form.querySelector('input[name="_csrf"]');
        if (csrfField) {
            csrfField.value = csrfToken;
        }
    });
});
