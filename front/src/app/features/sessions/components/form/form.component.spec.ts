import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { ActivatedRoute, Router } from '@angular/router';
import { Session } from '../../interfaces/session.interface';
import { HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import { spyOn } from 'jest-mock';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let route: ActivatedRoute;
  let router : Router;
  let httpMock: HttpTestingController;
  let matSnackBar: MatSnackBar;
  
  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  } 
  const mockSession: Partial<Session> = {
    name: 'testSession',
    description: 'test session description',
    date: new Date(),
    teacher_id: 1
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
        HttpClientTestingModule,
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService,
        {
          provide: MatSnackBar,
          useValue: {
            open: jest.fn()
          }
        }
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    route = TestBed.inject(ActivatedRoute);
    router = TestBed.inject(Router);
    httpMock = TestBed.inject(HttpTestingController);
    matSnackBar = TestBed.inject(MatSnackBar);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create form with empty fields when the route is create', () => {
    component.ngOnInit();
    expect(component.sessionForm).toBeTruthy();
    expect(component.sessionForm?.dirty).toBeFalsy();
    expect(component.sessionForm?.contains('name')).toBeTruthy();
    expect(component.sessionForm?.contains('date')).toBeTruthy();
    expect(component.sessionForm?.contains('teacher_id')).toBeTruthy();
    expect(component.sessionForm?.contains('description')).toBeTruthy();
  });

  it('should initialized form with session details when rout is update', () => { 
    const id='1';
    jest.spyOn(route.snapshot.paramMap, 'get').mockReturnValue(id);
    jest.spyOn(router, 'url', 'get').mockReturnValue('update');
    component.ngOnInit();
    expect(component.sessionForm).toBeDefined();
    expect(component.onUpdate).toBeTruthy();
    
    const req = httpMock.expectOne(`api/session/${id}`);
    expect(req.request.method).toBe('GET');

    const formateDate = mockSession.date?.toISOString().split('T')[0];
    const mockSessionWithDate = {...mockSession, date: formateDate};

    req.flush(mockSessionWithDate);
    expect(component.sessionForm?.value).toEqual(mockSessionWithDate);
  });
  
  it('should navigate to "sessions" route after showing message', () => {
    const message = 'Test message';
    const matSnackBarSpy = spyOn(TestBed.inject(MatSnackBar), 'open');
    const routerSpy = spyOn(TestBed.inject(Router), 'navigate');
  
    component['exitPage'](message);
  
    expect(matSnackBarSpy).toHaveBeenCalledWith(message, 'Close', { duration: 3000 });
    expect(routerSpy).toHaveBeenCalledWith(['sessions']);
  });
});