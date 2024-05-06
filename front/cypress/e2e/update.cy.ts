describe('Update', () => {
    // Déclaration des données de session initiales
    const sessions = [
        { id: 1, name: 'Yoga session', description: 'description', date: new Date(), teacher_id: 1, users: [1, 2], createdAt: new Date(), updatedAt: new Date() },
        { id: 2, name: 'Yoga session', description: 'description', date: new Date(), teacher_id: 2, users: [1, 2], createdAt: new Date(), updatedAt: new Date() }
    ];

    // Déclaration des données des enseignants
    const teachers = [{ id: 1, firstName: 'John', lastName: 'Doe', email: 'John@test.com' }, { id: 2, firstName: 'John2', lastName: 'Doe2', email: 'John2@test.com' }];

    // Informations d'administration pour la connexion
    const admin = { token: 'token', type: 'Bearer', id: 1, username: 'admin', firstName: 'Jane', lastName: 'Doe', admin: true };
    const loginCredentials = { email: 'email@test.com', password: 'password' };

    // Fonction pour simuler la connexion utilisateur
    function login() {
        cy.get('input[formControlName=email]').type(loginCredentials.email);
        cy.get('input[formControlName=password]').type(`${loginCredentials.password}{enter}{enter}`);
    }

    // Fonction pour récupérer le nom complet de l'enseignant basé sur son ID
    function getTeacherNameById(teacherId) {
        const teacher = teachers.find(t => t.id === teacherId);
        return teacher ? `${teacher.firstName} ${teacher.lastName}` : '';
    }

    // Mise à jour des informations de session
    const updatedSession = { id: 1, name: 'Updated session', description: 'Updated Yoga session description', date: new Date(), teacher_id: 2, users: [1, 2], createdAt: new Date(), updatedAt: new Date() };

    before(() => {
        cy.visit('/login');
        cy.intercept('POST', '/api/auth/login', { statusCode: 201, body: admin }).as('login');
        cy.intercept('GET', '/api/session', sessions).as('fetchSessions');
        cy.intercept('GET', '/api/teacher', { body: teachers }).as('teachers');
        login();
        cy.url().should('include', '/sessions'); // Vérification de la redirection après connexion
        cy.get('mat-card.item').should('have.length', sessions.length); // Vérification du nombre de sessions affichées
        
        // Interception de la récupération et de la mise à jour des sessions
        cy.intercept('GET', `/api/session/${sessions[0].id}`, {
            body: sessions[0],
        }).as('sessionToUpdate');

        // Simule un clic sur le bouton "Edit" pour une session
        cy.get('mat-card-actions').find('button[mat-raised-button]').contains('Edit').click();
        cy.intercept('PUT', `/api/session/${sessions[0].id}`, {
            body: sessions[0],
        }).as('updateSession');
    });

    it('should navigate to the update page and load the form with session details, update session and send request', () => {
        // Vérification et modification des champs du formulaire
        cy.get('input[formControlName="name"]').should('be.visible').should('have.value', sessions[0].name).clear().type(updatedSession.name);
        cy.get('input[formControlName="date"]').should('be.visible').should('have.value', sessions[0].date.toISOString().split('T')[0]).clear().type(updatedSession.date.toISOString().split('T')[0]);
        cy.get('textarea[formControlName="description"]').should('be.visible').should('have.value', sessions[0].description).clear().type(updatedSession.description);

        // Mise à jour de l'enseignant pour la session
        let teacherName = getTeacherNameById(sessions[0].teacher_id);
        cy.get('mat-select[formControlName="teacher_id"]').click().should('contain', teacherName);
        let newTeacherName = getTeacherNameById(updatedSession.teacher_id);
        cy.contains('mat-option', newTeacherName).click();
        cy.get('mat-select[formControlName="teacher_id"]').should('contain', newTeacherName);

        // Soumission du formulaire et vérification de la redirection
        cy.intercept('PUT', '/api/session', { body: updatedSession }).as('updatedSessions');
        cy.get('button[type="submit"]').should('not.be.disabled').click();
        cy.url().should('include', '/sessions'); // Vérifier la redirection vers la page des sessions
    });
});
