import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';

import { MeComponent } from './me.component';
import { User } from 'src/app/interfaces/user.interface';
import { of } from 'rxjs';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let routerMock = { navigate: jest.fn() };
  let userServiceMock: any;
  let matSnackBarMock: any;
  let sessionServiceMock: any;
  

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }
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
    userServiceMock = {
      delete: jest.fn(),
      getById: jest.fn()
    };
    matSnackBarMock = {
      open: jest.fn()
    };
    sessionServiceMock = {
      sessionInformation: { id: 123 },
      logOut: jest.fn()
    };

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('should display admin information', () => {
    component.user = mockAdmin;
    const createdDate = new Date();
    const lastUpdateDate = new Date();
    const expectedCreateDate = `Create at:  ${createdDate.toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' })}`;
    const expectedLastUpdateDate = `Last update:  ${lastUpdateDate.toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' })}`;
    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    expect(compiled.querySelector('p:nth-of-type(1)').textContent).toContain('Name: adminFirstName ADMINLASTNAME');
    expect(compiled.querySelector('p:nth-of-type(2)').textContent).toContain('Email: admin@test.com');
    expect(compiled.querySelector('.my2').textContent).toContain('You are admin');
    expect(compiled.querySelector('div.my2 button')).toBeFalsy();
    expect(compiled.querySelector('.create-date').textContent).toContain(expectedCreateDate);
    expect(compiled.querySelector('.last-update-date').textContent).toContain(expectedLastUpdateDate); 
  })
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
  it('should navigate back when back button is clicked', () => {
    const backSpy = jest.spyOn(window.history, 'back');
    component.back()
    expect(backSpy).toHaveBeenCalled();
  });
  
  it('should delete account and navigate to home', () => {
    component.delete();

    console.log("test", mockUser.id.toString())
    expect(userServiceMock.delete).toHaveBeenCalledWith(mockSessionService.sessionInformation.id.toString());

    // Simuler l'observable en émettant une valeur
    userServiceMock.delete.mockReturnValue(of(true));

    // Forcer l'exécution de l'observable en appelant la méthode subscribe
    // et passer une fonction callback
    component.delete();

    expect(matSnackBarMock.open).toHaveBeenCalledWith('Your account has been deleted !', 'Close', { duration: 3000 });
    expect(sessionServiceMock.logOut).toHaveBeenCalled();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/']);
  });

});