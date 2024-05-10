describe('LogOut', () => {
    const loginCredentials = { email: 'email@test.com', password: 'password' };

    function login() {
        cy.get('input[formControlName=email]').type(loginCredentials.email);
        cy.get('input[formControlName=password]').type(`${loginCredentials.password}{enter}{enter}`);
    }


    before(() => {
        cy.visit('/login')
        cy.intercept('POST', '/api/auth/login', {
          statusCode: 201,
          body: loginCredentials,
        }).as('login')
    
        cy.intercept('GET', '/api/session', []).as('sessions');
        login();
    
      })
    
      it('should logout successfully', () => {
        cy.get('.link').contains('Logout').click()
        cy.url().should('eq', Cypress.config().baseUrl)
        cy.get('.link').contains('Logout').should('not.exist')
      })


});
