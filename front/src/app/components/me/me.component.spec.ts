import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';

import { MeComponent } from './me.component';
import { User } from 'src/app/interfaces/user.interface';
import { of } from 'rxjs';
import { UserService } from 'src/app/services/user.service';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let routerMock = { navigate: jest.fn() };
  let userServiceMock: any;
  let matSnackBarMock: any;
  let sessionServiceMock: any;
  let router: Router;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    },
    logOut: jest.fn()
  }
  // Définition des données factices pour un utilisateur admin
  const mockAdmin: User = {
    id: 1,
    email: "admin@test.com",
    lastName: "adminLastName",
    firstName: "adminFirstName",
    admin: true,
    password: "password",
    createdAt: new Date(),
    updatedAt: new Date(),
  };
  // Définition des données factices pour un utilisateur normal
  const mockUser: User = {
    id: 1,
    email: "user@test.com",
    lastName: "userLastName",
    firstName: "userFirstName",
    admin: false,
    password: "password",
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  beforeEach(async () => {
    
  // Initialisation des mocks
  // Définition des comportements pour les méthodes des mocks
    userServiceMock = {
      delete: jest.fn().mockReturnValue(of(true)),
      getById: jest.fn().mockReturnValue(of(mockUser)) 
    };
    matSnackBarMock = {
      open: jest.fn()
    };
    sessionServiceMock = {
      sessionInformation: mockSessionService.sessionInformation,
      logOut: jest.fn()
    };

    await TestBed.configureTestingModule({
      // Configuration du module de test
      declarations: [MeComponent], // Déclaration du composant à tester
      imports: [ // Importation des modules nécessaires pour le composant
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        RouterTestingModule
      ],
      providers: [ // Fourni des mocks en tant que services pour le composant
        { provide: UserService, useValue: userServiceMock },
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: MatSnackBar, useValue: matSnackBarMock },
        { provide: Router, useValue: routerMock }
      ],
    })
      .compileComponents(); // Compilation du composant

    fixture = TestBed.createComponent(MeComponent); // Création du composant
    component = fixture.componentInstance; // Récupération de l'instance du composant
    fixture.detectChanges(); // Détection des changements
    router = TestBed.inject(Router);
  }); 

  //Unitaire
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  //Intégration : vérifie si les informations de l'utilisateur admin sont correctement affichées dans le composant en inspectant le DOM généré.
  it('should display admin information', () => {
    component.user = mockAdmin; // Définition des données d'entrée
    const createdDate = new Date();
    const lastUpdateDate = new Date();
    const expectedCreateDate = `Create at:  ${createdDate.toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' })}`;
    const expectedLastUpdateDate = `Last update:  ${lastUpdateDate.toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' })}`;
    fixture.detectChanges(); // Exécute le détection des changements dans le composant
    const compiled = fixture.nativeElement; // Récupère le DOM généré
    expect(compiled.querySelector('p:nth-of-type(1)').textContent).toContain('Name: adminFirstName ADMINLASTNAME');
    expect(compiled.querySelector('p:nth-of-type(2)').textContent).toContain('Email: admin@test.com');
    expect(compiled.querySelector('.my2').textContent).toContain('You are admin');
    expect(compiled.querySelector('div.my2 button')).toBeFalsy();
    expect(compiled.querySelector('.create-date').textContent).toContain(expectedCreateDate);
    expect(compiled.querySelector('.last-update-date').textContent).toContain(expectedLastUpdateDate); 
  })

  //Intégration : vérifie si les informations de l'utilisateur admin sont correctement affichées dans le composant en inspectant le DOM généré.
  it('should display user information', () => {
    component.user = mockUser;
    const createdDate = new Date();
    const lastUpdateDate = new Date();
    const expectedCreateDate = `Create at:  ${createdDate.toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' })}`;
    const expectedLastUpdateDate = `Last update:  ${lastUpdateDate.toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' })}`;
    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    expect(compiled.querySelector('p:nth-of-type(1)').textContent).toContain('Name: userFirstName USERLASTNAME');
    expect(compiled.querySelector('p:nth-of-type(2)').textContent).toContain('Email: user@test.com');
    expect(compiled.querySelector('div.my2 button')).toBeTruthy();
    expect(compiled.querySelector('div.my2 button').textContent).toContain('Detail');
    expect(compiled.querySelector('.create-date').textContent).toContain(expectedCreateDate);
    expect(compiled.querySelector('.last-update-date').textContent).toContain(expectedLastUpdateDate); 
  })

  //Unitaire : vérifie si le composant navigue correctement en arrière lorsque le bouton de retour est cliqué, en vérifiant si la fonction back a été appelée.
  it('should navigate back when back button is clicked', () => {
    const backSpy = jest.spyOn(window.history, 'back'); //Simule le comportement de la fonction back
    component.back() // Exécute la méthode à tester
    expect(backSpy).toHaveBeenCalled();
  });

  //Intégration : vérifie si le compte utilisateur est supprimé avec succès et si la navigation vers la page d'accueil est effectuée après la suppression, en inspectant les appels de méthodes aux services mockés.
  it('should delete account and navigate to home', fakeAsync(() => {
    component.delete();// Exécute la méthode à tester
    // Vérifie si la méthode delete du service UserService a été appelée avec l'ID de l'utilisateur
    expect(userServiceMock.delete).toHaveBeenCalledWith(mockUser.id.toString());
    // Modifie le comportement de la méthode delete pour qu'elle retourne un observable vrai
    userServiceMock.delete.mockReturnValue(of(true));
    // Réexécute la méthode à tester
    component.delete();
    expect(matSnackBarMock.open).toHaveBeenCalledWith('Your account has been deleted !', 'Close', { duration: 3000 }); // Vérifie si le message approprié est affiché avec la méthode open du MatSnackBar
    expect(sessionServiceMock.logOut).toHaveBeenCalled(); // Vérifie si la méthode logOut du service SessionService a été appelée
    expect(router.navigate).toHaveBeenCalledWith(['/']); // Vérifie si la méthode navigate du router a été appelée avec la route '/'
  }));

  //Intégration
  it('should delete account and navigate to home', fakeAsync(() => {
    // Simuler la réponse de l'appel à la méthode delete du service
    userServiceMock.delete.mockReturnValue(of(true));
    // Mettre à jour le composant avec un utilisateur
    component.user = mockUser;
    // Déclencher la détection des modifications initiales
    fixture.detectChanges();
    // Simuler un clic sur le bouton de suppression du compte
    const deleteButton = fixture.nativeElement.querySelector('.delete-button');
    deleteButton.click();
    // Attendre que les opérations asynchrones se terminent
    tick();
    // Vérifier que la méthode delete du service a été appelée avec le bon paramètre
    expect(userServiceMock.delete).toHaveBeenCalledWith(mockUser.id.toString());
    // Vérifier que la méthode open de matSnackBar a été appelée avec les bons arguments
    expect(matSnackBarMock.open).toHaveBeenCalledWith('Your account has been deleted !', 'Close', { duration: 3000 });
    // Vérifier que la méthode logOut de sessionService a été appelée
    expect(sessionServiceMock.logOut).toHaveBeenCalled();
    // Vérifier que la méthode navigate du router a été appelée avec le bon chemin
    expect(router.navigate).toHaveBeenCalledWith(['/']);
  }));

});