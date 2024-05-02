import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Session } from '../interfaces/session.interface';


describe('SessionsService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule,
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });
  afterEach(() => {
    httpMock.verify(); // Vérifie qu'il n'y a pas de requêtes HTTP non résolues
  });


  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve all sessions', () => {
    const mockSessions = [{ id: '1', title: 'Session 1' }, { id: '2', title: 'Session 2' }];
    // On s'attend à ce que le service retourne les sessions
    service.all().subscribe(sessions => {
      expect(sessions.length).toBe(2);
      expect(sessions).toEqual(mockSessions);
    });
    // On s'attend à ce qu'une requête GET soit envoyée sur l'URL de l'API
    const req = httpMock.expectOne('api/session'); // On vérifie qu'une seule requête a été faite sur l'URL de l'API
    expect(req.request.method).toBe('GET'); // On vérifie que la méthode utilisée est bien GET
    req.flush(mockSessions); // On envoie les données de mockSessions en réponse à la requête
  });

  it('should retrieve a session by id', () => {
    const mockSession = { id: '1', title: 'Session 1' };
    service.detail('1').subscribe(session => {
      expect(session).toEqual(mockSession);
    });
    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockSession);
  });

  it('should delete a session', () => {
    const mockSessions = [{ id: '1', title: 'Session 1' }, { id: '2', title: 'Session 2' }]; 
    service.delete('1').subscribe(() => {
      httpMock.expectOne('api/session').flush(mockSessions.filter(session => session.id !== '1')); 
    });   
    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('DELETE');
    req.flush({}); 
  });

  it('should create a session', () => {
    const mockSession: Session = {
      id: 1,
      name: 'Session 1',
      description: 'Dummy description',
      date: new Date(),
      teacher_id: 1,
      users: []
    };
    service.create(mockSession).subscribe(session => {
      expect(session).toEqual(mockSession);
    });
    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    req.flush(mockSession);
  });

  it('should update a session', () => {
    const mockSession: Session = {
      id: 1,
      name: 'Session 1',
      description: 'Dummy description',
      date: new Date(),
      teacher_id: 1,
      users: []
    };
    service.update('1', mockSession).subscribe(session => {
      expect(session).toEqual(mockSession);
    });
    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('PUT');
    req.flush(mockSession);
  });

  it('should participate to a session', () => {
    service.participate('1', '1').subscribe();
    const req = httpMock.expectOne('api/session/1/participate/1');
    expect(req.request.method).toBe('POST');
    req.flush({});
  });

  it('should unparticipate to a session', () => {
    service.unParticipate('1', '1').subscribe();
    const req = httpMock.expectOne('api/session/1/participate/1');
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });
 
});
