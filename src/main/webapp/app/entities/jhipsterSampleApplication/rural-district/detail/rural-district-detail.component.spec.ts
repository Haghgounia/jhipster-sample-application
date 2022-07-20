import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RuralDistrictDetailComponent } from './rural-district-detail.component';

describe('RuralDistrict Management Detail Component', () => {
  let comp: RuralDistrictDetailComponent;
  let fixture: ComponentFixture<RuralDistrictDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RuralDistrictDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ ruralDistrict: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RuralDistrictDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RuralDistrictDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load ruralDistrict on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.ruralDistrict).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
