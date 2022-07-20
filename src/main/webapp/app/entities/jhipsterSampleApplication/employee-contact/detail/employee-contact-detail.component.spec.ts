import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EmployeeContactDetailComponent } from './employee-contact-detail.component';

describe('EmployeeContact Management Detail Component', () => {
  let comp: EmployeeContactDetailComponent;
  let fixture: ComponentFixture<EmployeeContactDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EmployeeContactDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ employeeContact: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EmployeeContactDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EmployeeContactDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load employeeContact on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.employeeContact).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
