import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContinentDetailComponent } from './continent-detail.component';

describe('Continent Management Detail Component', () => {
  let comp: ContinentDetailComponent;
  let fixture: ComponentFixture<ContinentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ContinentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ continent: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ContinentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ContinentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load continent on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.continent).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
