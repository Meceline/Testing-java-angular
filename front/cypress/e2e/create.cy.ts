describe('Create', () => {
    const sessions = [
        { id: 1, name: 'Yoga session', description: 'description', date: new Date(), teacher_id: 1, users: [1, 2], createdAt: new Date(), updatedAt: new Date() },
        { id: 2, name: 'Yoga session', description: 'description', date: new Date(), teacher_id: 1, users: [1, 2], createdAt: new Date(), updatedAt: new Date() }
    ];
    const teachers = [{ id: 1, firstName: 'John', lastName: 'Doe', email: 'John@test.com' }];
    const admin = { token: 'token', type: 'Bearer', id: 1, username: 'admin', firstName: 'Jane', lastName: 'Doe', admin: true };
    const loginCredentials = { email: 'email@test.com', password: 'password' };

    function login() {
        cy.get('input[formControlName=email]').type(loginCredentials.email);
        cy.get('input[formControlName=password]').type(`${loginCredentials.password}{enter}{enter}`);
    }

    function submitForm(newSession) {
        cy.get('input[formControlName=name]').type(newSession.name);
        cy.get('input[formControlName=date]').type(newSession.date.toISOString().split('T')[0]);
        cy.get('textarea[formControlName=description]').type(newSession.description);
        cy.get('mat-select[formControlName=teacher_id]').click();
        cy.get('mat-option').contains(`${teachers[0].firstName} ${teachers[0].lastName}`).click();
        cy.get('button[type="submit"]').click();
    }

    before(() => {
        cy.visit('/login');
        cy.intercept('POST', '/api/auth/login', { statusCode: 201, body: admin }).as('login');
        cy.intercept('GET', '/api/session', sessions).as('sessions');
        cy.intercept('GET', '/api/teacher', { body: teachers }).as('teachers');
        login();
        cy.url().should('include', '/sessions'); // Vérification de la redirection après connexion.
        cy.get('mat-card.item').should('have.length', sessions.length); // Vérification du nombre de sessions affichées.
        cy.get('[routerLink=create]').click(); // Navigation vers la page de création de session.
        cy.url().should('include', '/create'); // Vérification de la redirection vers la page de création.
    });

    // Test pour vérifier la gestion des champs requis manquants.
    it('should display an error if a required field is missing', () => {
        cy.get('input[formControlName=name]').type(`{enter}`);
        cy.get('input[formControlName="name"]').should('have.class', 'ng-invalid'); 
        cy.get('button[type="submit"]').should('be.disabled'); 
    });

    // Test pour vérifier la création d'une session lorsque tous les champs requis sont remplis.
    it('should create a new session when required fields are entered', () => {
        const newSession = { id: 3, name: 'New session', description: 'description', date: new Date(), teacher_id: 1, users: [], createdAt: new Date(), updatedAt: new Date() };
        cy.intercept('POST', '/api/session', { statusCode: 201, body: newSession }).as('session');
        const sessionList = [...sessions, newSession];
        cy.intercept('GET', '/api/session', { body: sessionList }).as('updatedSessions');
        submitForm(newSession);
        cy.get('snack-bar-container').contains('Session created !').should('exist'); // Vérification de l'affichage de la confirmation.
        cy.url().should('include', '/sessions'); // Vérification de la redirection vers la liste des sessions.
        cy.get('mat-card.item').should('have.length', sessionList.length); // Vérification du nombre de sessions affichées.
        cy.get('mat-card-title').contains(newSession.name, { matchCase: false }).should('exist'); // Vérification de la présence de la nouvelle session dans la liste.
    });
});
