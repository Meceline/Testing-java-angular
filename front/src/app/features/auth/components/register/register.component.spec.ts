import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';



describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
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
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        HttpClientTestingModule,
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    router = TestBed.inject(Router);
    httpMock = TestBed.inject(HttpTestingController);
    jest.spyOn(router, 'navigate');
  });

  //Unitaire
  it('should create', () => { // Vérifie si le composant est créé correctement
    expect(component).toBeTruthy();
    expect(component.form.dirty).toBe(false);
    expect(component.onError).toBe(false);
    const button = fixture.debugElement.nativeElement.querySelector('button');
    expect(button.disabled).toBe(true);
  });

  //Intégration
  it('should call AuthService.register() with correct parameters and navigate to /login when registration attemps is succesful', () => {
      component.form.setValue({ // set le formulaire avec des données valides
        email: 'user@test.com',
        firstName: 'John',
        lastName: 'Doe',
        password: 'password'
      });
      expect(component.form.valid).toBe(true); //Vérifie que le formulaire est valide
      fixture.detectChanges();

      const button = fixture.debugElement.nativeElement.querySelector('button[type=submit]');
      expect(button.disabled).toBe(false); //Vérifie que le bouton est actif après avoir rempli le formulaire correctement
 
      component.submit();
      const request = httpMock.expectOne('api/auth/register');

      expect(request.request.method).toBe('POST');// Vérifie la méthode HTTP
      expect(request.request.body).toEqual({
        email: 'user@test.com',
        firstName: 'John',
        lastName: 'Doe',
        password: 'password'
      });
      request.flush({sessionInformation});// Simule une réponse réussie du service d'inscription.
      expect(router.navigate).toHaveBeenCalledWith(['/login']); // Vérifie que la méthode `navigate()` du router est appelée avec l'URL '/login'
    });


    //Unitaire
    it('should set onError to true on failed registration attempt', () => {
      component.form.setValue({ //Confirgure le formulaire avec des données valides
        email: 'user@test.com',
        firstName: 'John',
        lastName: 'Doe',
        password: 'password'
      });
      expect(component.form.valid).toBe(true);
      fixture.detectChanges();
      component.submit();
      const request = httpMock.expectOne('api/auth/register');
      request.flush('Error', {status: 400, statusText: 'Bad Request'}); // Simule une réponse d'erreur du service d'inscription
      fixture.detectChanges();
      expect(component.onError).toBe(true);
      const message = fixture.debugElement.nativeElement.querySelector('.error'); // Vérifie que le message d'erreur contient le texte "An error occured"
      expect(message.textContent).toContain('An error occurred');
    } );

});
