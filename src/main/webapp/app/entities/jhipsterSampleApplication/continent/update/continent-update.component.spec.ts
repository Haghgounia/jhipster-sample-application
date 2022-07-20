import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ContinentService } from '../service/continent.service';
import { IContinent, Continent } from '../continent.model';

import { ContinentUpdateComponent } from './continent-update.component';

describe('Continent Management Update Component', () => {
  let comp: ContinentUpdateComponent;
  let fixture: ComponentFixture<ContinentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let continentService: ContinentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ContinentUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ContinentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContinentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    continentService = TestBed.inject(ContinentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const continent: IContinent = { id: 456 };

      activatedRoute.data = of({ continent });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(continent));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Continent>>();
      const continent = { id: 123 };
      jest.spyOn(continentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ continent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: continent }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(continentService.update).toHaveBeenCalledWith(continent);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Continent>>();
      const continent = new Continent();
      jest.spyOn(continentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ continent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: continent }));
      saveSubject.complete();

      // THEN
      expect(continentService.create).toHaveBeenCalledWith(continent);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Continent>>();
      const continent = { id: 123 };
      jest.spyOn(continentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ continent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(continentService.update).toHaveBeenCalledWith(continent);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
