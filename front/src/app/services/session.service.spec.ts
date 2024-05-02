import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';
import { BehaviorSubject } from 'rxjs';

describe('SessionService', () => {
  let service: SessionService;
  let isLoggedSubject: BehaviorSubject<boolean>;
  
  const mockSessionInformation: SessionInformation = {
    id: 1,
    admin: true,
    token: "mockToken",
    type: "mockType",
    username: "mockUsername",
    firstName: "mockFirstName",
    lastName: "mockLastName",
  }

  beforeEach(() => {
    
     isLoggedSubject = new BehaviorSubject<boolean>(false);
     TestBed.configureTestingModule({
       providers: [
         SessionService,
         { provide: BehaviorSubject, useValue: isLoggedSubject }
       ]
     });
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should set user session and change logged in status to true', () => {
    service.logIn(mockSessionInformation);
    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(mockSessionInformation);
  });

  it('should clear user session and change logged in status to false', () => {
    service.logIn(mockSessionInformation);
    service.logOut();
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });


  it('should set user session and change logged in status to true', () => {
    service.logIn(mockSessionInformation);
    // Vérifie si le statut de connexion est mis à true
    expect(service.isLogged).toBe(true);
    // Vérifie si les informations de session sont définies
    expect(service.sessionInformation).toEqual(mockSessionInformation);
    // Vérifie si le sujet a émis la valeur true
    isLoggedSubject.subscribe(value => {
      expect(value).toBe(true);
    });
  });

  it('should clear user session and change logged in status to false', () => {
    // Simuler d'abord une connexion
    service.logIn(mockSessionInformation);
    // Exécuter la déconnexion
    service.logOut();
    // Vérifier si le statut de connexion est passé à false
    expect(service.isLogged).toBe(false);
    // Vérifier si les informations de session sont effacées
    expect(service.sessionInformation).toBeUndefined();
    // Vérifier si le sujet a émis la valeur false
    isLoggedSubject.subscribe(value => {
      expect(value).toBe(false);
    });
  });

});
