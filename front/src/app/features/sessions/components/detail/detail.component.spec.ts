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
    name: 'Test Session !!',
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
      sessionApiService = TestBed.inject(SessionApiService);
      service = TestBed.inject(SessionService);
      fixture = TestBed.createComponent(DetailComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
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

  it('should call participate method of sessionApiService and fetchSession', () => {
    component.session = mockSession; // Étant donné une session avec des utilisateurs existants
    const participateSpy = jest.spyOn(sessionApiService, 'participate').mockReturnValue(of(undefined)); // Espionner la méthode participate du service
    const detailSpy  = jest.spyOn(sessionApiService, 'detail').mockReturnValue(of({...mockSession, users: [1]})); // Espionner la méthode detail du service et la remplacer par une observable retournant une session modifiée

    expect(component.userId).toEqual(mockSessionService.sessionInformation.id.toString()); // Vérifier que l'identifiant de l'utilisateur est correctement défini

    component.participate();  // Quand l'utilisateur participe à la session
    expect(participateSpy).toHaveBeenCalledWith(component.sessionId, component.userId);// Vérifier que la méthode participate a été appelée avec les bons paramètres
    expect(detailSpy).toHaveBeenCalledWith(component.sessionId); // Vérifier que la méthode detail a été appelée avec l'identifiant de session
    expect(component.session.users).toContain(1);  // Vérifier que l'utilisateur a été ajouté à la liste des utilisateurs de la session
    expect(component.isParticipate).toBe(true); // Vérifier que la variable isParticipate est définie à true
  });

  it('should call unparticipate method of sessionApiService and fetchSession when unParticipate is called', () => {
    expect(component.userId).toEqual(mockSessionService.sessionInformation.id.toString());

    component.session = mockSession;
    const unparticipateSpy = jest.spyOn(sessionApiService, 'unParticipate').mockReturnValue(of(undefined));
    const detailSpy = jest.spyOn(sessionApiService, 'detail').mockReturnValue(of({...mockSession, users: []}));

    component.unParticipate();

    expect(unparticipateSpy).toHaveBeenCalledWith(component.sessionId, component.userId);
    expect(detailSpy).toHaveBeenCalledWith(component.sessionId);
    expect(component.session.users).not.toContain(1);
    expect(component.isParticipate).toBe(false);
  });
  
  it('should fetch and display session details on init', () => {
    const id = "1";
    component.sessionId = id;
    const detailSpy = jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(mockSession));

    component.ngOnInit();

    fixture.detectChanges();
    expect(component.userId).toEqual(mockSessionService.sessionInformation.id.toString());

    expect(detailSpy).toHaveBeenCalledWith(id);
    expect(component.session).toEqual(mockSession);
  });



});


