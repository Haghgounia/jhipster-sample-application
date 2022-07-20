import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AssetStatusDetailComponent } from './asset-status-detail.component';

describe('AssetStatus Management Detail Component', () => {
  let comp: AssetStatusDetailComponent;
  let fixture: ComponentFixture<AssetStatusDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AssetStatusDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ assetStatus: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AssetStatusDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AssetStatusDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load assetStatus on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.assetStatus).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
