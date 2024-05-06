describe('List', () => {
    const sessions = [
        // { id: 1, name: 'Session To Be Deleted', description: 'Updated Yoga session description', date: new Date(), teacher_id: 2, users: [1, 2], createdAt: new Date(), updatedAt: new Date() },
        // { id: 2, name: 'Yoga session', description: 'description', date: new Date(), teacher_id: 2, users: [1, 2], createdAt: new Date(), updatedAt: new Date() }
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


    before(() => {
        cy.visit('/login');
        cy.intercept('POST', '/api/auth/login', { statusCode: 201, body: admin }).as('login');
        cy.intercept('GET', '/api/session', sessions).as('fetchSessions');
        login();
        cy.url().should('include', '/sessions'); // Vérification de la redirection après connexion 
    });

    it('Check the number of mat-card.item elements', () => {
        cy.get('mat-card.item').should('have.length', sessions.length);
    });

});
