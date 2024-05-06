describe('Register spec', () => {
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
      cy.visit('/register');  // Visite la page d'enregistrement avant chaque test
    });
  
    // Test d'intégration: Vérifie la validation côté client des champs requis
    it('should indicate an error if required fields are missing', () => {
      cy.get('input[formControlName=firstName]').type(user.firstName)  
      cy.get('input[formControlName=lastName]').type(user.lastName)  
      cy.get('input[formControlName="email"]').type(`{enter}`);  // Email vide et envoi du formulaire
      cy.get('input[formControlName=password]').type(user.password)  // Remplit le mot de passe
      cy.get('input[formControlName="email"]').should('have.class', 'ng-invalid');  // Vérifie que le champ email est invalide
  
      cy.get('button[type=submit]').should('be.disabled')  // Vérifie que le bouton est désactivé
    })
  
    // Test d'intégration: Simule un flux complet de création de compte de l'utilisateur et visite de la page détails du compte
    it('should register, login, and check user account details', () => {
      cy.intercept('POST', '/api/auth/register', {
        statusCode: 201,
        body: user,
      }).as('register')  // Intercepte et simule la réponse de l'enregistrement
  
      cy.get('input[formControlName=firstName]').type(user.firstName) 
      cy.get('input[formControlName=lastName]').type(user.lastName) 
      cy.get('input[formControlName=email]').type(user.email) 
      cy.get('input[formControlName=password]').type(user.password) 
  
      cy.get('button[type=submit]').click()  // Envpo le formulaire
  
      cy.url().should('include', '/login')  // Vérifie que l'utilisateur est redirigé vers la page de connexion
  
      cy.intercept('POST', '/api/auth/login', {
        statusCode: 201,
        body: user,
      }).as('login')  // Intercepte et simule la réponse de connexion
  
      cy.intercept('GET', '/api/session', {
        statusCode: 200,
        body: user,
      }).as('getSession')  // Intercepte et simule la récupération de session
  
      cy.get('input[formControlName=email]').type(user.email)  // Saisit l'email pour la connexion
      cy.get('input[formControlName=password]').type(`${user.password}{enter}{enter}`)  // Saisit le mot de passe pour la connexion et soumet
  
      cy.intercept('GET', `/api/user/${user.id}`, {
        statusCode: 200,
        body: user,
      }).as('getUser')  // Intercepte et simule la réponse pour récupérer les détails de l'utilisateur
  
      cy.get('span[routerLink=me]').click();  // Clique sur le lien pour aller à la page de profil de l'utilisateur
  
      cy.url().should('include', '/me');  // Vérifie que l'URL contient '/me'
  
      cy.get('.m3 mat-card-content p').contains(`Name: ${user.firstName} ${user.lastName.toUpperCase()}`).should('exist');  // Vérifie que le nom est affiché correctement
      cy.get('.m3 mat-card-content p').contains(`Email: ${user.email}`).should('exist');  // Vérifie que l'email est affiché correctement
      cy.get('.m3 mat-card-content div.my2').should('exist');  // Vérifie que d'autres éléments du profil sont présents
    });
  })

