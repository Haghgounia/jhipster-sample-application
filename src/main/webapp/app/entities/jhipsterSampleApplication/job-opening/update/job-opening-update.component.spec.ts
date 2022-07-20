import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { JobOpeningService } from '../service/job-opening.service';
import { IJobOpening, JobOpening } from '../job-opening.model';

import { JobOpeningUpdateComponent } from './job-opening-update.component';

describe('JobOpening Management Update Component', () => {
  let comp: JobOpeningUpdateComponent;
  let fixture: ComponentFixture<JobOpeningUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let jobOpeningService: JobOpeningService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [JobOpeningUpdateComponent],
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
      .overrideTemplate(JobOpeningUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(JobOpeningUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    jobOpeningService = TestBed.inject(JobOpeningService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const jobOpening: IJobOpening = { id: 456 };

      activatedRoute.data = of({ jobOpening });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(jobOpening));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<JobOpening>>();
      const jobOpening = { id: 123 };
      jest.spyOn(jobOpeningService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ jobOpening });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: jobOpening }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(jobOpeningService.update).toHaveBeenCalledWith(jobOpening);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<JobOpening>>();
      const jobOpening = new JobOpening();
      jest.spyOn(jobOpeningService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ jobOpening });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: jobOpening }));
      saveSubject.complete();

      // THEN
      expect(jobOpeningService.create).toHaveBeenCalledWith(jobOpening);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<JobOpening>>();
      const jobOpening = { id: 123 };
      jest.spyOn(jobOpeningService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ jobOpening });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(jobOpeningService.update).toHaveBeenCalledWith(jobOpening);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
