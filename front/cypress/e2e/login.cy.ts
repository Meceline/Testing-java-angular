describe('Login spec', () => { 
  const user = { 
    id: 1,
    email: 'email@test.com',
    firstName: 'John',
    lastName: 'Doe',
    password: 'pass!1234',
    admin: false,
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  beforeEach(() => {
    cy.visit('/login');  // Visite la page de connexion avant chaque test
  });

  // Test d'intégration: vérifie l'interaction complète du flux de connexion avec l'API et l'UI.
  it('should login successfully', () => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 201,
      body: user,  // Intercepte l'appel API de connexion et simule une réponse
    });

    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: [],  // Intercepte l'appel API de session et simule une réponse
    }).as('session');

    cy.get('input[formControlName=email]').type(user.email)  // Remplit le champ email
    cy.get('input[formControlName=password]').type(`${user.password}{enter}{enter}`)  // Remplit le champ mot de passe et simule l'envoi du formulaire

    cy.url().should('include', '/sessions') ;  // Vérifie que l'URL est correcte après la connexion

    cy.get('.error').should('not.exist')  // Aucun message d'erreur n'est affiché
  })

  // Test d'intégration: teste la gestion des erreurs de l'API et de l'interface utilisateur
  it('should not login with incorrect email', () => {
    cy.get('input[formControlName=email]').type("test@test.com")  // Saisit un email incorrect
    cy.get('input[formControlName=password]').type(`${user.password}{enter}{enter}`)  // Saisit le mot de passe correct et envoi le formulaire

    cy.get('.error').should('contain', 'An error occurred')  // Vérifie que le message d'erreur est affiché
  })

  // Test d'intégration: teste lorsque le mot de passe est incorrect
  it('should not login with incorrect password', () => {
    cy.get('input[formControlName=email]').type(user.email)  // Email correct
    cy.get('input[formControlName=password]').type(`wrong-password{enter}{enter}`)  // Mot de passe incorrect et envoi le formulaire

    cy.get('.error').should('contain', 'An error occurred')  // Le message d'erreur est affiché
  })

  // Test d'intégration: vérifie que l'application gère correctement les tentatives de connexion sans mot de passe.
  it('should not login with missing credentials', () => {
    cy.get('input[formControlName=email]').type(user.email)  // Email correct
    cy.get('input[formControlName=password]').type(`{enter}{enter}`)  // Pas de mot de passe et envoi du formulaire

    cy.get('.error').should('contain', 'An error occurred')  // Vérifie que le message d'erreur approprié est affiché
  })
});
