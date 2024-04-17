import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { LoginComponent } from './login.component';
import { Router } from '@angular/router';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { LoginRequest } from '../../interfaces/loginRequest.interface';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let httpMock: HttpTestingController;
  let router: Router;

  const sessionInformation: SessionInformation = {
    token: 'token',
    type: 'type',
    id: 1,
    username: 'user@test.com',
    firstName: 'userLastName',
    lastName: 'userFirstName',
    admin: false
  };  
  
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        HttpClientTestingModule,
      ]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    router = TestBed.inject(Router);
    httpMock = TestBed.inject(HttpTestingController);
    jest.spyOn(router, 'navigate');
  });

  //Unitaire
  it('should create', () => {
    // Vérifie si le composant a été créé avec succès
    expect(component).toBeTruthy();
    expect(component.hide).toBe(true);
    expect(component.onError).toBe(false);
    expect(component.form.dirty).toBe(false);
    const button = fixture.debugElement.nativeElement.querySelector('button[type="submit"]');
    expect(button.disabled).toBe(true);
  });

  //Intégration
  it('should call AuthService.login() with correct parameters and navigate to /sessions when login attempt is successful', ()=>{
    // Crée une demande de connexion simulée
    const loginRequest: LoginRequest = {
      email: 'user@test.com',
      password: 'password'
    };
    component.form.setValue(loginRequest);
    component.submit();

    // Attend une requête HTTP vers l'API de connexion
    const request = httpMock.expectOne('api/auth/login');
    // Vérifie que la méthode de la requête est POST
    expect(request.request.method).toBe('POST');
    // Vérifie que le corps de la requête est correct
    expect(request.request.body).toEqual(loginRequest);
    // Simule une réponse réussie de l'API avec les informations de session
    request.flush(sessionInformation);
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  //Unitaire
it('should set onError to true when login attempt is unsuccessful', () => {
    // Crée une demande de connexion simulée
    const loginRequest: LoginRequest = {
      email: 'user@test.com',
      password: 'password'
    }; 
    component.form.setValue(loginRequest);
    component.submit();
    // Attend une requête HTTP vers l'API de connexion
    const request = httpMock.expectOne('api/auth/login');
    // Simule une réponse d'erreur de l'API
    request.flush('Error', { status: 401, statusText: 'Unauthorized' });
    fixture.detectChanges();
    // Vérifie que la propriété "onError" du composant est définie sur true en cas d'échec de la connexion
    expect(component.onError).toBe(true);
    // Vérifie que le message d'erreur est affiché dans le DOM du composant
    const message = fixture.debugElement.nativeElement.querySelector('.error');
    expect(message.textContent).toContain('An error occurred'); 
  });


});
