import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmployeeContactService } from '../service/employee-contact.service';
import { IEmployeeContact, EmployeeContact } from '../employee-contact.model';

import { EmployeeContactUpdateComponent } from './employee-contact-update.component';

describe('EmployeeContact Management Update Component', () => {
  let comp: EmployeeContactUpdateComponent;
  let fixture: ComponentFixture<EmployeeContactUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employeeContactService: EmployeeContactService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmployeeContactUpdateComponent],
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
      .overrideTemplate(EmployeeContactUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployeeContactUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employeeContactService = TestBed.inject(EmployeeContactService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const employeeContact: IEmployeeContact = { id: 456 };

      activatedRoute.data = of({ employeeContact });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(employeeContact));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<EmployeeContact>>();
      const employeeContact = { id: 123 };
      jest.spyOn(employeeContactService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeContact });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeContact }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(employeeContactService.update).toHaveBeenCalledWith(employeeContact);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<EmployeeContact>>();
      const employeeContact = new EmployeeContact();
      jest.spyOn(employeeContactService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeContact });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeContact }));
      saveSubject.complete();

      // THEN
      expect(employeeContactService.create).toHaveBeenCalledWith(employeeContact);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<EmployeeContact>>();
      const employeeContact = { id: 123 };
      jest.spyOn(employeeContactService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeContact });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employeeContactService.update).toHaveBeenCalledWith(employeeContact);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
