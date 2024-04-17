import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { Session } from '../../interfaces/session.interface';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;
  let sessionApiService: SessionApiService;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }
  const mockSession: Session = {
    id: 1,
    name: 'Test Session',
    description: "desc",
    date: new Date(),
    teacher_id: 1,
    users: [],
    createdAt: new Date(),
    updatedAt: new Date()
  };
  

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent], 
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    })
      .compileComponents();
      service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch and display session details on init', () => {
    const id = "1";
    component.sessionId = id;
    
    const detailSpy = jest.spyOn(sessionApiService, 'detail').mockReturnValue(of({} as Session))

    component.ngOnInit();

    fixture.detectChanges();
    expect(component.userId).toEqual(mockSessionService.sessionInformation.id.toString());

    expect(detailSpy).toHaveBeenCalledWith(id);
    expect(component.session).toEqual(mockSession);
  });

  it('should navigate back when back button is cliked', () => {
    const backSpy = jest.spyOn(window.history, 'back');
    component.back();
    expect(backSpy).toHaveBeenCalled();
  });


  it('should allow an admin to delete a session', () => {
    mockSessionService.sessionInformation.admin = true;
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component.isAdmin).toBe(true);
    const deleteSpy = jest.spyOn(sessionApiService, 'delete').mockReturnValue(of(null));
    component.delete();
    expect(deleteSpy).toHaveBeenCalledWith(component.sessionId);
  });
});

/*
 Cannot spyOn on a primitive value; undefined given

      88 |
      89 |     expect(component.isAdmin).toBe(true);
    > 90 |     const deleteSpy = jest.spyOn(sessionApiService, 'delete').mockReturnValue(of(null));
*/

