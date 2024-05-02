import { HttpClientModule } from '@angular/common/http';
import { TestBed, ComponentFixture } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { BehaviorSubject } from 'rxjs';

import { AppComponent } from './app.component';
import { SessionService } from './services/session.service';
import { Router } from '@angular/router';

describe('AppComponent', () => {
  let fixture: ComponentFixture<AppComponent>;
  let component: AppComponent;
  let sessionService: SessionService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();

    // Créer un composant et injecter les services nécessaires
    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    sessionService = TestBed.inject(SessionService);
    fixture.detectChanges();
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });


  it('should log out and navigate to home page', () => {
    // Créer un spy pour la méthode logOut du SessionService
    jest.spyOn(sessionService, 'logOut');

    // Créer un spy pour la méthode navigate du Router
    const routerSpy = jest.spyOn(TestBed.inject(Router), 'navigate');

    // Appeler la méthode logout du composant
    component.logout();

    // Vérifier si la méthode logOut du service a été appelée
    expect(sessionService.logOut).toHaveBeenCalled();

    // Vérifier si la méthode navigate du Router a été appelée avec la route appropriée
    expect(routerSpy).toHaveBeenCalledWith(['']);
  });
});
