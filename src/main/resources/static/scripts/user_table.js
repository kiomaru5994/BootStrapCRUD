document.addEventListener("DOMContentLoaded", function () {
    loadUsers();

    document.getElementById('confirmDeleteButton').addEventListener('click', function () {
        const userId = this.dataset.userId;

        fetch(`/api/users/delete/${userId}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    const row = document.getElementById(`user-row-${userId}`);
                    if (row) {
                        row.remove();
                    }

                    const deleteModalElement = document.getElementById('deleteModal');
                    if (deleteModalElement) {
                        const deleteModal = bootstrap.Modal.getInstance(deleteModalElement);
                        deleteModal.hide();
                    }
                } else {
                    console.error('Ошибка удаления пользователя:', response.statusText);
                }
            })
            .catch(error => console.error('Ошибка выполнения запроса на удаление:', error));
    });

    document.getElementById('addUserForm').addEventListener('submit', function (event) {
        event.preventDefault();

        const user = {
            username: document.getElementById('username').value,
            password: document.getElementById('password').value,
            email: document.getElementById('email').value,
            age: document.getElementById('age').value,
            roles: Array.from(document.getElementById('roles').selectedOptions).map(option => option.text)
        };

        fetch('/api/users/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(user)
        })
            .then(response => response.json())
            .then(newUser => {
                addUserToTable(newUser);
                document.getElementById('addUserForm').reset();
            })
            .catch(error => console.error('Ошибка добавления пользователя:', error));
    });

    document.getElementById('confirmEditButton').addEventListener('click', function () {
        const userId = document.getElementById('editId').value;

        const updatedUser = {
            username: document.getElementById('editUsername').value,
            password: document.getElementById('editPassword').value,
            email: document.getElementById('editEmail').value,
            age: document.getElementById('editAge').value,
            roles: Array.from(document.getElementById('editRoles').selectedOptions).map(option => option.text)
        };

        fetch(`/api/users/update/${userId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedUser)
        })
            .then(response => response.json())
            .then(() => {
                console.log('User updated successfully.');

                const editModalElement = document.getElementById('editModal');
                if (editModalElement) {
                    const editModal = bootstrap.Modal.getInstance(editModalElement);
                    editModal.hide();
                }

                loadUsers();
            })
            .catch(error => console.error('Ошибка редактирования пользователя:', error));
    });
});

function loadUsers() {
    fetch('/api/users')
        .then(response => response.json())
        .then(users => {
            console.log('Loaded users:', users);
            const userTable = document.getElementById('userTable');
            userTable.innerHTML = '';

            users.forEach(user => {
                addUserToTable(user);
            });
        })
        .catch(error => console.error('Ошибка загрузки пользователей:', error));
}

function addUserToTable(user) {
    const userTable = document.getElementById('userTable');
    const row = createUserRow(user);
    userTable.appendChild(row);
}

function createUserRow(user) {
    const row = document.createElement("tr");
    row.id = `user-row-${user.id}`;
    row.innerHTML = `
        <td>${user.id}</td>
        <td>${user.username}</td>
        <td>${user.email}</td>
        <td>${user.age}</td>
        <td>${formatRoles(user.roles)}</td>
        <td>
            <button type="button" class="btn btn-info" onclick="showEditModal(${user.id})">Edit</button>
        </td>
        <td>
            <button type="button" class="btn btn-danger" onclick="showDeleteModal(${user.id})">Delete</button>
        </td>
    `;
    return row;
}

function formatRoles(roles) {
    if (!Array.isArray(roles)) {
        console.error('Roles is not an array:', roles);
        return '';
    }
    return roles.map(role => (typeof role === 'string' ? role : role.name)).join(', ');
}

function showDeleteModal(userId) {
    fetch(`/api/users/${userId}`)
        .then(response => response.json())
        .then(user => {
            document.getElementById('deleteId').value = user.id;
            document.getElementById('deleteUsername').value = user.username;
            document.getElementById('deleteEmail').value = user.email;
            document.getElementById('deleteAge').value = user.age;

            const deleteModalElement = document.getElementById('deleteModal');
            if (deleteModalElement) {
                const deleteModal = new bootstrap.Modal(deleteModalElement, {});
                deleteModal.show();
            }

            document.getElementById('confirmDeleteButton').dataset.userId = user.id;
        })
        .catch(error => console.error('Ошибка загрузки данных пользователя:', error));
}

function showEditModal(userId) {
    fetch(`/api/users/${userId}`)
        .then(response => response.json())
        .then(user => {
            document.getElementById('editId').value = user.id;
            document.getElementById('editUsername').value = user.username;
            document.getElementById('editPassword').value = user.password;
            document.getElementById('editEmail').value = user.email;
            document.getElementById('editAge').value = user.age;

            const rolesSelect = document.getElementById('editRoles');
            Array.from(rolesSelect.options).forEach(option => {
                option.selected = user.roles.map(role => role.name).includes(option.text);
            });

            const editModalElement = document.getElementById('editModal');
            if (editModalElement) {
                const editModal = new bootstrap.Modal(editModalElement, {});
                editModal.show();
            }
        })
        .catch(error => console.error('Ошибка загрузки данных пользователя:', error));
}
