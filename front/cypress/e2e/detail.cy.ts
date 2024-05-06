describe('Detail', () => {
    const sessions = [
        { id: 1, name: 'Session To Be Deleted', description: 'Updated Yoga session description', date: new Date(), teacher_id: 2, users: [1, 2], createdAt: new Date(), updatedAt: new Date() },
        { id: 2, name: 'Yoga session', description: 'description', date: new Date(), teacher_id: 2, users: [1, 2], createdAt: new Date(), updatedAt: new Date() }
    ];

    const teachers = [{ id: 1, firstName: 'John', lastName: 'Doe', email: 'John@test.com' }, { id: 2, firstName: 'John2', lastName: 'Doe2', email: 'John2@test.com' }];

    const admin = { token: 'token', type: 'Bearer', id: 1, username: 'admin', firstName: 'Jane', lastName: 'Doe', admin: true };
    const loginCredentials = { email: 'email@test.com', password: 'password' };

    function login() {
        cy.get('input[formControlName=email]').type(loginCredentials.email);
        cy.get('input[formControlName=password]').type(`${loginCredentials.password}{enter}{enter}`);
    }

    function getTeacherNameById(teacherId) {
        const teacher = teachers.find(t => t.id === teacherId);
        return teacher ? `${teacher.firstName} ${teacher.lastName}` : '';
    }

    const sessionToDelete = sessions[0];

    before(() => {
        cy.visit('/login');
        cy.intercept('POST', '/api/auth/login', { statusCode: 201, body: admin }).as('login');
        cy.intercept('GET', '/api/session', sessions).as('fetchSessions');
        login();
        cy.url().should('include', '/sessions'); // Vérification de la redirection après connexion
        cy.get('mat-card.item').should('have.length', sessions.length); // Vérification du nombre de sessions affichées
        
        // Interception de la récupération et de la mise à jour des sessions
        cy.intercept('GET', `/api/session/${sessions[0].id}`, {
            body: sessions[0],
        }).as('sessionToUpdate');

        cy.intercept('GET', `/api/teacher/${teachers[0].id}`, {
            body: teachers[0]
          }).as('teacher 1');
        // Simule un clic sur le bouton "Edit" pour une session
        cy.get('mat-card-actions').find('button[mat-raised-button]').contains('Detail').click();
        
        cy.intercept('GET', `/api/detail/${sessions[0].id}`, {
            body: sessions[0],
        }).as('updateSession');
        
    });

    it('should navigate to the detail page and load the data', () => {
        // Vérifie que le titre de la session est affiché
        cy.get('h1').should('be.visible');
        // Vérifie que le titre de la session correspond à celui de la session supprimée
        cy.get('h1').should('contain', sessionToDelete.name);
    });

    it('should display a button to delete a session', () => {
        // Vérifie que le bouton de suppression de la session est affiché
        cy.get('mat-card-title').find('button[mat-raised-button]').contains('Delete').should('exist');
    });

    it('should delete a session', () => {
        // Intercepte la requête DELETE pour supprimer la session
        cy.intercept('DELETE', `/api/session/${sessionToDelete.id}`, {
        statusCode: 204,
        }).as('deleteSession');
    
        // Intercepte la requête GET pour récupérer les sessions après suppression
        const updatedSessions = sessions.filter(session => session.id !== sessionToDelete.id);
        cy.intercept('GET', '/api/session', {
        body: updatedSessions,
        }).as('getUpdatedSessions');
    
        // Clique sur le bouton de suppression de la session
        cy.get('mat-card-title').find('button[mat-raised-button]').contains('Delete').click();
    
        cy.wait('@deleteSession');
        cy.wait('@getUpdatedSessions');
    
        // Vérifie que le message de confirmation de suppression est affiché
        cy.get('snack-bar-container').contains('Session deleted !').should('exist');
        cy.wait(3000); 

        // Vérifie que l'URL contient '/sessions' après la suppression
        cy.url().should('include', '/sessions');
        // Vérifie que le nombre d'éléments dans la liste est réduit de 1
        cy.get('mat-card.item').should('have.length', 1);
        // Vérifie que le titre de la session supprimée n'est plus affiché
        cy.get('mat-card-title').contains(sessionToDelete.name, {matchCase: false}).should('not.exist');
    });

});
