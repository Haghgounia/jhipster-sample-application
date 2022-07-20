import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JobOpeningDetailComponent } from './job-opening-detail.component';

describe('JobOpening Management Detail Component', () => {
  let comp: JobOpeningDetailComponent;
  let fixture: ComponentFixture<JobOpeningDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [JobOpeningDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ jobOpening: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(JobOpeningDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(JobOpeningDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load jobOpening on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.jobOpening).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
